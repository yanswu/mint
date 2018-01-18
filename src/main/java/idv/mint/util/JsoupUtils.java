package idv.mint.util;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupUtils {

    private static final Logger logger = LogManager.getLogger(JsoupUtils.class);

    public static Document getDocument(String url) throws IOException {

	logger.debug("crawl url [" + url + "]");
	
	Connection connection = Jsoup.connect(url).userAgent(getUserAgentContent());

	Connection.Response resp = connection.execute();

	int statusCode = resp.statusCode();

	logger.debug("statusCode [" + statusCode + "]");
	
	if (statusCode == 200) {
	    Document document = connection.get();
//	    logger.debug("content [" + document.toString() + "]");	    
	    return document;
	}

	return null;
    }

    private static String getUserAgentContent() {

	StringBuilder userAgent = new StringBuilder();
	userAgent.append("Mozilla/5.0 (X11; Linux x86_64) ");
	userAgent.append("AppleWebKit/535.21 (KHTML, like Gecko) ");
	userAgent.append("Chrome/19.0.1042.0 ");
	userAgent.append("Safari/535.21  ");
	return userAgent.toString();
    }
}
