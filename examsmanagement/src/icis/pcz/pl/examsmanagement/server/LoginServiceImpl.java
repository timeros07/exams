package icis.pcz.pl.examsmanagement.server;

import icis.pcz.pl.examsmanagement.client.LoginService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	private static final long serialVersionUID = 1L;
	
	private static UserService userService = UserServiceFactory.getUserService();
	private static final String HOST_URL = "/Examsmanagement.html";

	@Override
	public String getLoginUrl() {
		return userService.createLoginURL(HOST_URL);
	}

	@Override
	public String getLogoutUrl() {
		return userService.createLogoutURL(HOST_URL);
	}

	@Override
	public String getUserName() {
		User user = userService.getCurrentUser();
		if(user == null)
		{
			return "";
		}
		else
			return user.getNickname();
	}

}
