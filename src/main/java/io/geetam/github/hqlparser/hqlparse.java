/*

MIT License

Copyright (c) 2022 Indian Institute of Science, Bangalore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE
SOFTWARE.

*/
package io.geetam.github.hqlparser;
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
        System.out.println(CommonTreeWalk.getRelNode());
        //System.out.println( tree.toStringTree() );
    }

}
