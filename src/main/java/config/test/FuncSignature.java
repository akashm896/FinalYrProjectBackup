/*
Copyright 2020 IIT Bombay.

This software is dual-licensed under commercial and open source licenses.
For open source use, this software is available under LGPL v3 license
(https://www.gnu.org/licenses/lgpl-3.0.en.html). For commercial uses
(beyond those permitted as per LGPL v3), contact dbridge@cse.iitb.ac.in.
*/

package config.test;

public class FuncSignature{
    /**
     * Fully resolved class path.
     * Eg: wilos.business.services.spem2.guide.GuidanceService
     */
    public final String classPathRef;
    /**
     * Function signature (with type and parameter types)
     * Eg: java.util.Set getRoleDefinitions(wilos.model.spem2.guide.Guidance)
     */
    public final String funcSign;
    /**
     * id for reference
     */
    public final int id;

    public FuncSignature(int id, String classPathRef, String funcSign) {
        this.id = id;
        this.classPathRef = classPathRef;
        this.funcSign = funcSign;
    }
}