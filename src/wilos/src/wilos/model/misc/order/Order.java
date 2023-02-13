package wilos.model.misc.order;

import java.io.Serializable;

/**
 * Created by venkatesh on 10/7/17.
 */
public class Order implements Serializable{
    private int id;
    private DateDim date;
    private int status;
    private int price;
    private String filler;

    public DateDim getDate() {
        return date;
    }

    public void setDate(DateDim date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
