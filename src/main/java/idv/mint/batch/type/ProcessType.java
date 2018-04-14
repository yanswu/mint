package idv.mint.batch.type;

import java.util.Arrays;

public enum ProcessType {
    
    FAILED("F"),
    
    SUCCESS("S"),
    
    PARTIAL("P"),
    
    UNKNOWN("U");
    
    private String value ;
    
    ProcessType(String value) {
	this.value = value;
    }
    
    public String getValue() {
	return value;
    }
    
    public static ProcessType find(String val) {
	
	return Arrays.stream(values()).filter(e -> e.getValue().equals(val)).findFirst().orElse(UNKNOWN);
    }
    
    public boolean isSuccess() {
	return equals(SUCCESS);
    }

    public boolean isFailed() {
	return equals(FAILED);
    }

    public boolean isPartial() {
	return equals(PARTIAL);
    }
    
}
