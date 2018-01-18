package idv.mint.type;

public enum CrawlType {
    
    ONLINE,
    
    FILE;
    
    public boolean isOnLine() {
	return equals(ONLINE);
    }

    public boolean isFile() {
	return equals(FILE);
    }
}
