package com.nosliw.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class HAPFileUtility {
	
    public static Set<File> getAllFiles(String path) {
    	Set<File> out = new HashSet<File>();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return out;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
            	out.addAll(HAPFileUtility.getAllFiles(fs.getAbsolutePath()));
            } else {
            	out.add(fs);
            }
        }
        return out;
    }
	
    public static String readFile(File file){
    	return HAPFileUtility.readFile(file.getAbsolutePath());
    }
    
	public static String readFile(String filePath){
		return readFile(filePath, "\n");
	}
	
	public static String readFile(String filePath, String nextLine){
		StringBuffer out = new StringBuffer();
		try{
			FileReader inputFile = new FileReader(filePath);
		    BufferedReader bufferReader = new BufferedReader(inputFile);
            String line;
            while ((line=bufferReader.readLine())!=null){
            	out.append(line+nextLine);
            }
            bufferReader.close(); 			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out.toString();
	}

	public static String readFile(InputStream stream){
		return readFile(stream, "\n");
	}
	
	public static void writeFile(String fileName, String content){
		try {
			 
			File file = new File(fileName);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				try{
					file.createNewFile();
				}
				catch(Exception e){
					e.printStackTrace();
					int kkkk = 555;
					kkkk++;
				}
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static String getFileName(File file){
		String name = file.getName();
		int i = name.indexOf(".");
		if(i!=-1){
			name = name.substring(0, i);
		}
		return name;
	}
	
	
	public static String readFile(InputStream stream, String nextLine){
		StringBuffer out = new StringBuffer();
		try{
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(stream));			
            String line;
            while ((line=bufferReader.readLine())!=null){
            	out.append(line+nextLine);
            }
            bufferReader.close(); 			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out.toString();
	}
	
	public static InputStream getInputStreamOnClassPath(Class c, String fileName){
		InputStream stream = c.getResourceAsStream(fileName);
		return stream;
	}
	
	public static InputStream getInputStreamFromFile(String location, String fileName){
		InputStream stream = null;
		try {
			stream = new FileInputStream(buildFullFileName(location, fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return stream;
	}
	
	public static String getClassFolderPath(Class cs){
		String fileFolder = cs.getResource("").getFile();
		return fileFolder;
	}
	

	
	public static String buildFullFileName(String location, String fileName, String type){
		return location+"/"+fileName+"."+type; 
	}

	public static String buildFullFileName(String location, String fileName){
		return location+"/"+fileName; 
	}
}
