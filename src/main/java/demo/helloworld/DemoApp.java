package demo.helloworld;

import act.app.ActionContext;
import act.boot.app.RunApp;
import act.controller.Controller;
import act.job.Every;
import act.job.FixedDelay;
import act.job.InvokeAfter;
import org.osgl.http.H;
import org.osgl.mvc.annotation.Action;
import org.osgl.mvc.annotation.Before;
import org.osgl.mvc.annotation.GetAction;

/**
 * Hello world!
 */
public class DemoApp extends Controller.Util {

    // The before interceptor, applied to all action methods except "loginUser" and "logoutUser"
    @Before(except = "loginUser,logoutUser")
    public void authenticate(ActionContext context) {
        if (!loggedIn(context)) {
            if (context.isAjax()) {
                throw forbidden();
            } else {
                throw redirect("/login");
            }
        }
    }

    @GetAction
    public void home() {
    }

    @GetAction("/hi")
    public void sayHelloTo(String who) {
        render(who);
    }

    /**
     * For GET request, display the login form
     * <p>
     *     For POST request, authenticate the user. We are using very simple authentication
     *     here by checking if the password contains word "act"
     * </p>
     * @param username
     * @param context
     */
    @Action(value = "/login", methods = {H.Method.POST, H.Method.GET})
    public void loginUser(String username, String password, ActionContext context) {
        boolean loggedIn = loggedIn(context);
        if (context.req().method() == H.Method.GET) {
            render(loggedIn);
        } else {
            loggedIn = loggedIn || password.contains("act");
            if (loggedIn) {
                context.session().put("username", username);
            } else {
                context.flash().error("Unknown username/password");
            }
            redirect("/login");
        }
    }

    @Action(value = "/logout", methods = {H.Method.GET, H.Method.POST})
    public void logoutUser(ActionContext context) {
        context.session().clear();
        redirect("/login");
    }

    public static void main(String[] args) throws Exception {
        RunApp.start("demo", DemoApp.class);
    }

    @Every("2s")
    public void jobA() {
        System.out.println("job A...");
    }

    @InvokeAfter("demo.helloworld.DemoApp.jobA")
    public void jobB() {
        System.out.println("job B...");
    }

    @FixedDelay("2s")
    public void jobC() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // ignore
        }
        System.out.println("job C...");
    }

    private boolean loggedIn(ActionContext context) {
        return context.session().contains("username");
    }
}
