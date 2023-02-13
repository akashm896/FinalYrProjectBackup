package wilos.business.services.misc.custom;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wilos.hibernate.misc.order.DateDimDao;
import wilos.hibernate.misc.order.OrderDao;
import wilos.hibernate.misc.project.ProjectDao;
import wilos.hibernate.misc.wilosuser.RoleDao;
import wilos.hibernate.misc.wilosuser.WilosUserDao;
import wilos.model.misc.order.DateDim;
import wilos.model.misc.order.Order;
import wilos.model.misc.project.Project;
import wilos.model.misc.wilosuser.Role;
import wilos.model.misc.wilosuser.WilosUser;

import java.util.*;

/**
 * Created by K. Venkatesh Emani on 3/5/2017.
 * Custom file to illustrate capabilities of our techniques.
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class CustomService {
    private RoleDao roleDao;
    private WilosUserDao wilosUserDao;
    private ProjectDao projectDao;
    private OrderDao orderDao;
    private DateDimDao dateDimDao;

    /** Lazy fetch */
    public Set<DateDim> genReport(){
        List<Order> orders = orderDao.getOrder();
        Set<DateDim> dateDims = new HashSet<>();

        for (Order order : orders) {
            DateDim date = order.getDate();
            dateDims.add(date);
        }
        return dateDims;
    }

    /** Lazy fetch
     * @param startTime*/
    public List<Integer> genReportA(long startTime){
        Session session = orderDao.getSessionFactory().getCurrentSession();
        Query q = session.createQuery("from Order as o");
        List<Integer> dateDimsIds = new ArrayList<>();
        List orders = q.list();
        long ordersTime = System.currentTimeMillis();

        for (Object order : orders) {
            DateDim date = ((Order)order).getDate();
            dateDimsIds.add(date.getDate_sk());
        }
        long datesTime = System.currentTimeMillis();

        System.out.println("Orders time(s): " + ((ordersTime - startTime)*1.0/1000) +  "Dates time(s): " + ((datesTime-ordersTime)*1.0/1000));
        return dateDimsIds;
    }

    /** Lazy fetch
     * @param startTime*/
    public List<Integer> genReportB(long startTime){
        OrderDao daoLocal;
        SessionFactory sessionFactory;
        Session session;
        Query query;
        daoLocal = this.orderDao;
        sessionFactory = daoLocal.getSessionFactory();
        session = sessionFactory.getCurrentSession();
        query = session.createQuery("select o from Order as o join fetch o.date");
        List orders = query.list();
        long ordersTime = System.currentTimeMillis();

        List<Integer> dateDimIds = new ArrayList<>();
        for (Object o : orders) {
            dateDimIds.add(((Order)o).getDate().getDate_sk());
        }
        long datesTime = System.currentTimeMillis();

        System.out.println("Orders time(s): " + ((ordersTime - startTime)*1.0/1000) +  "Dates time(s): " + ((datesTime-ordersTime)*1.0/1000));
        return dateDimIds;
    }

    /** Lazy fetch
     * @param startTime*/
    public List<Integer> genReportC(long startTime){
        DateDimDao dateDimDaoLocal;
        OrderDao orderDaoLocal;
        SessionFactory sessionFactory;
        Session session;
        Query query1;
        Query query2;
        List<Integer> dateDimIds = new ArrayList<>();

        dateDimDaoLocal = this.dateDimDao;
        sessionFactory = dateDimDaoLocal.getSessionFactory();
        session = sessionFactory.getCurrentSession();
        query1 = session.createQuery("from DateDim as d");
        query1.list();//caching
        long datesTime = System.currentTimeMillis();

        orderDaoLocal = this.orderDao;
        sessionFactory = orderDaoLocal.getSessionFactory();
        session = sessionFactory.getCurrentSession();
        query2 = session.createQuery("from Order as o");
        List orders = query2.list();
        long ordersTime = System.currentTimeMillis();

        for (Object o : orders) {
            dateDimIds.add(((Order)o).getDate().getDate_sk());
        }

        System.out.println("Orders time(s): " + ((ordersTime - datesTime)*1.0/1000) +  "Dates time(s): " + ((datesTime-startTime)*1.0/1000));
        return dateDimIds;
    }



    /** Join implementation */
    public Set<WilosUser> getRoleUser(){
        Set<WilosUser> userSet = new HashSet<>();
        List<WilosUser> wilosUsers = wilosUserDao.getAllWilosUsers();
        List<Role> roles = roleDao.getRole();
        for (WilosUser user : wilosUsers) {
            for (Role role : roles) {
                if(user.getId() == role.getRole_id()){
                    userSet.add(user);
                }
            }
        }
        return userSet;
    }

    /**
     * Aggregation without group by.
     */
    public int getNumUnfinishedProjects(){
        int count = 0;
        List<Project> projects = this.projectDao.getAllProjects();

        for (Project project : projects) {
            if (!(project.getIsFinished())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Mostly similar to above function. But ...
     *
     * In this case, one aggregated variable (x) is dependent
     * on another aggregated variable (count). This causes
     * a cycle of dependencies, and as a result, the function
     * cannot be translated to SQL.
     */
    public int getCumulativeCountSum(){
        int count = 0;
        int x = 0;
        List<Project> projects = this.projectDao.getAllProjects();

        for (Project project : projects) {
            if (!(project.getIsFinished())) {
                count++;
                x += count;
            }
        }
        return x;
    }

    /**
     * Aggregation with group by
     * @return
     */
    public Map getPerUserRoleCount(){
        Map<String, Integer> perUserRoleCount = new HashMap<>();
        List<WilosUser> wilosUsers = wilosUserDao.getAllWilosUsers();
        List<Role> roles = roleDao.getRole();
        int count;
        for (WilosUser user : wilosUsers) {
            count = 0;
            for (Role role : roles) {
                if(user.getId() == role.getRole_id()){
                    count++;
                }
            }
            perUserRoleCount.put(user.getId(), count);
        }
        return perUserRoleCount;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void setWilosUserDao(WilosUserDao wilosUserDao) {
        this.wilosUserDao = wilosUserDao;
    }

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void setDateDimDao(DateDimDao dateDimDao) {
        this.dateDimDao = dateDimDao;
    }
}
