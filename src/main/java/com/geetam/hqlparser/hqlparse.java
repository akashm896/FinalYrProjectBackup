package com.geetam.hqlparser;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.hibernate.hql.ast.origin.hql.parse.HQLLexer;
import org.hibernate.hql.ast.origin.hql.parse.HQLParser;
import org.hibernate.hql.ast.origin.hql.parse.HQLParser.statement_return;

public class hqlparse {
    public static void main(String[] args) throws RecognitionException {
        String testCase = "select an.mother.id, max(an.bodyWeight) from Animal an group by an.mother.id having max(an.bodyWeight)>1.0";
        String petclinicFindByLastName = "SELECT DISTINCT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName%";
        String petClinicFindById = "SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id =:id";
        ANTLRStringStream antlrStream = new ANTLRStringStream(petClinicFindById);
        HQLLexer lexer = new HQLLexer( antlrStream );
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        HQLParser parser = new HQLParser( tokens );
        statement_return statement = parser.statement();
       // System.out.println( tokens.getTokens() );
        CommonTree tree = (CommonTree) statement.getTree();
        CommonTreeWalk.postOrder(tree, 0);
        CommonTreeWalk.printInfo();
        //System.out.println( tree.toStringTree() );
    }

}
