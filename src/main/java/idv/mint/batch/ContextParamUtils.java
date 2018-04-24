package idv.mint.batch;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import idv.mint.context.config.AppConfig;
import idv.mint.context.config.JpaConfig;
import idv.mint.entity.enums.StockMarketType;

public class ContextParamUtils {

    public static AnnotationConfigApplicationContext createSpringApplicationContext() {

	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
	context.register(AppConfig.class, JpaConfig.class);
	context.refresh();
	return context;
    }

    public static Context<BatchSettings, Object> createContext() {

	Context<BatchSettings, Object> context = new Context<>();
	return context;
    }

    public static Context<BatchSettings, Object> createContextWithSpringConfig() {

	Context<BatchSettings, Object> context = new Context<>();
	context.put(BatchSettings.SPRING_APP_CONTEXT, createSpringApplicationContext());
	return context;
    }

    public static Optional<List<StockMarketType>> getStockMarketType(Context<BatchSettings, Object> context) {

	Object marketTypeObj = context.get(BatchSettings.MARKET_TYPE);

	if (marketTypeObj == null) {
	    return Optional.of(Arrays.asList(StockMarketType.TSE,StockMarketType.OTC));
	}
	
	StockMarketType marketType = (StockMarketType) marketTypeObj;
	
	return Optional.of(Arrays.asList(marketType));
    }

    public static Optional<AnnotationConfigApplicationContext> getSpringAppContext(Context<BatchSettings, Object> context) {

	Object springContextObj = context.get(BatchSettings.SPRING_APP_CONTEXT);

	if (springContextObj == null) {
	    return Optional.empty();
	}

	AnnotationConfigApplicationContext springContext = (AnnotationConfigApplicationContext) springContextObj;

	return Optional.of(springContext);
    }

    public static Optional<String> getStockCode(Context<BatchSettings, Object> context) {

	Object stockCodeObj = context.get(BatchSettings.STOCK_CODE);

	if (stockCodeObj == null) {
	    return Optional.empty();
	}

	String stockCode = (String) stockCodeObj;

	return Optional.of(stockCode);
    }
}
