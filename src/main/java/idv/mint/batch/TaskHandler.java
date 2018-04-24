package idv.mint.batch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

    public abstract boolean execute(Context<BatchSettings, Object> context) throws Exception;

    public void executeTask(Context<BatchSettings, Object> context) {

	try {

	    taskStartTime = LocalDateTime.now();

	    boolean success = execute(context);

	    taskEndTime = LocalDateTime.now();

	    taskExecuteConsoleLog(taskStartTime, taskEndTime);

	    if (success) {
		doNextHandler(context);
	    } else {
	    }
	} catch (Exception e) {

	} finally {

	    Optional<AnnotationConfigApplicationContext> springContextOptional = ContextParamUtils.getSpringAppContext(context);
	    if (springContextOptional.isPresent()) {
		AnnotationConfigApplicationContext springContext = springContextOptional.get();
		springContext.close();
	    }
	}
    }

    protected void doNextHandler(Context<BatchSettings, Object> context) {

	if (next != null) {
	    next.executeTask(context);
	}
    }

    protected <T> T getSpringBean(Context<BatchSettings, Object> context, Class<T> clazz) {

	return ContextParamUtils.getSpringAppContext(context).get().getBean(clazz);
    }

    private static void taskExecuteConsoleLog(LocalDateTime startTime, LocalDateTime endTime) {

	if (startTime != null && endTime != null) {

	    long seconds = 0;
	    Duration duration = Duration.between(startTime, endTime);
	    seconds = duration.getSeconds();

	    System.out.printf("Task startTime [%s], endTime [%s] \n ", startTime.format(formatter), endTime.format(formatter));
	    System.out.printf("it spends %d seconds ", seconds);
	}
    }

}
