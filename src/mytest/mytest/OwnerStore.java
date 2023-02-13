package mytest;

import java.util.List;

public class OwnerStore {

    private List<Owner> store;

    public void addOwner(Owner owner) {
        store.add(owner);
    }

    public void addOwner(String firstName, String lastName) {
        Owner owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        store.add(owner);
    }

}
