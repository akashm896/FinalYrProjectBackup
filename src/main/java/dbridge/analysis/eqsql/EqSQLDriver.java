/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

/*
 * EeDag implementation for Hibernate code
 */

package dbridge.analysis.eqsql;

import io.geetam.github.Autowire.ServiceAllocTransform;
import dbridge.analysis.eqsql.analysis.*;
import dbridge.analysis.eqsql.expr.node.HQLTranslatable;
import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.trans.Rule;
import dbridge.analysis.eqsql.util.SootClassHelper;
import dbridge.analysis.region.api.RegionAnalyzer;
import exceptions.HQLTranslationException;
import exceptions.RewriteException;
import config.EqSQLConfig;
import mytest.debug;
import soot.*;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.options.Options;

import java.util.*;

public class EqSQLDriver {
    public static String currFuncSig;
    /**
     * Signature of the class whose functions are to be rewritten
     */
    private String classSignature;
    /**
     * Full signature (including class) of the individual functions to be rewritten
     */
    private String funcSignature;
    /**
     * Object that contains cross-method information about the function being analyzed.
     */
    private FuncStackAnalyzer fsa;
    /**
     * Path to locate the byte-compiled classes
     */
    private String inputPath;
    /**
     * Path to write the output files
     */
    private String outputPath;

    /**
     * @param classSignature Signature of the class whose function is to be rewritten
     * @param funcSignature <b>Subsignature</b> of function to be analyzed and rewritten.
     */
    public EqSQLDriver(String inputPath, String outputPath, String classSignature, String funcSignature){
        this.classSignature = classSignature;

        /* We are doing redundant work here by combining class name and method name which is later again
        * split in some places.
        * //TODO Identify whether the class + method sign is needed anywhere. If not
        * use only method sign and remove this redundancy */
        this.funcSignature = SootClassHelper.appendClassName(classSignature, funcSignature);
        this.fsa = new FuncStackAnalyzer(this.funcSignature);
        currFuncSig = this.funcSignature;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

	public Node getExpr() {
        /* If SQL extraction was attempted but failed for some function, its subsignature
         * will be added to this list. And returned at the end. */
        Set<String> failedFuncSignatures = new HashSet<>();

        /* Set soot entry points */
        doSetEntryPoints();

        /* Initialize region analysis framework */
        RegionAnalyzer.initialize(DIRRegionAnalyzer.INSTANCE, DIRBranchRegionSpecialAnalyzer.INSTANCE, DIRBranchRegionAnalyzer.INSTANCE, DIRLoopRegionAnalyzer.INSTANCE, DIRSequentialRegionAnalyzer.INSTANCE);

        /*  Run soot packs to get required information.
         * Our implementation constructs a stack of all functions called from each top level function we wish
         * to rewrite, and soot packs populate information about all functions in each stack.
         */

        /*
            Geetam: Service fields in spring applications are often autowired, i.e. they are not explicitly allocated.
            This makes their points-to set empty and soot will not include any method call with that field as the receiver
            in the call graph.
         */
        PackManager.v().getPack("jtp").add(new Transform("jtp.ServiceAllocTransform", new ServiceAllocTransform()));
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.newSample", new FuncStackInfoBuilder(fsa)));
        SootMethod mainMethod = Scene.v().getMethod("<" + this.funcSignature + ">");
        // cg pack is applied because FuncInfoStackBuilder transformation in wjtp phase needs call graph
        //Geetam: No need for the above
        PhaseOptions.v().setPhaseOption("cg", "verbose");

        /*Geetam: Cleaner to just runPacks instead of getting them and then calling .apply

         */
        PackManager.v().getPack("jtp").apply(mainMethod.retrieveActiveBody());
        PackManager.v().runPacks();
       // PackManager.v().getPack("cg").apply();
        //PackManager.v().getPack("wjtp").apply();

//        PackManager.v().getPack("cg").apply();
//        System.out.println("main method has active body after cg " + mainMethod.hasActiveBody());
//        PackManager.v().getPack("jtp").apply(mainMethod.retrieveActiveBody());
//        PackManager.v().getPack("cg").apply();
//        PackManager.v().getPack("wjtp").apply();//updates fsa.success
//        System.out.println("main method has active body after wjtp " + mainMethod.hasActiveBody());

        /* run the analysis to compute the algebraic expression
         * and other necessary information. */
        if (fsa.isSuccess()) {
            try {
                fsa.run(); //fsa state gets updated by this call.
            } catch (Exception | AssertionError e) {
                e.printStackTrace();
                fsa.setSuccess(false);
            }
        }
        return fsa.isSuccess() ? fsa.getExpr() : null;
    }

    public boolean rewrite(Node bestExpr) {
        assert bestExpr != null;
        try {
            String query = getQuery(bestExpr);
            JInstanceFieldRef daoIfr = findDaoInstanceRef(fsa.getTopLevelFuncBody());
            BodyRewriter br = new BodyRewriter(query, fsa.getRetRegion(),
                    fsa.getTopLevelFuncBody(), fsa.getVarType(), fsa.getLoopsSwallowed(),daoIfr);
            //br.rewriteBody();
            rewriteClass(classSignature);
        } catch (Exception | AssertionError e){
            e.printStackTrace();
            System.err.println(e.getMessage());
            fsa.setSuccess(false);
        }

        return fsa.isSuccess();
	}

    private String getQuery(Node bestExpr) throws HQLTranslationException {
        if(!(bestExpr instanceof HQLTranslatable)){
            throw new HQLTranslationException(bestExpr + " is not HQLTranslatable");
        }
        return ((HQLTranslatable) bestExpr).toHibQuery();
    }


    private JInstanceFieldRef findDaoInstanceRef(Body funcBody) throws RewriteException {
        for (Unit unit : funcBody.getUnits()) {
            if(unit instanceof JAssignStmt){
                JAssignStmt assignStmt = (JAssignStmt)unit;
                Value value = assignStmt.rightBox.getValue();
                if(value instanceof JInstanceFieldRef){
                    JInstanceFieldRef ifr = (JInstanceFieldRef) value;
                    SootFieldRef fieldRef = ifr.getFieldRef();
                    if(fieldRef.type().toString().endsWith("Dao")){
                        return ifr;
                    }
                }
            }
        }

        throw new RewriteException("No dao object found");
    }

    /**
     * Generate modified source code for the class <code>className</code>
     * @param className Signature of the class to be rewritten
     */
    private void rewriteClass(String className) {
        String[] args;
        try {
            String sootOptions;
            sootOptions = " -O -p cg enabled:false -p jb " +
                    "use-original-names:true " +
                    "-d " + outputPath + " " +
                    "-p tag enabled:true " +
                    "--keep-line-number " +
                    "-p jb.dae enabled:true -p jop.dae " +
                    "enabled:true -p jb.uce enabled:true -p jb.ule enabled:true -p jop.lcm enabled:true -p db" +
                    ".deobfuscate enabled:true -f " + dbridge.common.Config.OUTPUT_FORMAT;
            args = (className + sootOptions).split(" ");

            soot.Main.main(args);
            G.v().reset();
        } catch (Exception e) {
            System.err.println("Exception in rewriteClass " + e.getStackTrace());
        }
    }

    /**
     * Set the function signatures as soot entry points.
     */
    private void doSetEntryPoints() {
        debug d = new debug("EqSQLDriver.java", "doSetEntryPoints");
        String rtJarPath = EqSQLConfig.RT_JAR_PATH;
        d.dg("inputPath is " + inputPath);
        String options = "-soot-class-path " + inputPath +
                (rtJarPath == null ? "" : (";" + rtJarPath)) +
                " -w -p jb " +
                "preserve-source-annotations:true -p jb " +
                "use-original-names:true " +
                "-p tag enabled:true " +
                "--keep-line-number " +
                "-x java -x org -x javax -x soot -x sun -allow-phantom-refs -no-bodies-for-excluded -f " + dbridge.common.Config.OUTPUT_FORMAT;

        Options.v().set_soot_classpath("");//to avoid duplicate classpath error
        String[] args = options.split(" ");
        Options.v().parse(args);

        SootClass c = Scene.v().forceResolve(classSignature, SootClass.BODIES);
        c.setApplicationClass();
        Scene.v().loadNecessaryClasses();

        List entryPoints = new ArrayList();
        String subsignature = SootClassHelper.getMethodSubsignature(funcSignature);
        SootMethod method = c.getMethod(subsignature);
        entryPoints.add(method);
        Scene.v().setEntryPoints(entryPoints);
    }

    public boolean doEqSQLRewrite() {
        debug d = new debug("EqSQLDriver.java", "doEqSQLRewrite()");
        Node expr = getExpr();
        d.dg("Before Transform:");
        d.dg(expr);
        boolean success = false;
        if(expr != null){
            expr = doTransform(expr);
            d.dg("after transform, expr = ");
            d.dg(expr);
            //success = rewrite(expr);
            success = true;
        }

        return success;
    }


    public static Node doTransform(Node expr) {
        /* apply simplifications */
        expr =  applyTransRules(expr, Rule.getSimplificationRules());
            /* do fold transformations */
        expr = applyTransRules(expr, Rule.getFoldTransRules());
            /* apply simplifications once again */
        expr = applyTransRules(expr, Rule.getSimplificationRules());
        return expr;
    }

    public static Node applyTransRules(Node inNode, List<Rule> rules){
        Node transNode = inNode;
        int numIterations = 2; //no of iterations to apply trans rules

        for(int i=0;i<numIterations;i++) {
            for (Rule rule : rules) {
                transNode = transNode.accept(rule);
            }
        }
        return transNode;
    }

}