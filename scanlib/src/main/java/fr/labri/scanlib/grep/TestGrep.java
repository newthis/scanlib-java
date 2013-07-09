package fr.labri.scanlib.grep;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestGrep {
	
	public static void main(String[] args) throws IOException {
		
		Pattern p = Pattern.compile("java\\.util\\.List");
		int i = 0;
		
		javaxt.io.Directory directory = new javaxt.io.Directory("/tmp/cassandra");
		javaxt.io.File[] files = directory.getFiles("*.java", true);
		for(javaxt.io.File file : files){
			if(!file.isHidden()) {
				for(String line : Files.readAllLines(Paths.get(file.getPath()+file.getName()),Charset.forName("UTF-8"))) {
					  Matcher matcher = p.matcher(line);
					  if(matcher.find()) {
						  i++;
					  }
				}
			}		
		}
		System.out.println(i+" ocurrences found");
	}


	
}
