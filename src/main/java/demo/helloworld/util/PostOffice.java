package demo.helloworld.util;

import act.event.On;
import act.mail.Mailer;
import act.util.Async;
import demo.helloworld.model.Customer;

@Mailer
public class PostOffice extends Mailer.Util {

    // Make the sendWelcomeLetter method an asyc event handler
    @On(Customer.EVENT_CREATED)
    @Async
    public void sendWelcomeLetter(Customer customer) {
        to(customer.getEmail());
        subject("welcome to ActFramework demo app");
        send(customer);
    }
}
