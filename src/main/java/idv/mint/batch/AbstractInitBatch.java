package idv.mint.batch;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import idv.mint.batch.type.BatchStatusType;
import idv.mint.context.config.AppConfig;
import idv.mint.context.config.JpaConfig;

public abstract class AbstractInitBatch {
    
    protected static final Logger logger = LogManager.getLogger(AbstractInitBatch.class);

    private static AnnotationConfigApplicationContext context = null;

    private LocalDateTime taskStartTime;
    
    private LocalDateTime taskEndTime;
    
    private BatchStatusType statusType;
    
    static {
	context = new AnnotationConfigApplicationContext();
	context.register(AppConfig.class, JpaConfig.class);
	context.refresh();
    }

    protected <T> T getSpringContext(Class<T> clazz) {
	return context.getBean(clazz);
    }

    public abstract BatchStatusType process() throws Exception;

    
    public void execute() {

	taskStartTime = LocalDateTime.now();
	try {
	    statusType = process();
	} catch (Exception e) {
	    logger.error(e,e);
	    statusType = BatchStatusType.FAILED;
	}
	taskEndTime = LocalDateTime.now();
	context.close();
    }

    protected String getStartTime() {
	return null;
    }

    protected String getEndTime() {
	return null;
    }

    protected long getBatchProcessSeconds() {

	Duration duration = Duration.between(taskStartTime, taskEndTime);
	long seconds = duration.getSeconds();
	return seconds;
    }

    public BatchStatusType getBatchStatusType() {
        return statusType;
    }


    
    
}
