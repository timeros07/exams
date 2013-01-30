package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void getLoginUrl(AsyncCallback<String> callback);

	void getLogoutUrl(AsyncCallback<String> callback);

	void getUserName(AsyncCallback<String> callback);

}
