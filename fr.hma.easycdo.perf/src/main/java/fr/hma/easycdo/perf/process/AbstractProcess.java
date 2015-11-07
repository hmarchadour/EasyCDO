package fr.hma.easycdo.perf.process;

import java.util.concurrent.Callable;

import org.eclipse.emf.cdo.session.CDOSession;

public abstract class AbstractProcess implements Callable<Result> {

	private long startTime;

	private long stopTime;

	private CDOSession session;

	private String resourceName;

	public AbstractProcess(CDOSession session, String resourceName) {
		startTime = System.currentTimeMillis();
		this.session = session;
		this.resourceName = resourceName;
	}

	@Override
	public Result call() throws Exception {
		Result result = null;
		try {
			process(session, resourceName);
			result = Result.SUCCESS;
		} catch (Throwable t) {
			result = new Result(t);
		} finally {
			stopTime = System.currentTimeMillis();
		}
		return result;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	protected abstract void process(CDOSession session, String resourceName) throws Throwable;
}
