package util;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;





public class User {
	
	@Email
	public String email;
	@Required
	public String password;
	public String comment;
}