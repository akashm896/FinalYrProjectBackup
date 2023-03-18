// Arguements : <ProcessOrTargetDirectory> <MainClass> <TargetClass> <TargetMethod>
//Ref: 1) https://gist.github.com/bdqnghi/9d8d990b29caeb4e5157d7df35e083ce
//     2) https://github.com/soot-oss/soot/wiki/Tutorials



////////////////////////////////////////////////////////////////////////////////
import java.util.*;

////////////////////////////////////////////////////////////////////////////////

import soot.*;
import soot.dava.DavaBody;
import soot.options.Options;

import soot.jimple.Stmt;

import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.util.Chain;

////////////////////////////////////////////////////////////////////////////////



public class Analysis {
    private static HashMap<String, Boolean> visited = new
        HashMap<String, Boolean>();

    public void doAnalysis() {
        /*************************************************************
         * XXX you can implement your analysis here
         ************************************************************/
    }

    public static void main(String[] args) {

        //String targetDirectory="./target";
        //String mClass="AddNumFun";
        //String tClass="AddNumFun";
        //String tMethod="expr"

//        String targetDirectory="target/classes/com/bookstore/controller"; // args[0];
        String targetDirectory=".";
        String mClass="com.bookstore.BookstoreApplication"; // args[1];
//        String tClass1="com.bookstore.controller.HomeController"; // args[2];
//        String tMethod1="java.lang.String newUserPost(javax.servlet.http.HttpServletRequest,java.lang.String,java.lang.String,org.springframework.ui.Model)>"; // args[3];

        String tClass1 = "org.springframework.samples.petclinic.owner.Owner";
        String tMethod1 = "createUser";
        boolean methodFound=false;

////////////////////////////////////////////////////////

//        soot.G.reset();
//        Options.v().set_prepend_classpath(true);
//        Options.v().set_src_prec(Options.src_prec_only_class);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
//        Options.v().set_output_format(Options.output_format_none);
//        Options.v().set_keep_line_number(true);
        Options.v().set_no_writeout_body_releasing(true);
//        Options.v().setPhaseOption("cg.spark", "verbose:false");

        Scene.v().setSootClassPath(System.getProperty("java.class.path"));
        SootClass sc = Scene.v().loadClass(tClass1, 2);
        Scene.v().loadNecessaryClasses();
        Chain<SootField> fields = sc.getFields();
        List <SootField> uniqueFields = new ArrayList<>();
        int numOfMethods = sc.getMethods().size();
        List<SootMethod> methods = sc.getMethods();

        for(SootMethod m : methods){
            if(m.toString().indexOf(tMethod1) != -1) {
                try {
                    System.out.println("Body of method = \n" + m.retrieveActiveBody().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            if(m.toString().indexOf(tMethod) != -1) {
//                System.out.println("Method found \n ");
//                System.out.println(m.getActiveBody().toString());
//                if (m.hasActiveBody()) {
//                    System.out.println("Has body");
//                }
//                else
//                    System.out.println("No body present");
//            }
        }
//        System.out.println("Method not found");
        ////////////////////////////////////////////////////////////

//        Scene.v().setSootClassPath(System.getProperty("java.class.path"));
//        SootClass sc2 = Scene.v().loadClass(tClass2, 2);
//        Scene.v().loadNecessaryClasses();
//        List<SootMethod> methods2 = sc2.getMethods();
//
//        for(SootMethod m : methods2){
//            if(m.toString().indexOf(tMethod2) != -1) {
//                try {
//                    System.out.println("Body of method2 = \n" + m.retrieveActiveBody().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if(m.toString().indexOf(tMethod) != -1) {
//                System.out.println("Method found \n ");
//                System.out.println(m.getActiveBody().toString());
//                if (m.hasActiveBody()) {
//                    System.out.println("Has body");
//                }
//                else
//                    System.out.println("No body present");
//            }
        }

















        /*
        System.out.println(uniqueFields + " " + fields);
        List<String> procDir = new ArrayList<String>();
        procDir.add(targetDirectory);

        // Set Soot options
        soot.G.reset();
        Options.v().set_process_dir(procDir);
         Options.v().set_prepend_classpath(true);
        Options.v().set_src_prec(Options.src_prec_only_class);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_none);
        Options.v().set_keep_line_number(true);
        Options.v().setPhaseOption("cg.spark", "verbose:false");

        Scene.v().loadNecessaryClasses();
//        Scene.v().loadBasicClasses();
//        Scene.v().loadDynamicClasses();

        SootClass entryClass = Scene.v().getSootClassUnsafe(mClass);
        SootMethod entryMethod = entryClass.getMethodByNameUnsafe("main");
        SootClass targetClass = Scene.v().getSootClassUnsafe(tClass);
        SootMethod targetMethod = entryClass.getMethodByNameUnsafe(tMethod);

        Options.v().set_main_class(mClass);
        Scene.v().setEntryPoints(Collections.singletonList(entryMethod));

        // System.out.println (entryClass.getName());
        System.out.println("tclass: " + targetClass);
        System.out.println("tmethod: " + targetMethod);
        System.out.println("tmethodname: " + tMethod);
//        int numOfMethods = targetClass.getMethods().size();
        System.out.println(targetClass.getName());
        System.out.println("#methods present: " + numOfMethods);

        Iterator mi = targetClass.getMethods().iterator();
        while (mi.hasNext()) {
            SootMethod sm = (SootMethod)mi.next();

            if(sm.getName().equals(tMethod))
            {methodFound=true; break;}
        }

        if(methodFound) {
            Body body = targetMethod.getActiveBody();
            for(Unit unit : body.getUnits())
                System.out.println(unit);
            System.out.println("\n Body = \n " + body.toString());
            System.out.println("Method found");

//            printInfo(targetMethod);
            /*************************************************************
             * XXX This would be a good place to call the function
             * which performs the Kildalls Analysis
             *************************************************************/
//            drawMethodDependenceGraph(targetMethod);
        /*
        } else {
            System.out.println("ERROR *** Method not found: " + tMethod);
            System.exit(1);
        }*/
    }




