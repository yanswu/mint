/**
 * 
 */
package idv.mint.context.enums;

import java.util.Arrays;

/**
 * @author WuJerry
 *
 */
public enum EncodingType implements I18nEnum {

    UTF8("UTF-8"),

    MS950("MS950"),

    BIG5("BIG5"),

    ISO8859_1("ISO8859_1"),

    UNKNOWN("UNKNOWN");

    private String value;

    EncodingType(String encoding) {
	this.value = encoding;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public static EncodingType find(String val) {
	
	return Arrays.stream(values()).filter(e -> e.getValue().equals(val)).findFirst().orElse(UNKNOWN);
    }

    public boolean isUTF8() {
	return getValue().equals(UTF8);
    }

    public boolean isBig5() {
	return getValue().equals(BIG5);
    }

    public boolean isMS950() {
	return getValue().equals(MS950);
    }
}
