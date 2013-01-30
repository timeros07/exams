package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.core.client.JavaScriptObject;

class ResponseObject extends JavaScriptObject
{
	  protected ResponseObject(){}                                              // [2]

	  public final native String getResponse() /*-{ return this.response; }-*/; // [3]
}