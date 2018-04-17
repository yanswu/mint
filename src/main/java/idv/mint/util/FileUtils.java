package idv.mint.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtils extends org.apache.commons.io.FileUtils{


    public static void cleanFile(Path path) throws IOException {

	if(Files.exists(path)) {
	    Files.delete(path);	    
	}
	Files.createFile(path);
    }

    public static void writeFile(Path path, String line) throws IOException {

	List<String> lines = new ArrayList<>();
	lines.add(line);
	writeFile(path,lines);
    }

    public static void writeFile(Path path, List<String> lines) throws IOException {
	
	if (!Files.exists(path)) {
	    createFile(path);
	}
	
	Files.write(path, lines, Charset.defaultCharset());
    }
    
    /**
     * 
     * @param path
     * @throws IOException
     */
    private static void createFile(Path path) throws IOException {
	
	if (!Files.exists(path)) {
	    Path dir = path.getParent();
	    // 根目錄不存在  則產生目錄
	    if(!Files.exists(dir)) {
		Files.createDirectories(dir);
	    }
	    Files.createFile(path);
	}
    }
    
    public static void writeFileAppend(Path path,List<String> lines) throws IOException {
	
	if(!Files.exists(path)) {
	    Files.createFile(path);
	}
	Files.write(path, lines, Charset.defaultCharset(),StandardOpenOption.APPEND);
    }
    
    
}
