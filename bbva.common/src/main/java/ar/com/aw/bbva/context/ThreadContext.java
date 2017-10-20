package ar.com.aw.bbva.context;

public class ThreadContext {
	public static final ThreadLocal<UserContext> userThread = new ThreadLocal<UserContext>();

	public static void set(UserContext context) {
		userThread.set(context);
	}

	public static void unset() {
		userThread.remove();
	}

	public static UserContext get() {
		return userThread.get();
	}
}
