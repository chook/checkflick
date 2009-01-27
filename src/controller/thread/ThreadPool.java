package controller.thread;

public class ThreadPool extends Object {
	private ThreadPoolData data = new ThreadPoolData();

	public ThreadPool(int numberOfThreads) {
		// make sure that it's at least one
		numberOfThreads = Math.max(1, numberOfThreads);

		data.idleWorkers = new ObjectFIFO(numberOfThreads);
		data.workerList = new ThreadPoolWorker[numberOfThreads];

		for (int i = 0; i < data.workerList.length; i++) {
			data.workerList[i] = new ThreadPoolWorker(data.idleWorkers);
		}
	}

	public void execute(Runnable target) throws InterruptedException {
		// block (forever) until a worker is available
		ThreadPoolWorker worker = (ThreadPoolWorker) data.idleWorkers.remove();
		worker.process(target);
	}

	public void stopRequestIdleWorkers() {
		try {
			Object[] idle = data.idleWorkers.removeAll();
			for (int i = 0; i < idle.length; i++) {
				((ThreadPoolWorker) idle[i]).stopRequest();
			}
		} catch (InterruptedException x) {
			Thread.currentThread().interrupt(); // re-assert
		}
	}

	public void stopRequestAllWorkers() {
		stopRequestIdleWorkers();

		try {
			Thread.sleep(250);
		} catch (InterruptedException x) {
		}

		for (int i = 0; i < data.workerList.length; i++) {
			if (data.workerList[i].isAlive()) {
				data.workerList[i].stopRequest();
			}
		}
	}
}

