package mytest;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedList;
import java.util.List;

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
        List<Integer> list1 = new LinkedList<>();
        List<Integer> list2 = new LinkedList<>();
        for(Integer i : list1) {
            list2.add(i);
            break;
        }
        return true;
    }

    public Boolean foo2(Integer x, Integer y) {
        Integer z1 = x + y;
        List<Integer> list1 = new LinkedList<>();
        List<Integer> list2 = new LinkedList<>();

        for(Integer i : list1) {
            if(z1 == 0) {
                break;
            }
            list2.add(i);
        }
        System.out.println(list2.get(0));
        return true;
    }

    public Owner getBySomeColumn() {
        return new Owner();
    }

    public void save(Owner owner) {

    }

}
