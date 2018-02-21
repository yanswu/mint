package idv.mint.batch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import idv.mint.batch.type.BatchStatusType;
import idv.mint.context.config.AppConfig;
import idv.mint.context.config.JpaConfig;

public abstract class AbstractInitialBatch {
    
    protected static final Logger logger = LogManager.getLogger(AbstractInitialBatch.class);

    private AnnotationConfigApplicationContext context = null;
    
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime taskStartTime;
    
    private LocalDateTime taskEndTime;
    
    private BatchStatusType statusType;
    
    private void initializedSpringConfig() {
	
	context = new AnnotationConfigApplicationContext();
	context.register(AppConfig.class, JpaConfig.class);
	context.refresh();
    }

    protected <T> T getSpringBean(Class<T> clazz) {
	
	return context.getBean(clazz);
    }

    public abstract BatchStatusType process() throws Exception;

    
    public void execute() {
	
	initializedSpringConfig();
	
	taskStartTime = LocalDateTime.now();
	
	try {
	    statusType = process();
	} catch (Exception e) {
	    logger.error(e,e);
	    statusType = BatchStatusType.FAILED;
	}
	
	taskEndTime = LocalDateTime.now();
	
	logger.info("Task startTime ["+this.getStartTime()+"], endTime ["+this.getEndTime()+"]");
	logger.info("it spends "+this.getBatchProcessSeconds()+" seconds");
	logger.info("the Task BatchStatus is "+ getBatchStatusType());
	
	context.close();
    }

    protected String getStartTime() {
	
	return taskStartTime != null ? taskStartTime.format(formatter) : StringUtils.EMPTY;
    }

    protected String getEndTime() {
	
	return taskEndTime != null ? taskEndTime.format(formatter) : StringUtils.EMPTY;
    }

    protected long getBatchProcessSeconds() {
	
	long seconds = 0;
	
	if(taskStartTime != null && taskEndTime != null) {
	    Duration duration = Duration.between(taskStartTime, taskEndTime);
	    seconds = duration.getSeconds();
	}
	
	return seconds;
    }

    public BatchStatusType getBatchStatusType() {
	
        return statusType;
    }


    
    
}
