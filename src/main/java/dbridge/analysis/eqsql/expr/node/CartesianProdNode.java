/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.expr.node;

import dbridge.analysis.eqsql.expr.operator.CartesianProdOp;
import exceptions.HQLTranslationException;

/**
 * Created by venkatesh on 7/7/17.
 * Node to represent a single relation or a cartesian product of relations.
 */
public class CartesianProdNode extends Node implements HQLTranslatable {

    public CartesianProdNode(Node... relations){
        super(new CartesianProdOp(relations.length), relations);
    }

    @Override
    public String toHibQuery() throws HQLTranslationException {
        StringBuilder hibQuery = new StringBuilder("from ");
        for (int i = 0; i < children.length; i++) {
            Node child = children[i];
            String childHQL;
            if(child instanceof ClassRefNode){
                ClassRefNode crn = (ClassRefNode)child;
                childHQL = crn.toHibQuery() + " as " + crn.getAlias();
                hibQuery.append(i == 0 ? childHQL : ", " + childHQL);
            }
            else if(child instanceof FieldRefNode){
                FieldRefNode frn = (FieldRefNode) child;
                if(i ==0){
                    ClassRefNode crn = frn.getTypeClassRef();
                    childHQL = crn.toHibQuery() + " as " + crn.getAlias();
                }
                else {
                    childHQL = "join fetch " + frn.toHibQuery();
                }
                hibQuery.append(" ").append(childHQL);
            }
            else{
                throw new HQLTranslationException(child + " is not a ClassRef or FieldRef");
            }
        }

        return hibQuery.toString();
    }

}
