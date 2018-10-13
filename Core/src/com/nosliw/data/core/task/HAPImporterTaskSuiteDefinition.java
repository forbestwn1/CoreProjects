package com.nosliw.data.core.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPImporterTaskSuiteDefinition {

	static public List<HAPDefinitionTaskSuite> readTaskDefinitionSuiteFromFolder(String folder, HAPManagerTask taskManager){
		List<HAPDefinitionTaskSuite> out = new ArrayList<HAPDefinitionTaskSuite>();
		Set<File> files = HAPFileUtility.getAllFiles(folder);
		for(File file : files){
			if(file.getName().endsWith(".expression")){
				try {
					InputStream inputStream = new FileInputStream(file);
					HAPDefinitionTaskSuite taskDefinitionSuite = readTaskSuiteDefinitionFromFile(inputStream, taskManager);
			         out.add(taskDefinitionSuite);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return out;
	}

	static public List<HAPDefinitionTaskSuite> readTaskSuiteFromClassFolder(Class cs, HAPManagerTask taskManager){
		final List<HAPDefinitionTaskSuite> out = new ArrayList<HAPDefinitionTaskSuite>();
		try{
			URI uri = cs.getResource("").toURI();
		    try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
		    	Path path = Paths.get(uri);
				Files.walkFileTree(path, new HashSet(), 1, new SimpleFileVisitor<Path>() { 
		            @Override
		            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		            	if(file.getFileName().toString().endsWith(".expression")){
		            		HAPDefinitionTaskSuite taskDefinitionSuite = readTaskSuiteDefinitionFromFile(Files.newInputStream(file), taskManager); 
					         out.add(taskDefinitionSuite);
		            	}
		                return FileVisitResult.CONTINUE;
		            }
		        });
		    }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	public static HAPDefinitionTaskSuite readTaskSuiteDefinitionFromFile(InputStream inputStream, HAPManagerTask taskManager){
		HAPDefinitionTaskSuite suite = null;
		try{
			String content = HAPFileUtility.readFile(inputStream);
			JSONObject contentJson = new JSONObject(content);
			suite = readTaskSuiteDefinitionFromJSON(contentJson, taskManager);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return suite;
	}
	
	private static HAPDefinitionTaskSuite readTaskSuiteDefinitionFromJSON(JSONObject taskSuiteJson, HAPManagerTask taskManager){
		HAPDefinitionTaskSuite suite = null;
		try{
			suite = new HAPDefinitionTaskSuite(taskManager);
			suite.buildObject(taskSuiteJson, HAPSerializationFormat.JSON);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return suite;
	}
	
}
