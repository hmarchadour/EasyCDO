package fr.hma.easycdo.perf.process;

public class Result {

	public static final Result SUCCESS = new Result();

	private boolean success;
	private Throwable t;

	private Result() {
		this.success = true;
		this.t = null;
	}

	public Result(Throwable t) {
		this.success = false;
		this.t = t;
	}

	public boolean isSuccess() {
		return success;
	}

	public Throwable getThrowable() {
		return t;
	}

}
