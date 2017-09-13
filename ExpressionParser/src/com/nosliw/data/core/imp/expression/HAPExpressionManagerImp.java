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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionParser;

public class HAPExpressionManagerImp implements HAPExpressionManager{

	//all expression definition suites
	private Map<String, HAPExpressionDefinitionSuiteImp> m_expressionDefinitionSuites;

	//used to generate id
	private int m_idIndex;
	
	//processor
	private HAPExpressionDefinitionProcessorImp m_expressionProcessor;
	
	public HAPExpressionManagerImp(HAPExpressionParser expressionParser, HAPDataTypeHelper dataTypeHelper){
		this.m_expressionProcessor = new HAPExpressionDefinitionProcessorImp(expressionParser, dataTypeHelper);
		this.init();
	}
	
	private void init(){
		HAPValueInfoManager.getInstance().importFromClassFolder(this.getClass());
		
		this.m_expressionDefinitionSuites = new LinkedHashMap<String, HAPExpressionDefinitionSuiteImp>();
		this.m_idIndex = 1;
	}

	@Override
	public HAPExpressionDefinitionSuite getExpressionDefinitionSuite(String suiteName){		return this.m_expressionDefinitionSuites.get(suiteName);	}
	
	@Override
	public Set<String> getExpressionDefinitionSuites() {		return this.m_expressionDefinitionSuites.keySet();	}
	
	@Override
	public void addExpressionDefinitionSuite(HAPExpressionDefinitionSuite expressionDefinitionSuite){
		HAPExpressionDefinitionSuiteImp suite = (HAPExpressionDefinitionSuiteImp)this.getExpressionDefinitionSuite(expressionDefinitionSuite.getName());
		if(suite==null){
			this.m_expressionDefinitionSuites.put(expressionDefinitionSuite.getName(), (HAPExpressionDefinitionSuiteImp)expressionDefinitionSuite);
		}
		else{
			suite.merge((HAPExpressionDefinitionSuiteImp)expressionDefinitionSuite);
		}
	}
	
	@Override
	public HAPExpressionDefinition getExpressionDefinition(String suite, String name) {		return this.getExpressionDefinitionSuite(suite).getExpressionDefinition(name);	}

	@Override
	public HAPExpression processExpression(String suiteName, String expressionName, Map<String, HAPDataTypeCriteria> variableCriterias){
		String id = expressionName + "_no" + this.m_idIndex++;
		HAPExpressionDefinitionSuite suite = this.getExpressionDefinitionSuite(suiteName);
		HAPExpressionDefinition expDef = suite.getExpressionDefinition(expressionName); 
		HAPExpression expression = this.m_expressionProcessor.processExpressionDefinition(id, expDef, suite.getAllExpressionDefinitions(), suite.getConstants(), variableCriterias);
		return expression;
	}

	@Override
	public HAPExpression processExpression(HAPExpressionDefinition expressionDefinition, Map<String, HAPDataTypeCriteria> variableCriterias) {
		String id = ""+this.m_idIndex++;
		HAPExpression expression = this.m_expressionProcessor.processExpressionDefinition(id, expressionDefinition, null, null, variableCriterias);
		return expression;
	}

	public void importExpressionFromFolder(String folder){
		Set<File> files = HAPFileUtility.getAllFiles(folder);
		for(File file : files){
			if(file.getName().endsWith(".expression")){
				try {
					InputStream inputStream = new FileInputStream(file);
			         HAPExpressionDefinitionSuiteImp expressionDefinitionSuite = (HAPExpressionDefinitionSuiteImp)HAPStringableEntityImporterJSON.parseJsonEntity(inputStream, HAPExpressionDefinitionSuiteImp._VALUEINFO_NAME, HAPValueInfoManager.getInstance());
			         this.addExpressionDefinitionSuite(expressionDefinitionSuite);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void importFromClassFolder(Class cs){
		try{
			URI uri = cs.getResource("").toURI();
		    try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
		    	Path path = Paths.get(uri);
				Files.walkFileTree(path, new HashSet(), 1, new SimpleFileVisitor<Path>() { 
		            @Override
		            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		            	if(file.getFileName().toString().endsWith(".expression")){
					         HAPExpressionDefinitionSuiteImp expressionDefinitionSuite = (HAPExpressionDefinitionSuiteImp)HAPStringableEntityImporterJSON.parseJsonEntity(Files.newInputStream(file), HAPExpressionDefinitionSuiteImp._VALUEINFO_NAME, HAPValueInfoManager.getInstance());
					         addExpressionDefinitionSuite(expressionDefinitionSuite);
		            	}
		                return FileVisitResult.CONTINUE;
		            }
		        });
		    }
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
