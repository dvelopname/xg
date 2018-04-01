package br.com.c4ll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HttpUtils {
	
	public HttpEntity<byte[]> createHttpEntity(String filePath, String fileName) throws IOException {
		byte[] arquivo = Files.readAllBytes( Paths.get(filePath) );
        
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        HttpEntity<byte[]> entity = new HttpEntity<byte[]>(arquivo, httpHeaders);
        
        return entity;
	}
}
