package idv.mint.batch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TaskHandler {

    protected static final Logger logger = LogManager.getLogger(TaskHandler.class);

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime taskStartTime;

    private LocalDateTime taskEndTime;

    protected TaskHandler next;

    public TaskHandler() {
    }

    public TaskHandler(TaskHandler next) {
	this.next = next;
    }

    public void setNextHandler(TaskHandler next) {
	this.next = next;
    }

    public abstract boolean execute(Map<String, Object> params) throws Exception;

    public void executeTask(Map<String, Object> params) {

	try {

	    taskStartTime = LocalDateTime.now();

	    boolean success = execute(params);

	    taskEndTime = LocalDateTime.now();

	    taskProcessTimes(taskStartTime, taskEndTime);

	    if (success) {
		doNextHandler(params);
	    } else {
	    }
	} catch (Exception e) {

	}
    }

    protected void doNextHandler(Map<String, Object> params) {

	if (next != null) {
	    next.executeTask(params);
	}
    }

    private static void taskProcessTimes(LocalDateTime startTime, LocalDateTime endTime) {

	if (startTime != null && endTime != null) {

	    long seconds = 0;
	    Duration duration = Duration.between(startTime, endTime);
	    seconds = duration.getSeconds();

	    logger.info("Task startTime [" + startTime.format(formatter) + "], endTime [" + endTime.format(formatter) + "]");
	    logger.info("it spends " + seconds + " seconds");
	}
    }

}
