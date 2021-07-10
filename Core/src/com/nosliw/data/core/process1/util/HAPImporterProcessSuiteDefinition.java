package com.nosliw.data.core.process1.util;

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

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;

public class HAPImporterProcessSuiteDefinition {

	static public List<HAPResourceDefinitionProcessSuite> readProcessDefinitionSuiteFromFolder(String folder, HAPManagerActivityPlugin activityPluginMan){
		List<HAPResourceDefinitionProcessSuite> out = new ArrayList<HAPResourceDefinitionProcessSuite>();
		Set<File> files = HAPFileUtility.getAllFiles(folder);
		for(File file : files){
			if(file.getName().endsWith(".process")){
				try {
					InputStream inputStream = new FileInputStream(file);
					HAPResourceDefinitionProcessSuite taskDefinitionSuite = readProcessSuiteDefinitionFromFile(inputStream, activityPluginMan);
			         out.add(taskDefinitionSuite);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return out;
	}

	static public List<HAPResourceDefinitionProcessSuite> readProcessSuiteFromClassFolder(Class cs, HAPManagerActivityPlugin activityPluginMan){
		final List<HAPResourceDefinitionProcessSuite> out = new ArrayList<HAPResourceDefinitionProcessSuite>();
		try{
			URI uri = cs.getResource("").toURI();
		    try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
		    	Path path = Paths.get(uri);
				Files.walkFileTree(path, new HashSet(), 1, new SimpleFileVisitor<Path>() { 
		            @Override
		            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		            	if(file.getFileName().toString().endsWith(".process")){
		            		HAPResourceDefinitionProcessSuite taskDefinitionSuite = readProcessSuiteDefinitionFromFile(Files.newInputStream(file), activityPluginMan); 
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

	public static HAPResourceDefinitionProcessSuite readProcessSuiteDefinitionFromFile(InputStream inputStream, HAPManagerActivityPlugin activityPluginMan){
		HAPResourceDefinitionProcessSuite suite = null;
		try{
			String content = HAPFileUtility.readFile(inputStream);
			JSONObject contentJson = HAPJsonUtility.newJsonObject(content);
			suite = HAPParserProcessDefinition.parsePocessSuite(contentJson, activityPluginMan);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return suite;
	}
}
