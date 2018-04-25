
package me.d2o.web.stomp;

/**
 * Class: StompException
 *
 * @author bo.hanssen
 * @since Jan 4, 2017 5:02:22 PM
 *
 */
public class StompException extends Exception {

	private static final long serialVersionUID = 1L;

	
	public StompException() {
		super();
	}

	
	public StompException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	
	public StompException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	
	public StompException(String arg0) {
		super(arg0);
	}

	
	public StompException(Throwable arg0) {
		super(arg0);
	}

}
