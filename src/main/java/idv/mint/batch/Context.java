package idv.mint.batch;

import java.util.HashMap;

public class Context<K, V> extends HashMap<K, V> {

    public enum Constants {
	MARKET_TYPE, STOCK_CODE, SPRING_APP_CONTEXT;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Context() {
    };

}
