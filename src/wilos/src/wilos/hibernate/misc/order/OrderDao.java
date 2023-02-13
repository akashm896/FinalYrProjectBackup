package wilos.hibernate.misc.order;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wilos.model.misc.order.DateDim;
import wilos.model.misc.order.Order;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by venkatesh on 10/7/17.
 */
public class OrderDao extends HibernateDaoSupport {
    public List<Order> getOrder(){
        List order = this.getHibernateTemplate().loadAll(Order.class);
        return order;
    }

    public Set<DateDim> genReportA(){
        List<Order> orders = getOrder();
        Set<DateDim> dateDims = new HashSet<>();

        for (Order order : orders) {
            DateDim date = order.getDate();
            dateDims.add(date);
        }
        return dateDims;
    }
}
