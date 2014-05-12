package com.hystrix.utils.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Files {
	
	public static String[] readLines(String filename) {
		
        FileReader fileReader;
        BufferedReader bufferedReader;
        List<String> lines = new ArrayList<String>();
        String line = null;
        try{
        	fileReader = new FileReader(filename);
        	bufferedReader = new BufferedReader(fileReader);
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
		}catch (IOException e){}
		
        return lines.toArray(new String[lines.size()]);
    }

}
