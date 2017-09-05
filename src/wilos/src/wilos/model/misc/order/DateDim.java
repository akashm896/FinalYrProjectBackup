package wilos.model.misc.order;

import java.util.Date;

/**
 * Created by venkatesh on 10/7/17.
 */
public class DateDim {
    private int id;
    private int date_sk;
    private Date date;
    private String filler;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDate_sk() {
        return date_sk;
    }

    public void setDate_sk(int date_sk) {
        this.date_sk = date_sk;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }
}
