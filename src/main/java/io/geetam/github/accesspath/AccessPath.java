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
package io.geetam.github.accesspath;

import dbridge.analysis.eqsql.expr.node.VarNode;
import mytest.debug;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static dbridge.analysis.eqsql.hibernate.construct.Utils.bcelActualCollectionFieldType;


public class AccessPath {
    private Deque<String> path;

    public AccessPath() {
        path = new ArrayDeque<>();
    }
    public AccessPath(AccessPath ap) {
        path = new ArrayDeque<>();
        path.addAll(ap.getPath());
    }

    public AccessPath(String strPath) {
        path = new ArrayDeque<>();
        String[] segments = strPath.split("[.]");
        if(segments.length == 0) {
            path.add(strPath);
        }
        for(String s : segments) {
            path.add(s);
        }
    }

    public String getBase() {
        return path.peek();
    }

    public Deque<String> getPath() {
        return path;
    }

    public static boolean isTerminalType(Type type) {
        String typeStr = type.toString();
        switch (typeStr) {
            case "java.math.BigDecimal":
            case "java.lang.Long":
            case "java.lang.Boolean":
            case "java.lang.Double":
            case "java.time.LocalDate":
            case "java.lang.String":
            case "java.util.Collection":
            case "java.lang.StringBuilder":
            case "java.lang.Integer":
            case "java.lang.Iterable":
            case "java.util.List":
            case "java.util.Set":
//            case "java.lang.Object":
            case "org.springframework.data.domain.Page": {
                return true;
            }

            default:
                if(typeStr.endsWith("Repository"))
                    return true;
                return type instanceof RefType == false;
        }
    }

    public static boolean isPrimitiveType(Type type) {
        String typeStr = type.toString();
        switch (typeStr) {
            case "java.math.BigDecimal":
            case "java.lang.Long":
            case "java.lang.Double":
            case "java.time.LocalDate":
            case "java.lang.String":
            case "java.lang.Integer":
            case "java.util.Date":
            case "java.lang.Boolean": {
                return true;
            }

            default:
                return type instanceof RefType == false;
        }
    }

    public static boolean isCollectionType(Type type) {
        String typeStr = type.toString();
        switch (typeStr) {
            case "java.util.List":
            case "java.util.Collection":
            case "java.lang.Iterable":
            case "java.util.Queue":
            case "java.util.Set":
            case "java.util.Dequeue":
            case "java.util.HashSet":
            case "org.springframework.data.domain.Page":
                return true;
            default:
                return false;
        }
    }

    // MyImpl ///

    public static boolean isReturnTypeEntity(InvokeExpr invokeExpr){
        debug d= new debug("AccessPath.java","isReturnTypeEntity()");
        d.dg("check :"+ invokeExpr.getType());
        d.dg("tags = "+invokeExpr.getMethod().getTags());
//        d.dg(" ***  "+ invokeExpr.getMethod().getTags().get(0).getClass());
//        d.dg(" ***  "+ ((SignatureTag)invokeExpr.getMethod().getTags().get(0)).getSignature());
        String retType = "";
        if(invokeExpr.getMethod().getTags().size() == 0) { // this is to handle findAll() cases
            String repo = invokeExpr.getMethodRef().declaringClass().toString();
            SootClass sootRepoClass = Scene.v().loadClass(repo, 2);
            String repoGenerics = sootRepoClass.getTags().get(0).toString();
            String firstGeneric = repoGenerics.substring(repoGenerics.indexOf('<')+2);
            firstGeneric = firstGeneric.split(";")[0];
            firstGeneric = firstGeneric.replace("/", ".");
            retType = firstGeneric;
        }
        else {
            int ind1 = invokeExpr.getMethod().getTags().get(0).toString().indexOf('<');
            int ind2 = invokeExpr.getMethod().getTags().get(0).toString().indexOf('>');
            retType = invokeExpr.getMethod().getTags().get(0).toString().substring(ind1 + 2, ind2 - 1);
            retType = retType.replace('/', '.');
            d.dg("method retType = " + retType);
        }
        SootClass leftTypeClass = Scene.v().getSootClass(retType);
        return !isPrimitiveType(leftTypeClass.getType());
    }

    public static Type getCollectionEntityType(InvokeExpr invokeExpr){
        debug d= new debug("AccessPath.java","getCollectionEntityType()");
        d.dg(invokeExpr);
        d.dg(invokeExpr.getMethod().getTags());
        String retType = "";
        if(invokeExpr.getMethod().getTags().size() == 0) { // this is to handle findAll() cases
            String repo = invokeExpr.getMethodRef().declaringClass().toString();
            SootClass sootRepoClass = Scene.v().loadClass(repo, 2);
            String repoGenerics = sootRepoClass.getTags().get(0).toString();
            String firstGeneric = repoGenerics.substring(repoGenerics.indexOf('<')+2);
            firstGeneric = firstGeneric.split(";")[0];
            firstGeneric = firstGeneric.replace("/", ".");
            retType = firstGeneric;
        }
        else {
            int ind1 = invokeExpr.getMethod().getTags().get(0).toString().indexOf('<');
            int ind2 = invokeExpr.getMethod().getTags().get(0).toString().indexOf('>');
            retType = invokeExpr.getMethod().getTags().get(0).toString().substring(ind1 + 2, ind2 - 1);
            d.dg(retType);
            retType = retType.replace('/', '.');
        }
        SootClass leftTypeClass = Scene.v().getSootClass(retType);
        d.dg("Collection Entity Type = "+leftTypeClass.getType());
        return leftTypeClass.getType();
    }

    public static String getCollectionType(SootField field){
        debug d=new debug("AccessPath.java","getCollectionType");
        String type = "";
        List<Tag> tags= field.getTags();
        d.dg("field tags = "+tags);
        int ind1= field.getTags().get(0).toString().indexOf('<');
        int ind2= field.getTags().get(0).toString().indexOf('>');
        type = field.getTags().get(0).toString().substring(ind1+2,ind2-1);
        type = type.replace('/','.');
        d.dg("Collection field Type = "+type);
        return type;
    }


    // MyImpl End //

    //Number of dots in the access path
    int getLength() {
        return path.size() - 1;
    }

    @Override
    public String toString() {
        StringBuilder retBuilder = new StringBuilder();
        for(String sub : path) {
            retBuilder.append(sub);
            if(sub != path.getLast()) {
                retBuilder.append(".");
            }
        }
        return retBuilder.toString();
    }

    public static AccessPath replaceBase(AccessPath acp, String toReplaceWith) {
        debug d = new debug("AccessPath.java", "replaceBase()");
        d.dg("input access path: " + acp);
        d.dg("replacement of base: " + toReplaceWith);
        AccessPath newAccp = new AccessPath(acp);
        newAccp.getPath().removeFirst();
        newAccp.getPath().addFirst(toReplaceWith);
        return newAccp;
    }

    public VarNode toVarNode() {
        return new VarNode(toString());
    }

    public AccessPath clone() {
        AccessPath ret = new AccessPath();
        ret.path.addAll(this.path);
        return ret;
    }

    public void append(String toAppend) {
        this.path.add(toAppend);
    }

    public void prepend(String toPrepend) {
        this.path.addFirst(toPrepend);
    }
}
