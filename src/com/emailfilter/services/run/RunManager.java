package com.emailfilter.services.run;

import java.util.concurrent.Executors;

public class RunManager {

	private final IFilterRun run;

	public RunManager(IFilterRun run) {
		this.run = run;
	}

	public void execute() {
		Executors.newSingleThreadExecutor().submit(run);
	}
}
