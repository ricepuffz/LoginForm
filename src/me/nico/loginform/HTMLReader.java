package me.nico.loginform;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTMLReader
{
	public static final String[] readHtml(String path)
	{
		String fileToString = "";
		
		try {
			Object[] lines = Files.lines(Paths.get(path), StandardCharsets.UTF_8).toArray();
			for (Object line : lines)
				fileToString += String.valueOf(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileToString.split("¬");
	}
}
