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
                "org.springframework.samples.petclinic.owner.OwnerController: java.lang.String initUpdateOwnerForm(int,org.springframework.ui.Model)", //13
                "org.springframework.samples.petclinic.owner.OwnerController: org.springframework.web.servlet.ModelAndView showOwner(int)", //14
                "org.springframework.samples.petclinic.owner.OwnerController: java.lang.String showOwner2(int)", //15
                "org.springframework.samples.petclinic.owner.OwnerController: java.lang.String initCreationForm(java.util.Map)", //16
                "org.springframework.samples.petclinic.owner.OwnerController: java.lang.String processCreationForm(org.springframework.samples.petclinic.owner.Owner,org.springframework.validation.BindingResult)", //17
                "org.springframework.samples.petclinic.owner.OwnerController: java.lang.String processFindForm(org.springframework.samples.petclinic.owner.Owner,org.springframework.validation.BindingResult,java.util.Map)", //18
                "org.springframework.samples.petclinic.owner.Owner: java.util.List getPets()", //19
                "org.springframework.samples.petclinic.owner.PetController: java.lang.String processCreationForm(org.springframework.samples.petclinic.owner.Owner,org.springframework.samples.petclinic.owner.Pet,org.springframework.validation.BindingResult,org.springframework.ui.ModelMap)", //20
                "org.springframework.samples.petclinic.owner.Owner: org.springframework.samples.petclinic.owner.Pet getPet(java.lang.String,boolean)", //21
                "org.springframework.samples.petclinic.owner.PetController: java.lang.String initUpdateForm(int,org.springframework.ui.ModelMap)", //22
                "org.springframework.samples.petclinic.owner.PetController: java.lang.String initCreationForm(org.springframework.samples.petclinic.owner.Owner,org.springframework.ui.ModelMap)", //23
                "org.springframework.samples.petclinic.owner.PetController: java.lang.String processUpdateForm(org.springframework.samples.petclinic.owner.Pet,org.springframework.validation.BindingResult,org.springframework.samples.petclinic.owner.Owner,org.springframework.ui.ModelMap)", //24
                "com.reljicd.controller.HomeController: java.lang.String home(int,org.springframework.ui.Model)", //25
                "com.reljicd.controller.PostController: java.lang.String newPost(java.security.Principal,org.springframework.ui.Model)", //26
                "com.reljicd.controller.PostController: java.lang.String createNewPost(com.reljicd.model.Post,org.springframework.validation.BindingResult)", //27
                "com.reljicd.controller.PostController: java.lang.String editPostWithId(java.lang.Long,java.security.Principal,org.springframework.ui.Model)", //28
                "com.reljicd.controller.RegistrationController: java.lang.String registration(org.springframework.ui.Model)", //29
                "com.reljicd.controller.RegistrationController: java.lang.String createNewUser(com.reljicd.model.User,org.springframework.validation.BindingResult,org.springframework.ui.Model)", //30
                "com.reljicd.controller.CommentController: java.lang.String commentPostWithId(java.lang.Long,java.security.Principal,org.springframework.ui.Model)", //31
                "com.bookstore.controller.ShoppingCartController: java.lang.String shoppingCart(org.springframework.ui.Model,java.security.Principal)",  //32
                "com.spring.bioMedical.Controller.DoctorController: java.lang.String index(org.springframework.ui.Model)", //33
                "com.spring.bioMedical.Controller.AdminController: java.lang.String doctorDetails(org.springframework.ui.Model)", //34
                "onlineShop.controller.ProductController: java.lang.String getProductForm(org.springframework.ui.Model)", //35
                "onlineShop.controller.ProductController: java.lang.String editProduct(onlineShop.model.Product)", //36
                "com.geetam.tests.AccessPathsToBottomNode: void foo()", //37
                "com.bookstore.service.impl.UserServiceImpl: void setUserDefaultPayment(java.lang.Long,com.bookstore.domain.User)", //38
                "com.shakeel.controller.ProductsController: java.lang.String saveProduct(com.shakeel.model.Product)", //39
                "com.bookstore.controller.HomeController: java.lang.String myProfile(org.springframework.ui.Model,java.security.Principal)", //40
                "com.bookstore.controller.HomeController: java.lang.String listOfCreditCards(org.springframework.ui.Model,java.security.Principal,javax.servlet.http.HttpServletRequest)", //41
                "com.bookstore.controller.HomeController: java.lang.String listOfShippingAddresses(org.springframework.ui.Model,java.security.Principal,javax.servlet.http.HttpServletRequest)", //42
                "com.bookstore.controller.HomeController: java.lang.String addNewCreditCard(org.springframework.ui.Model,java.security.Principal)", //43
                "io.geetam.github.tests.NatLoop: void foo()", //44
                "org.springframework.samples.petclinic.vet.VetController: java.lang.String showVetList(java.util.Map)", //45
                "org.springframework.samples.petclinic.vet.VetController: org.springframework.samples.petclinic.vet.Vets showResourcesVetList()", //46
                "org.springframework.samples.petclinic.owner.VisitController: java.lang.String initNewVisitForm(int,java.util.Map)",//47
                "org.springframework.samples.petclinic.owner.VisitController: java.lang.String processNewVisitForm(org.springframework.samples.petclinic.visit.Visit,org.springframework.validation.BindingResult)",//48
                "org.springframework.samples.petclinic.owner.VisitController: org.springframework.samples.petclinic.visit.Visit loadPetWithVisit(int,java.util.Map)", //48
                "com.gorankitic.springboot.crudthymeleaf.controller.EmployeeController: java.lang.String listEmployees(org.springframework.ui.Model)", //49
                "com.gorankitic.springboot.crudthymeleaf.controller.EmployeeController: java.lang.String showFormAdd(org.springframework.ui.Model)", //50
                "com.gorankitic.springboot.crudthymeleaf.controller.EmployeeController: java.lang.String showFormUpdate(int,org.springframework.ui.Model)", //51
                "com.gorankitic.springboot.crudthymeleaf.controller.EmployeeController: java.lang.String saveEmployee(com.gorankitic.springboot.crudthymeleaf.entity.Employee)", //52
                "com.reljicd.controller.BlogController: java.lang.String blogForUsername(java.lang.String,int,org.springframework.ui.Model)" //53


        };

        for(int i = 0; i < func_signatures.length; i++){
            String[] split = func_signatures[i].split(": ");
            fsl.add(new FuncSignature(i+1, split[0], split[1]));
        }

        return fsl;
    }
}

