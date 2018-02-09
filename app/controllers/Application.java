package controllers;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import models.User;
import models.Person;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	public static class Registration {
        @Email
        public String email;
        @Required
        public String password;
    }
    
    public static Result register() {
        Form<Registration> userForm = Form.form(Registration.class);
        return ok(views.html.register.render(userForm));
    }
    

    public static Result postRegister() {
        Form<Registration> userForm = 
                Form.form(Registration.class).bindFromRequest();
        User user = new User(userForm.get().email, userForm.get().password);
        user.save(); 
        return ok("registered"); 
    }
    
    public static class Login {
        @Email
        public String email;
        @Required
        public String password;
        
        public String validate() {
            if (User.authenticate(email, password) == null) {
                return "Invalid user or password";
            } 
            return null;
        }
    }
    
    public static Result login() {
        Form<Login> userForm = Form.form(Login.class);
        return ok(views.html.login.render(userForm));
    }
    
    public static Result postLogin() {
        Form<Login> userForm = Form.form(Login.class).bindFromRequest();
        if (userForm.hasErrors()) {
            return badRequest(views.html.login.render(userForm));
        } else {
        	session().clear();
        	session("email", userForm.get().email);
        	return redirect("/");
        }
    }
    
    public static Result index() {
    	String email = session("email");
    	if(email != null){
    		return ok(email);
    	} else {
    		return ok("not login");
    	}
//        return ok("Hello World!").as("text/html");
//        return badRequest("bad request");
//        return unauthorized("You are forbidden");
//    	return redirect("/new");
//    	return status(200, "good");
//    	List<String> lines = new ArrayList<>();
//    	lines.add("a");
//    	lines.add("b");
//    	lines.add("c");
//    	return ok(views.html.index.render("Play", "Hello World! Parameters passed. ", lines));
    }
    
    public static Result form() {
    	Form<util.User> userForm = Form.form(util.User.class);
    	return ok(views.html.form.render(userForm));
    }
    
    public static Result postForm() {
    	Form<util.User> userForm = Form.form(util.User.class);
    	util.User user = userForm.bindFromRequest().get();
    	return ok(user.email + ""+ user.password);
    }
    public static Result addPerson() {
        Person p1 = new Person();
        Person p2 = new Person();
        p1.name = "vamei2";
        p2.name = "play2";
        p1.save();
        p2.save();
        return ok("Saved");
    }


	public static Result allPerson() {
	    List<Person> persons = Person.findAll();
	    return ok(views.html.personList.render(persons));
	}
	public static Result bcrypt() {
	    String passwordHash = BCrypt.hashpw("Hello",BCrypt.gensalt());
	    boolean correct = BCrypt.checkpw("Hello", passwordHash);
	    boolean wrong = BCrypt.checkpw("World", passwordHash);
	    return ok(passwordHash + " " + correct + " " + wrong);
	}
	
	public static Result loginout() {
    	String email = session("email");
    	if(email != null){
    		session().clear();
    		return ok("login out");
    	} else {
    		return ok("you do not login");
    	}
	}
}
