package wilos.test.business.services.misc.custom;

import org.junit.Test;
import shared.EqSQLComparator;
import shared.EqSQLTest;
import shared.EqSQLTestHelper;
import wilos.business.services.misc.custom.CustomService;
import wilos.business.services.misc.project.ProjectService;
import wilos.model.misc.project.Project;
import wilos.test.TestConfiguration;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by K. Venkatesh Emani on 5/7/2017.
 */
public class CustomServiceTest {
    private ProjectService ps;
    private CustomService cs;
    private EqSQLComparator projectComparator;

    public CustomServiceTest() {
        this.ps = (ProjectService) TestConfiguration.getInstance()
                .getApplicationContext().getBean("ProjectService");
        this.cs = (CustomService) TestConfiguration.getInstance()
                .getApplicationContext().getBean("CustomService");

        this.projectComparator = new EqSQLComparator() {
            @Override
            public boolean compare(Collection c1, Collection c2) {
                if(c1 == null || c2 == null){
                    return false;
                }
                assert (c1 instanceof Set && c2 instanceof Set);

                if(c1.size() != c2.size()){
                    return false;
                }
                System.out.println("c1 size:" + c1.size());
                System.out.println("c2 size:" + c2.size());

                //check if objects fetched are the same
                Set<String> c1Ids = findProjectIds(c1);
                Set<String> c2Ids = findProjectIds(c2);
                return c1Ids.containsAll(c2Ids);
            }
        };
    }

    private Set<String> findProjectIds(Collection c1) {
        Set<String> ids = new HashSet<>();
        for (Object o : c1) {
            assert o instanceof Project;
            ids.add(((Project)o).getId());
        }
        return ids;
    }

    @Test
    public void testGenerateData(){
        Project p;
        Project p2;

        p = new Project();
        p.setConcreteName("Wilos");
        p.setDescription("projet de test");
        p.setIsFinished(true);
        this.ps.getProjectDao().saveOrUpdateProject(p);

        p2 = new Project();
        p2.setConcreteName("Wilos2");
        p2.setDescription("projet de test2");
        p2.setIsFinished(false);
        this.ps.getProjectDao().saveOrUpdateProject(p2);
    }

    @Test
    public void testGenReportA(){
        long startTime = System.currentTimeMillis();
        List<Integer> dateDimIds = this.cs.genReportA(startTime);
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime-startTime)*1.0/1000; //seconds
        System.out.println("Total time taken (s): " + timeTaken);
        System.out.println(dateDimIds.size());
    }

    @Test
    public void testGenReportB(){
        long startTime = System.currentTimeMillis();
        List<Integer> dateDims = this.cs.genReportB(startTime);
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime-startTime)*1.0/1000; //seconds
        System.out.println("Time taken (s): " + timeTaken);
        System.out.println(dateDims.size());
    }

    @Test
    public void testGenReportC(){
        long startTime = System.currentTimeMillis();
        List<Integer> dateDimIds = this.cs.genReportC(startTime);
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime-startTime)*1.0/1000; //seconds
        System.out.println("Time taken (s): " + timeTaken);
        System.out.println(dateDimIds.size());
    }

    @Test
    public void testGetUnfinishedProjects() {
        EqSQLTestHelper.doTest(new EqSQLTest() {
            @Override
            public Collection test() {
                Set<Project> unfinishedProjects = ps.getUnfinishedProjects();
                return unfinishedProjects;
            }
        }, projectComparator);
    }

    @Test
    public void testGetAllProjectsWithNoProcess() {
        EqSQLTestHelper.doTest(new EqSQLTest() {
            @Override
            public Collection test() {
                Set<Project> projNoProcess = ps.getAllProjectsWithNoProcess();
                return projNoProcess;
            }
        }, projectComparator);
    }
}
