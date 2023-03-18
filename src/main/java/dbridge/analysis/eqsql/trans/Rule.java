/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.trans;

import dbridge.analysis.eqsql.expr.node.Node;
import dbridge.analysis.eqsql.expr.node.NodeFactory;
import dbridge.analysis.eqsql.expr.operator.Operator;
import dbridge.analysis.eqsql.expr.operator.OpType;
import dbridge.analysis.eqsql.trans.fold.*;
import dbridge.analysis.eqsql.trans.simplify.*;
import dbridge.visitor.NodeVisitor;

import java.util.*;

/**
 * Created by ek on 31/10/16.
 */
public class Rule implements NodeVisitor {
    private InputTree inPattern;
    private OutputTree outPattern;
    protected Map<Integer, Node> binding;
    private Node inExpr;

    public Rule(InputTree inPattern, OutputTree outPattern) {
        this.inPattern = inPattern;
        this.outPattern = outPattern;
    }

    /**
     * Check whether the rule is applicable on this expression.
     * Note: inExpr is read and initialized in this function rather than in the constructor,
     * so that the same instance of a rule can be used to transform multiple expressions.
     * @param inExpr The input expression which should be checked for applicability of transformation
     * @return true if rule is applicable, false otherwise
     */
    private boolean match(Node inExpr){
        this.inExpr = inExpr;
        binding = new HashMap<>();

        return matchHelper(this.inExpr, this.inPattern);
    }

    /**
     * Any preconditions that need to be checked by individual transformation rules (for example: query should
     * contain a key column). Individual transformations should override this function with appropriate logic. The
     * base class implementation returns true.
     *
     * Note that this function is used after binding, so binding information is available for use in this function.
     *
     * Right now, this function returns only true or false. However, it may be necessary to store some information
     * computed during precondition check that is required to construct the output expression. Enabling this is TODO.
     */
    public boolean checkPreconds(Operator root){
        return true;
    }

    private boolean matchHelper(Node inExpr, InputTree inPattern) {
        Operator exprOp = inExpr.getOperator();
        OpType patternOpType = inPattern.getOpType();
        boolean isMatch = false;

        if(patternOpType == OpType.Any ||exprOp.getType() == patternOpType) {
            isMatch = true;
            if (binding.containsKey(inPattern.getId()) && !binding.get(inPattern.getId()).toString().equals(inExpr.toString()))
                return false;
            binding.put(inPattern.getId(), inExpr);
            if(inPattern.hasChildren()) {
                if(inExpr.getNumChildren() != inPattern.getNumChildren()){
                    doCleanup(); //clear bindings
                    return false;
                }
                for (int i = 0; i < inPattern.getNumChildren(); i++) {
                    Node exprChild = inExpr.getChild(i);
                    InputTree patternChild = inPattern.getChild(i);

                    if(matchHelper(exprChild, patternChild)){
                        binding.put(patternChild.getId(), exprChild);
                    }
                    else {
                        doCleanup(); //clear bindings
                        return false;
                    }
                }
            }
        }

        return isMatch;
    }

    private Node apply(Node _inExpr) {
        /* Match the pattern and setup bindings */
        boolean isMatch = match(_inExpr);
        /* Check for other preconditions */
        boolean isApplicable = isMatch && checkPreconds(_inExpr.getOperator());

        if(isApplicable){
            Node outExpr = getOutputExpr(this.outPattern);
            doCleanup();

            return outExpr;
        }
        /* not applicable, so return the input expression as is */
        return this.inExpr;
    }

    private void doCleanup() {
        binding.clear();
    }

    /**
     * This pattern
     * @param outPattern
     * @return
     */
    protected Node getOutputExpr(OutputTree outPattern) {
        if(outPattern.isFromInput()){
            return binding.get(outPattern.getId());
        }
        else if(outPattern.isFromScratch()){
            Node node = binding.get(outPattern.getId());
            return outPattern.getNode(node == null ? null : node.getOperator());
        }
        else{
            assert outPattern.isTree();
            OpType opType = outPattern.getOpType();
            List<Object> childrenExpr = new ArrayList<>();
            for (OutputTree childTree : outPattern.getChildren()) {
                childrenExpr.add(getOutputExpr(childTree));
            }
            return NodeFactory.constructFromOp(opType, childrenExpr.toArray());
        }
    }

    @Override
    public Node visit(Node node) {
        return apply(node);
    }

    public static List<Rule> getSimplificationRules(){
        List<Rule> simpliRules = new ArrayList<>();
        simpliRules.add(new RuleS1A());
        simpliRules.add(new RuleS1B());
        simpliRules.add(new RuleS2());
        simpliRules.add(new RuleS3());
        simpliRules.add(new RuleS4());
        simpliRules.add(new RuleS5());
        simpliRules.add(new RuleS6());
        simpliRules.add(new RuleS7A());
        simpliRules.add(new RuleS7B());
        simpliRules.add(new RuleS7C());
        simpliRules.add(new RuleS8A());
        simpliRules.add(new RuleS8B());

        return simpliRules;
    }

    public static List<Rule> getFoldTransRules(){
        List<Rule> foldTransRules = new ArrayList<>();
        foldTransRules.add(new RuleT1A());
        foldTransRules.add(new RuleT1B());
        foldTransRules.add(new RuleT1C());
        foldTransRules.add(new RuleT2());
        foldTransRules.add(new RuleT3());
        foldTransRules.add(new RuleT4());
        foldTransRules.add(new RuleT5());

        return foldTransRules;
    }

    @Override
    public String toString() {
        return "In Pattern:\n" +
                inPattern.toString() +
                "\n\nOut Pattern:\n" +
                outPattern.toString();
    }

    public InputTree getInPattern(){
        return inPattern;
    }

    public OutputTree getOutPattern(){
        return outPattern;
    }
}
