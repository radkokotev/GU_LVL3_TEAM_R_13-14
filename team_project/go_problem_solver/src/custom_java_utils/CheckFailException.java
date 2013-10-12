package custom_java_utils;

/**
 * An exception to represent CHECK failure.
 */
public class CheckFailException extends Exception {
	private static final long serialVersionUID = 188994544267270755L;
	
	public CheckFailException(String msg) {
		super(msg);
	}

}
