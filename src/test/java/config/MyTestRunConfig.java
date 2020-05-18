package config;

import config.test.EqSQLRunConfig;
import config.test.FuncSignature;

import java.util.ArrayList;
import java.util.List;

public class MyTestRunConfig implements EqSQLRunConfig {

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
                "mytest.Owner: java.lang.String getFirstName()",//1
                "mytest.Owner: java.lang.Boolean bar()",//2
                "mytest.Owner: java.lang.Boolean foo()",//3
                "mytest.Owner: java.lang.Boolean foo2(java.lang.Integer,java.lang.Integer)",//4
                "wilos.business.services.misc.custom.CustomService: int getNumUnfinishedProjects()",//5
                "wilos.business.services.misc.project.ProjectService: java.util.List getAllProjects()",//6
                "wilos.business.services.misc.custom.CustomService: java.util.Set genReport()",//7
                "xyz.technoguy.addressbook.controller.ViewController: java.lang.String contacts(org.springframework.ui.Model)",//8
                "xyz.technoguy.addressbook.controller.ViewController: org.springframework.web.servlet.ModelAndView getContact(org.springframework.web.servlet.ModelAndView,java.lang.String,xyz.technoguy.addressbook.persistence.entity.Contact)", //9
                "com.shakeel.controller.ProductsController: java.lang.String product(java.lang.Long,org.springframework.ui.Model)", //10
                "com.shakeel.controller.ProductsController: java.lang.String productsList(org.springframework.ui.Model)", //11
                "org.springframework.samples.petclinic.owner.OwnerRepository: org.springframework.samples.petclinic.owner.Owner findById(java.lang.Integer)", //12
                "org.springframework.samples.petclinic.owner.OwnerController: java.lang.String initUpdateOwnerForm(int,org.springframework.ui.Model)" //13
        };

        for(int i = 0; i < func_signatures.length; i++){
            String[] split = func_signatures[i].split(": ");
            fsl.add(new FuncSignature(i+1, split[0], split[1]));
        }

        return fsl;
    }
}

