package com.nosliw.data.core.imp.expression;

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

import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.expression.HAPExpressionManager;

public class HAPExpressionImporter {

	static public List<HAPExpressionDefinitionSuite> importExpressionSuiteFromFolder(String folder, HAPExpressionManager expressionMan){
		List<HAPExpressionDefinitionSuite> suites = readExpressionSuiteFromFolder(folder);
		for(HAPExpressionDefinitionSuite suite : suites){
			expressionMan.addExpressionDefinitionSuite(suite);
		}
		return suites;
	}

	static public List<HAPExpressionDefinitionSuite> importExpressionSuiteFromClassFolder(Class cs, HAPExpressionManager expressionMan){
		List<HAPExpressionDefinitionSuite> suites = readExpressionSuiteFromClassFolder(cs);
		for(HAPExpressionDefinitionSuite suite : suites){
			expressionMan.addExpressionDefinitionSuite(suite);
		}
		return suites;
	}
	
	static public List<HAPExpressionDefinitionSuite> readExpressionSuiteFromFolder(String folder){
		List<HAPExpressionDefinitionSuite> out = new ArrayList<HAPExpressionDefinitionSuite>();
		Set<File> files = HAPFileUtility.getAllFiles(folder);
		for(File file : files){
			if(file.getName().endsWith(".expression")){
				try {
					InputStream inputStream = new FileInputStream(file);
			         HAPExpressionDefinitionSuiteImp expressionDefinitionSuite = importExpressionDefinitionSuiteFromFile(inputStream);
			         out.add(expressionDefinitionSuite);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return out;
	}

	static public List<HAPExpressionDefinitionSuite> readExpressionSuiteFromClassFolder(Class cs){
		final List<HAPExpressionDefinitionSuite> out = new ArrayList<HAPExpressionDefinitionSuite>();
		try{
			URI uri = cs.getResource("").toURI();
		    try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
		    	Path path = Paths.get(uri);
				Files.walkFileTree(path, new HashSet(), 1, new SimpleFileVisitor<Path>() { 
		            @Override
		            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		            	if(file.getFileName().toString().endsWith(".expression")){
					         HAPExpressionDefinitionSuiteImp expressionDefinitionSuite = importExpressionDefinitionSuiteFromFile(Files.newInputStream(file)); 
					         out.add(expressionDefinitionSuite);
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
	
	static private HAPExpressionDefinitionSuiteImp importExpressionDefinitionSuiteFromFile(InputStream inputStream){
        HAPExpressionDefinitionSuiteImp expressionDefinitionSuite = (HAPExpressionDefinitionSuiteImp)HAPStringableEntityImporterJSON.parseJsonEntity(inputStream, HAPExpressionDefinitionSuiteImp._VALUEINFO_NAME, HAPValueInfoManager.getInstance());
        return expressionDefinitionSuite;
	}
}
