package com.nosliw.data.core.script.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expressionsuite.HAPDefinitionExpressionSuite;

//
//context for processing script expressions: 
//		supporting expression in suite
//		variables criteria
//		data constants for expression
//		value constants for script
public class HAPProcessContextScriptExpression {

	//every unit has its own expression definition suite
	//it includes expression definition and constants from parent and its own
	private HAPDefinitionExpressionSuite m_expressionDefinitionSuite;

	private Map<String, Object> m_constants;
	
	public HAPProcessContextScriptExpression(){
		this.m_expressionDefinitionSuite = new HAPDefinitionExpressionSuite();
		this.m_constants = new LinkedHashMap<String, Object>();
	}

	public HAPDefinitionExpressionSuite getExpressionDefinitionSuite(){		return this.m_expressionDefinitionSuite;	}

	public Map<String, HAPVariableInfo> getDataVariables(){  return this.m_expressionDefinitionSuite.getVariablesInfo();  }
	
	public Map<String, Object> getConstants(){  return this.m_constants;   }
	public Map<String, HAPData> getDataConstants(){  return this.m_expressionDefinitionSuite.getConstants();  }
	
	public Map<String, HAPDefinitionExpression> getExpressionDefinitions(){  return this.m_expressionDefinitionSuite.getExpressionDefinitions();   }
	
	public void addConstant(String name, Object value) {
		this.m_constants.put(name, value);
		if(value instanceof HAPData)   this.m_expressionDefinitionSuite.addConstant(name, (HAPData)value);
	}
	
	public void addConstants(Map<String, Object> datas){  
		for(String name : datas.keySet()){
			this.addConstant(name, datas.get(name));
		}
	}
	public void addExpressionDefinition(String name, HAPDefinitionExpression expressionDefinition){  this.m_expressionDefinitionSuite.addExpressionDefinition(name, expressionDefinition);  }
	public void addExpressionDefinition(Map<String, HAPDefinitionExpression> expressions){	this.m_expressionDefinitionSuite.addExpressionDefinition(expressions);	}
	public void addDataVariables(Map<String, HAPVariableInfo> variables){
		for(String varName : variables.keySet()) {
			this.m_expressionDefinitionSuite.addVariableInfo(varName, variables.get(varName));
		}
	}
	public void addDataVariable(String name, HAPVariableInfo varInfo){  
		this.m_expressionDefinitionSuite.addVariableInfo(name, varInfo);
	}	
}
