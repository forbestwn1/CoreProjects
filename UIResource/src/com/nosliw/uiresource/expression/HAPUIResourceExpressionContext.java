package com.nosliw.uiresource.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.imp.expression.HAPExpressionDefinitionSuiteImp;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;

//every ui resource/tag has its own context for processing expressions: 
//		supporting expression in suite
//		variables criteria
public class HAPUIResourceExpressionContext {

	//every unit has its own expression definition suite
	//it includes expression definition and constants from parent and its own
	private HAPExpressionDefinitionSuiteImp m_expressionDefinitionSuite;

	//variables defined in context
	private Map<String, HAPDataTypeCriteria> m_variables; 	

	public HAPUIResourceExpressionContext(){
		this.m_expressionDefinitionSuite = new HAPExpressionDefinitionSuiteImp();
		this.m_variables = new LinkedHashMap<String, HAPDataTypeCriteria>();
	}
	
	public HAPDefinitionTaskSuite getExpressionDefinitionSuite(){		return this.m_expressionDefinitionSuite;	}
	public Map<String, HAPDataTypeCriteria> getVariables(){  return this.m_variables;  }
	public Map<String, HAPData> getConstants(){  return this.m_expressionDefinitionSuite.getConstants();  }
	public Map<String, HAPDefinitionTask> getExpressionDefinitions(){  return this.m_expressionDefinitionSuite.getAllTaskDefinitions();   }
	
	public void addConstant(String name, HAPData data){  this.m_expressionDefinitionSuite.addConstant(name, data);  }
	public void addConstants(Map<String, HAPData> datas){  
		for(String name : datas.keySet()){
			this.m_expressionDefinitionSuite.addConstant(name, datas.get(name));  
		}
	}
	public void addExpressionDefinition(HAPDefinitionTask expressionDefinition){  this.m_expressionDefinitionSuite.addExpressionDefinition(expressionDefinition);  }
	public void addVariables(Map<String, HAPDataTypeCriteria> variables){  this.m_variables.putAll(variables);  }
	public void addVariable(String name, HAPDataTypeCriteria criteria){  this.m_variables.put(name, criteria);  }
	
}
