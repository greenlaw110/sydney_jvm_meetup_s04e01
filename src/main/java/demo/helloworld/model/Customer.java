package demo.helloworld.model;

import act.db.morphia.MorphiaModel;
import org.mongodb.morphia.annotations.Entity;

@Entity("cust")
public class Customer extends MorphiaModel<Customer> {

    public static final String EVENT_CREATED = "customer-created";

    private String name;
    private String email;
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
