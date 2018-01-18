package idv.mint.context;

/**
 * 
 * @author WuJerry
 *
 */
public enum AppSettings {

    HomePage("index"),
    
    ;

    private String value;
    

    AppSettings(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
