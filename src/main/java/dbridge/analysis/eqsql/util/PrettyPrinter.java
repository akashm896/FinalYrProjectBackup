/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.util;

import java.util.List;

/**
 * Created by ek on 2/11/16.
 */
public class PrettyPrinter {
    public static String makeTreeString(Object root, List children){
        String prettyString = root.toString();

        if(children == null || children.size() == 0) {
//            if(prettyString.contains(".") && prettyString.contains("(") && prettyString.contains(")"))
//            return prettyString.substring(prettyString.lastIndexOf('.') + 1, prettyString.length() - 1);
//            else
                return prettyString;
        }

        for (Object child : children) {
            String childStr = (child == null) ? "Null" : child.toString();
            childStr = doIndent(childStr);
            prettyString = prettyString + "\n" +
                    childStr;
        }

        return prettyString;
    }

    /**
     * Indent each new line in str (including the first)
     */
    public static String doIndent(String str) {
        String indent = "| ";
        str = indent + str;
        str = str.replace("\n","\n" + indent);

        return str;
    }
}
