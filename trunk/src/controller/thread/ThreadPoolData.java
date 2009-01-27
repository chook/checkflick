package controller.thread;

public class ThreadPoolData {
	protected ObjectFIFO idleWorkers;
	protected ThreadPoolWorker[] workerList;

	protected ThreadPoolData() {
	}
}