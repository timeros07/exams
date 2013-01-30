package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService {

	public String getLoginUrl();
	public String getLogoutUrl();
	public String getUserName();
	
	
}
