package mytest;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class Owner {
    private String firstName;
    private String lastName;

    public Integer x;
    public Integer y;

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean foo() {
        Owner o = new Owner();
        int z = x + y;
        int z2 = z + y;
        int z3 = z2 + x;
        o.x = z;
        int z4 = o.x;
        return true;
    }

    public Boolean bar() {
        Owner owner = getBySomeColumn();
        owner.x = 10;
        save(owner);
        return true;
    }

    public Boolean foo2(Integer x, Integer y) {
        Integer z1 = x + y;
        Integer z2 = z1 + 10;
        Integer z3 = z2 + 5;
        return true;
    }

    public Owner getBySomeColumn() {
        return new Owner();
    }

    public void save(Owner owner) {

    }

}
