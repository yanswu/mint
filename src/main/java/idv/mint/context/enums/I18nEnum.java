package idv.mint.context.enums;

import org.apache.commons.lang3.StringUtils;

public interface I18nEnum{
    
    
    public default String getValue() {
	return StringUtils.EMPTY;
    }
    
    public default String getI18nText() {
	return "";
    };
    
}
