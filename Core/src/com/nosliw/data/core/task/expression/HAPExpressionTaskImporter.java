package com.nosliw.data.core.task.expression;

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
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPDefinitionTaskSuiteForTest;
import com.nosliw.data.core.task.HAPTaskManager;

public class HAPExpressionTaskImporter {

	static public List<HAPDefinitionTaskSuite> importTaskDefinitionSuiteFromFolder(String folder, HAPTaskManager expressionTaskMan){
		List<HAPDefinitionTaskSuite> suites = readTaskDefinitionSuiteFromFolder(folder);
		for(HAPDefinitionTaskSuite suite : suites){
			expressionTaskMan.addTaskDefinitionSuite(suite);
		}
		return suites;
	}

	static public List<HAPDefinitionTaskSuite> importTaskDefinitionSuiteFromClassFolder(Class cs, HAPTaskManager expressionTaskMan){
		List<HAPDefinitionTaskSuite> suites = readTaskSuiteFromClassFolder(cs);
		for(HAPDefinitionTaskSuite suite : suites){
			expressionTaskMan.addTaskDefinitionSuite(suite);
		}
		return suites;
	}
	
	static public List<HAPDefinitionTaskSuite> readTaskDefinitionSuiteFromFolder(String folder){
		List<HAPDefinitionTaskSuite> out = new ArrayList<HAPDefinitionTaskSuite>();
		Set<File> files = HAPFileUtility.getAllFiles(folder);
		for(File file : files){
			if(file.getName().endsWith(".expression")){
				try {
					InputStream inputStream = new FileInputStream(file);
					HAPDefinitionTaskSuite taskDefinitionSuite = importExpressionDefinitionSuiteFromFile(inputStream);
			         out.add(taskDefinitionSuite);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return out;
	}

	static public List<HAPDefinitionTaskSuite> readTaskSuiteFromClassFolder(Class cs){
		final List<HAPDefinitionTaskSuite> out = new ArrayList<HAPDefinitionTaskSuite>();
		try{
			URI uri = cs.getResource("").toURI();
		    try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
		    	Path path = Paths.get(uri);
				Files.walkFileTree(path, new HashSet(), 1, new SimpleFileVisitor<Path>() { 
		            @Override
		            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		            	if(file.getFileName().toString().endsWith(".expression")){
		            		HAPDefinitionTaskSuite taskDefinitionSuite = importExpressionDefinitionSuiteFromFile(Files.newInputStream(file)); 
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
	
	static private HAPDefinitionTaskSuite importExpressionDefinitionSuiteFromFile(InputStream inputStream){
		HAPDefinitionTaskSuite suite = null;
		try{
			String content = HAPFileUtility.readFile(inputStream);
			JSONObject contentJson = new JSONObject(content);
			suite = importTaskDefinitionSuiteFromJSON(contentJson);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return suite;
	}
	
	public static HAPDefinitionTaskSuite importTaskDefinitionSuiteFromJSON(JSONObject taskSuiteJson){
		HAPDefinitionTaskSuiteForTest suite = null;
		try{
			suite = new HAPDefinitionTaskSuiteForTest();
			suite.buildObject(taskSuiteJson, HAPSerializationFormat.JSON);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return suite;
	}
}
