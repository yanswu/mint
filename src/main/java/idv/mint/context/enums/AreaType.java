package idv.mint.context.enums;

import java.util.Arrays;

public enum AreaType {

    TW("TW"), 
    
    UNKNOWN("UNKNOWN");

    private String value;

    AreaType(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public boolean isTW() {
	return equals(TW);
    }

    public static AreaType find(String val) {
	return Arrays.stream(values()).filter(e -> e.getValue().equals(val)).findFirst().orElse(UNKNOWN);

    }
}
