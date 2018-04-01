package br.com.c4ll.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class TemporaryFileUtils {
	
	public File createFile(String data, String identifier, String format) throws IOException {
		identifier = identifier == null ? "" : identifier.replace("." + format, "");
		
		final DateTime dateTime = new DateTime();
		final long millis = dateTime.now().getMillis();
		final String fileName = "src/main/resources/templates/" + identifier + "." + format;
		
		final File file = new File(fileName);
		final FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(data);
		fileWriter.close();
		
		return file;
	}
}
