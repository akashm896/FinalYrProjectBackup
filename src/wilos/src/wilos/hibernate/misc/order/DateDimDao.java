package wilos.hibernate.misc.order;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wilos.model.misc.order.DateDim;

import java.util.List;

/**
 * Created by venkatesh on 7/17/2017.
 */
public class DateDimDao extends HibernateDaoSupport {
    public List<DateDim> getDateDim(){
        List dates = this.getHibernateTemplate().loadAll(DateDim.class);
        return dates;
    }
}
