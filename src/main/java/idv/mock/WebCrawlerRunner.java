package idv.mock;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import idv.mint.util.JsoupUtils;

public class WebCrawlerRunner {
    
    public static void main(String[] args) throws Exception {
	
	String urlTemplate = "https://goodinfo.tw/StockInfo/StockDividendPolicy.asp?STOCK_ID=%s";

//	String stockCode = "8349";
//	String stockCode = "1773";
	String stockCode = "3008";
	
	String url = String.format(urlTemplate, stockCode);
	
	    Document document = JsoupUtils.getDocument(url);

	    Elements elements = document.select("div#divDetail > table > tbody tr");
	    
	    System.err.println(elements.toString());
	    for (Element el : elements) {
		Elements tdList = el.select("td");
		String cash = tdList.get(3).text();
		String shares = tdList.get(6).text();
		String rocYear = tdList.get(14).text();
		String hightPrice = tdList.get(15).text();
		String lowPrice = tdList.get(16).text();
		
		String template = "year[%s],hiP[%s],loP[%s],cashDividend[%s],stockDividend[%s]";
		String text = String.format(template, rocYear,hightPrice,lowPrice,cash,shares);
		System.err.println(text);
		
	    }
//	    System.err.println(elements.toString());
	    
    }
}
