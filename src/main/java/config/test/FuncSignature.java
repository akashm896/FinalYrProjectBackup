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