package config;

import config.test.EqSQLRunConfig;
import config.test.FuncSignature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by venkatesh on 4/7/17.
 * Configure system specific and input project specific settings here.
 */
public class WilosRunConfig implements EqSQLRunConfig {

    public String inputRoot = "target/classes";
    public String outputRoot = "sootOutput";
    public List<FuncSignature> funcSignatures = makeFuncSignList();

    @Override
    public String getInputRoot() {
        return inputRoot;
    }

    @Override
    public String getOutputRoot() {
        return outputRoot;
    }

    @Override
    public List<FuncSignature> getFuncSignatures() {
        return funcSignatures;
    }

    @Override
    public FuncSignature getFuncSignature(int index) {
        return funcSignatures.get(index);
    }

    static List<FuncSignature> makeFuncSignList() {
        List<FuncSignature> fsl = new ArrayList<>();
        String[] func_signatures = {
                "wilos.business.services.misc.project.ProjectService: java.util.Set getUnfinishedProjects()",//1
                "wilos.business.services.misc.project.ProjectService: java.util.Set getAllProjectsWithNoProcess()",//2
                "wilos.business.services.misc.project.ProjectService: java.util.Set getAllProjectsWithProcess()",//3
                "wilos.business.services.misc.custom.CustomService: java.util.Set getRoleUser()",//4
                "wilos.business.services.misc.custom.CustomService: int getNumUnfinishedProjects()",//5
                "wilos.business.services.misc.project.ProjectService: java.util.List getAllProjects()",//6
                "wilos.business.services.misc.custom.CustomService: java.util.Set genReport()"//7
        };

        for(int i = 0; i < func_signatures.length; i++){
            String[] split = func_signatures[i].split(": ");
            fsl.add(new FuncSignature(i+1, split[0], split[1]));
        }

        return fsl;
    }
}
