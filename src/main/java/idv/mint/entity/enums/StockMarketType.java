/**
 * 
 */
package idv.mint.entity.enums;

import java.util.Arrays;

/**
 * @author WuJerry
 *
 */
public enum StockMarketType {

    TSE(1),

    OTC(2),

    UNKNOWN(-9);

    private Integer value;

    StockMarketType(Integer value) {
	this.value = value;
    }

    public Integer getValue() {
	return value;
    }

    public void setValue(Integer value) {
	this.value = value;
    }

    public static StockMarketType find(Integer val) {

	if (val == null) {
	    return UNKNOWN;
	}
	return Arrays.stream(values()).filter(e -> e.getValue().intValue() == val.intValue()).findFirst().orElse(UNKNOWN);

    }

    public boolean isTSE() {
	return this.equals(TSE);
    }

    public boolean isOTC() {
	return this.equals(OTC);
    }

    public boolean isUnknown() {
	return this.equals(UNKNOWN);
    }
}
