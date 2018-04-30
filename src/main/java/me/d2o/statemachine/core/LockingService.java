/**
 *
 */
package me.d2o.statemachine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Class: LockingService
 *
 * @author bo.hanssen
 * @since Mar 3, 2017 1:59:16 PM
 *
 */
@Service
public class LockingService {

	private static final Logger logger = LoggerFactory.getLogger(LockingService.class);

	private List<String> locks;
	private Map<Long, String> lockMap;
	private Map<String, List<Long>> queue;

	@PostConstruct
	void init() {
		locks = Collections.synchronizedList(new ArrayList<>());
		queue = Collections.synchronizedMap(new HashMap<>());
		lockMap = Collections.synchronizedMap(new HashMap<>());
		logger.debug("Initialized StateMachine lockservice");
	}

	void aquire(TransitEvent event) {
		String id = event.getMachineId();
		long threadId = this.getThreadId();
		logger.info("Try to aquire lock for game [{}]", id);
		while (!checkLock(threadId, id)) {
			this.sleep();
		}
		locks.add(id);
		lockMap.put(threadId, id);
		logger.info("Aquired lock for game [{}]", id);
	}

	private boolean checkLock(long threadId, String id) {
		boolean goAhead = false;
		if (!queue.containsKey(id)) {
			queue.put(id, Collections.synchronizedList(new ArrayList<>()));
		}

		if (locks.contains(id) && !queue.get(id).contains(threadId)) {
			queue.get(id).add(threadId);
		} else if (!locks.contains(id) && !queue.get(id).isEmpty() && queue.get(id).get(0).equals(threadId)) {
			queue.get(id).remove(0);
			goAhead = true;
		} else if (!locks.contains(id) && queue.get(id).isEmpty()) {
			goAhead = true;
		}
		return goAhead;
	}

	void sleep() {
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.error("Thread interrupted", e);
		}
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
	void afterCommit(ApplicationEvent event) {
		long threadId = this.getThreadId();
		if (lockMap.get(threadId) != null) {
			locks.remove(lockMap.get(threadId));
			logger.info("Remove lock for thread [{}] game [{}]", threadId, lockMap.get(threadId));
			lockMap.remove(threadId);
		}
	}

	private long getThreadId() {
		return Thread.currentThread().getId();
	}

}
