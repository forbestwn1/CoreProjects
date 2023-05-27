package com.nosliw.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.nosliw.common.serialization.HAPUtilityJson;

public class HAPUtilityFile {
	
	public static String getFileCoreName(String fileName) {
		int index = fileName.indexOf(".");
		if(index==-1)   return fileName;
		else return fileName.substring(0, index);
	}
	
	public static List<File> sortFiles(Set<File> files){
		List<File> sortedList = new ArrayList<File>(files);
		Collections.sort(sortedList, new Comparator<File>() {

			@Override
			public int compare(File arg0, File arg1) {
				return arg0.getPath().compareTo(arg1.getPath());
			}
		});
		return sortedList;
	}
	
	public static Set<File> getChildren(String path){
    	Set<File> out = new HashSet<File>();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return out;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
        	out.add(fs);
        }
        return out;
	}
	
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
            	out.addAll(HAPUtilityFile.getAllFiles(fs.getAbsolutePath()));
            } else {
            	out.add(fs);
            }
        }
        return out;
    }
	
    public static String readFile(File file){
    	return HAPUtilityFile.readFile(file.getAbsolutePath());
    }
    
	public static String readFile(String filePath){
		return readFile(filePath, "\n");
	}
	
	public static String readFile(String filePath, String nextLine){
		try {
			return FileUtils.readFileToString(new File(filePath) , Charset.forName("UTF-8"));
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	public static String readFile(InputStream stream){
		return readFile(stream, "\n");
	}

	public static String writeJsonFile(String folder, String fileName, String content) {    return writeFile(folder, fileName, HAPUtilityJson.formatJson(content));     }
	public static String writeJsonFile(String fileName, String content){    return writeFile(fileName, HAPUtilityJson.formatJson(content));     }
	
	public static String writeFile(String folder, String fileName, String content){		return writeFile(buildFullFileName(folder, fileName), content); 	}
	public static String writeFile(String fileName, String content){
		try {
			fileName = getValidFileName(fileName);
			File file = new File(fileName);
			//create dir if not exist
			file.getParentFile().mkdirs();
			
			FileUtils.writeStringToFile(file, content, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public static void deleteFolder(String path) {
		try {
			FileUtils.deleteDirectory(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteFile(String path) {
		File file = new File(path);
		file.delete();
	}
	
	private static String getValidFileName(String fileFullName){
		int index = fileFullName.lastIndexOf("/");
		if(index==-1)  index = fileFullName.lastIndexOf("\\");
		
		String path = null;
		String fileName = null;
		if(index==-1)  fileName = fileFullName;
		else{
			fileName = fileFullName.substring(index+1);
			path = fileFullName.substring(0, index+1);
		}
		
		fileName = encodeName(fileName);

		String out = "";
		if(path!=null)  out = out + path;
		out = out + fileName;
		return out;
	}

	private static String encodeName(String name) {
		String out = name;
		char[] invalidChars = {'|', ':', '*'};
		for(char invalidChar : invalidChars){
			out = out.replace(invalidChar, '_');
		}
		return out;
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
			stream.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return out.toString();
	}

	public static String readFile(Class c, String fileName) {
		InputStream inputStream = getInputStreamOnClassPath(c, fileName);
		return readFile(inputStream);
	}
	
	public static InputStream getInputStreamOnClassPath(Class c, String fileName){
		InputStream stream = c.getResourceAsStream(fileName);
		return stream;
	}

	public static String getFileNameOnClassPath(Class c, String fileName){
		return c.getResource(fileName).getFile();
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
	
	public static Path getClassFolderPath(Class cs){
		Path out = null;
		try{
			URI uri = cs.getResource("").toURI();
	        System.out.println("Starting from: " + uri);
		    try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
		    	out = Paths.get(uri);
		    }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	public static String getClassFolderName(Class cs){
		/*
		try{
			{
		     URI uri = cs.getResource("").toURI();
		        System.out.println("Starting from: " + uri);
		        try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
		            Path myPath = Paths.get(uri);
		            Files.walkFileTree(myPath, new SimpleFileVisitor<Path>() { 
		                @Override
		                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		                	if(file.toString().endsWith("xml")){
			                    System.out.println(file);
			                    BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
			                    System.out.println(reader.readLine());
			                    System.out.println(reader.readLine());
			                    System.out.println(reader.readLine());
			                    System.out.println(reader.readLine());
		                	}
		                    return FileVisitResult.CONTINUE;
		                }
		            });
		        }
			}
			
			
		        {
	       URI uri = cs.getResource("").toURI();
	        Path myPath;
	        if (uri.getScheme().equals("jar")) {
	            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
	            myPath = fileSystem.getPath("");
	        } else {
	            myPath = Paths.get(uri);
	        }
	        Stream<Path> walk = Files.walk(myPath, 1);
	        for (Iterator<Path> it = walk.iterator(); it.hasNext();){
	            System.out.println(it.next());
	        }
		        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		*/
		
		String fileFolder = cs.getResource("").getFile();
		return fileFolder;
	}
	
	public static String buildFullFileName(String location, String fileName, String type){	return normalizeFolderPath(location)+encodeName(fileName)+"."+type;	}

	public static String buildFullFileName(String location, String fileName){		return normalizeFolderPath(location)+encodeName(fileName);  	}

	public static String buildFullFolderPath(String base, String path){		return normalizeFolderPath(base)+encodeName(path)+"/";  	}

	public static String getValidFolder(String folderPath){
		File directory = new File(folderPath);
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    return directory.getAbsolutePath();
	}

	
	private static String normalizeFolderPath(String folder) {
		if(folder.endsWith("/")||folder.endsWith("\\")) 	return folder;
		else 	return folder + "/";
	}
}
