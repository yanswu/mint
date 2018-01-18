package idv.mint.entity.enums;

import java.util.Arrays;

public enum OverseasType {

    LOCAL(0),

    KY(1),

    UNKNOWN(-9);

    private Integer value;

    OverseasType(Integer val) {
	this.value = val;
    }

    public Integer getValue() {
	return value;
    }

    public void setValue(Integer value) {
	this.value = value;
    }

    public static OverseasType find(Integer val) {

	if (val == null) {
	    return UNKNOWN;
	}
	return Arrays.stream(values()).filter(e -> e.getValue().equals(val)).findFirst().orElse(UNKNOWN);

    }

    public boolean isKY() {
	return equals(KY);
    }

    public boolean isLocal() {
	return equals(LOCAL);
    }
}
