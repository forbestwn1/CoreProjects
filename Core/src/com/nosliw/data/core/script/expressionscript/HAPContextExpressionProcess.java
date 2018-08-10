package com.nosliw.data.core.script.expressionscript;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.expressionsuite.HAPDefinitionExpressionSuite;

//every ui resource/tag has its own context for processing expressions: 
//		supporting expression in suite
//		variables criteria
//		constants
public class HAPContextExpressionProcess {

	//every unit has its own expression definition suite
	//it includes expression definition and constants from parent and its own
	private HAPDefinitionExpressionSuite m_expressionDefinitionSuite;

	public HAPContextExpressionProcess(){
		this.m_expressionDefinitionSuite = new HAPDefinitionExpressionSuite();
	}
	
	public HAPDefinitionExpressionSuite getExpressionDefinitionSuite(){		return this.m_expressionDefinitionSuite;	}
	public Map<String, HAPVariableInfo> getVariables(){  return this.m_expressionDefinitionSuite.getVariablesInfo();  }
	public Map<String, HAPData> getConstants(){  return this.m_expressionDefinitionSuite.getConstants();  }
	public Map<String, HAPDefinitionExpression> getExpressionDefinitions(){  return this.m_expressionDefinitionSuite.getExpressionDefinitions();   }
	
	public void addConstant(String name, HAPData data){  this.m_expressionDefinitionSuite.addConstant(name, data);  }
	public void addConstants(Map<String, HAPData> datas){  
		for(String name : datas.keySet()){
			this.m_expressionDefinitionSuite.addConstant(name, datas.get(name));  
		}
	}
	public void addExpressionDefinition(String name, HAPDefinitionExpression expressionDefinition){  this.m_expressionDefinitionSuite.addExpressionDefinition(name, expressionDefinition);  }
	public void addVariables(Map<String, HAPVariableInfo> variables){
		for(String varName : variables.keySet()) {
			this.m_expressionDefinitionSuite.addVariableInfo(varName, variables.get(varName));
		}
	}
	public void addVariable(String name, HAPVariableInfo varInfo){  
		this.m_expressionDefinitionSuite.addVariableInfo(name, varInfo);
	}	
}
