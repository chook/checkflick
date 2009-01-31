package controller.thread;

/**
 * A helper class for the thread pool (contains workers)
 * @author Chook
 *
 */
class ThreadPoolData {
	protected ObjectFIFO idleWorkers;
	protected ThreadPoolWorker[] workerList;

	protected ThreadPoolData() {
	}
}