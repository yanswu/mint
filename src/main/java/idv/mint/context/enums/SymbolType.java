package idv.mint.context.enums;

public enum SymbolType {

    COMMA(","),

    DASH("-");

    private String value;

    private SymbolType(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
