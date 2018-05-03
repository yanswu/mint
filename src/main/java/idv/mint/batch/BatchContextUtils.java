package idv.mint.batch;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import idv.mint.context.config.AppConfig;
import idv.mint.context.config.JpaConfig;
import idv.mint.entity.enums.StockMarketType;

public class BatchContextUtils {

    public static AnnotationConfigApplicationContext createSpringApplicationContext() {

	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
	context.register(AppConfig.class, JpaConfig.class);
	context.refresh();
	return context;
    }

    public static Context<Context.Constants, Object> createContext() {

	Context<Context.Constants, Object> context = new Context<>();
	return context;
    }

    public static Context<Context.Constants, Object> createContextWithSpringConfig() {

	Context<Context.Constants, Object> context = new Context<>();
	context.put(Context.Constants.SPRING_APP_CONTEXT, createSpringApplicationContext());
	return context;
    }

    public static List<StockMarketType> getStockMarketType(Context<Context.Constants, Object> context) {

	Object marketTypeObj = context.get(Context.Constants.MARKET_TYPE);

	if (marketTypeObj == null) {
	    return Arrays.asList(StockMarketType.TSE,StockMarketType.OTC);
	}
	
	StockMarketType marketType = (StockMarketType) marketTypeObj;
	
	return Arrays.asList(marketType);
    }

    public static Optional<AnnotationConfigApplicationContext> getSpringAppContext(Context<Context.Constants, Object> context) {

	Object springContextObj = context.get(Context.Constants.SPRING_APP_CONTEXT);

	if (springContextObj == null) {
	    return Optional.empty();
	}

	AnnotationConfigApplicationContext springContext = (AnnotationConfigApplicationContext) springContextObj;

	return Optional.of(springContext);
    }

    public static Optional<String> getStockCode(Context<Context.Constants, Object> context) {

	Object stockCodeObj = context.get(Context.Constants.STOCK_CODE);

	if (stockCodeObj == null) {
	    return Optional.empty();
	}

	String stockCode = (String) stockCodeObj;

	return Optional.of(stockCode);
    }
}
