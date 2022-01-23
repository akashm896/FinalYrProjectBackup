/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package dbridge.analysis.eqsql.util;

/**
 * Created by K. Venkatesh Emani on 12/21/2016.
 */
public class SootClassHelper {
    private static String CLASS_METHOD_SEPARATOR = ": ";

    public static String getClassName(String fullMethodSignature){
        String[] split = fullMethodSignature.split(CLASS_METHOD_SEPARATOR);
        return split[0];
    }

    public static String getMethodSubsignature(String fullMethodSignature){
        String[] split = fullMethodSignature.split(CLASS_METHOD_SEPARATOR);
        return split[1];
    }

    public static String appendClassName(String classSignature, String funcSignature) {
        return classSignature + CLASS_METHOD_SEPARATOR + funcSignature;
    }

    public static String trimSootMethodSignature(String methodSignature) {
        return methodSignature.substring(1, methodSignature.length() - 1);
    }
}
