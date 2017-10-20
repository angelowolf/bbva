package ar.com.aw.bbva.aop;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "bean:name=PerfInterceptor", description = "Monitor de performance")
public class PerfInterceptor implements MethodInterceptor {

	static Logger									logger					= Logger.getLogger(PerfInterceptor.class);
	private ConcurrentHashMap<String, MethodStats>	methodStats				= new ConcurrentHashMap<String, MethodStats>();
	private long									statLogFrequency		= 10;
	private long									methodWarningThreshold	= 50;
	private long									rowsWarning				= 5;

	@ManagedAttribute
	public long getStatLogFrequency() {
		return statLogFrequency;
	}

	@ManagedAttribute
	public void setStatLogFrequency(long statLogFrequency) {
		this.statLogFrequency = statLogFrequency;
	}

	@ManagedAttribute
	public long getMethodWarningThreshold() {
		return methodWarningThreshold;
	}

	@ManagedAttribute
	public void setRowsWarning(long rowsWarning) {
		this.rowsWarning = rowsWarning;
	}

	@ManagedAttribute
	public long getRowsWarning() {
		return rowsWarning;
	}

	@ManagedAttribute
	public void setMethodWarningThreshold(long methodWarningThreshold) {
		this.methodWarningThreshold = methodWarningThreshold;
	}

	@ManagedOperation
	public String getStatics(String classAndMethodName) {
		MethodStats stats = methodStats.get(classAndMethodName);
		return stats.toString();

	}

	public Object invoke(MethodInvocation method) throws Throwable {

		long start = System.currentTimeMillis();
		int rows = 1;
		try {

			Object obj = method.proceed();

			if (obj instanceof Collection) {
				rows = ((Collection<?>) obj).size();
			}

			return obj;
		} finally {
			updateStats(method.getThis().getClass().getName() + " "
					+ method.getMethod().getName(),
					argumentsToString(method.getArguments())+" ",
					(System.currentTimeMillis() - start), rows);
		}
	}

	private String argumentsToString(Object[] arguments) {
		if(arguments == null) return null;
		StringBuilder argumentsStringBuilder = new StringBuilder();
		for (Object object : arguments) {
			if(argumentsStringBuilder.length() != 0)
				argumentsStringBuilder.append(", ");
			if(object == null) 
				argumentsStringBuilder.append("null");
			else if(object instanceof Object[])
				argumentsStringBuilder
				.append("[")
				.append(argumentsToString((Object[]) object))
				.append("]");
			else 
				argumentsStringBuilder.append(object.toString());
		}
		return argumentsStringBuilder.toString();
	}

	private void updateStats(String classAndMethodName, String params, long elapsedTime, int rows) {
		MethodStats stats = methodStats.get(classAndMethodName);
		if (stats == null) {
			stats = new MethodStats(classAndMethodName);
			methodStats.put(classAndMethodName, stats);
		}
		stats.count++;
		stats.totalTime += elapsedTime;
		long avgTime = stats.totalTime / stats.count;
		if (elapsedTime > stats.maxTime) {
			stats.maxTime = elapsedTime;
		}

		if (elapsedTime > methodWarningThreshold || rows > rowsWarning) {
			logger.warn("method warning: " + classAndMethodName + "("+params+")"
					+ ", cnt = "+ stats.count
					+ ", lastTime = " + elapsedTime
					+ ", maxTime = " + stats.maxTime 
					+ ", avgTime = " + avgTime 
					+ ", rows = " + rows);
		}

		if (stats.count % statLogFrequency == 0) {
			long runningAvg = (stats.totalTime - stats.lastTotalTime) / statLogFrequency;
			logger.debug("method: " + classAndMethodName + "("+params+")"
					+ ", cnt = "+ stats.count 
					+ ", lastTime = " + elapsedTime
					+ ", avgTime = " + avgTime 
					+ ", runningAvg = " + runningAvg
					+ ", maxTime = " + stats.maxTime);

			// reset the last total time
			stats.lastTotalTime = stats.totalTime;
		}
	}

	class MethodStats {

		public String	methodName;
		public Object[] params;
		public long		count;
		public long		totalTime;
		public long		lastTotalTime;
		public long		maxTime;

		public MethodStats(String methodName) {
			this.methodName = methodName;
		}

		public String toString() {
			long avgTime = this.totalTime / this.count;
			return "method: " + this.methodName + "("+params+")"
					+ ", cnt = "+ this.count 
					+ ", avgTime = " + avgTime 
					+ ", maxTime = " + this.maxTime;
		}
	}
}