package demo.helloworld.controller;


import act.cli.Command;
import act.cli.Required;
import act.controller.Controller;
import act.db.morphia.MorphiaDao;
import act.event.EventBus;
import act.util.PropertySpec;
import demo.helloworld.DemoApp;
import demo.helloworld.model.Customer;
import org.bson.types.ObjectId;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.annotation.With;

import javax.inject.Inject;

@With(DemoApp.class) // apply the interceptors defined in DemoApp class
@Controller("/customer") // specify the common URL path context "/customer"
public class CustomerController {

    @Inject
    private MorphiaDao<Customer> customerDao;

    @Inject
    private EventBus eventBus;

    @Command(name = "cust.list", help = "List all customers")
    @GetAction
    @PropertySpec("-address.suburb")
    public Iterable<Customer> list() {
        return customerDao.findAll();
    }

    @Command(name = "cust.show", help = "display customer details")
    @GetAction("/{id}")
    public Customer show(
           @Required("specify the customer ID") String id
    ) {
        return customerDao.findById(id);
    }

    @PostAction
    public String createCustomer(Customer customer) {
        customerDao.save(customer);
        eventBus.trigger(Customer.EVENT_CREATED, customer);
        return customer.getIdAsStr();
    }
}
