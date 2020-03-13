package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.criteria.HAPVariableInfo;

public class HAPDefinitionExpressionSuite extends HAPResourceDefinitionContainer{

	private Map<String, HAPVariableInfo> m_variables;
	
	private Map<String, HAPData> m_constants;

	private Map<String, HAPDefinitionExpression> m_expressions;
	
	public HAPDefinitionExpressionSuite() {
		this.m_variables = new LinkedHashMap<String, HAPVariableInfo>();
		this.m_constants = new LinkedHashMap<String, HAPData>();
		this.m_expressions = new LinkedHashMap<String, HAPDefinitionExpression>();
	}
	
	public HAPDefinitionExpressionSuite(
			String name, 
			String description, 
			Map<String, HAPVariableInfo> variables,
			Map<String, HAPData> constants) {
		super();
		this.m_name = name;
		this.m_description = description;
		this.m_variables.putAll(variables);
		this.m_constants.putAll(constants);
	}
	
	public Map<String, HAPVariableInfo> getVariablesInfo(){   return this.m_variables;   }
	public void addVariableInfo(String name, HAPVariableInfo varInfo) {   this.m_variables.put(name, varInfo);   }
	
	public Map<String, HAPData> getConstants(){  return this.m_constants;    }
	public void addConstant(String name, HAPData constant) {  this.m_constants.put(name, constant);    }
	
	public void addExpressionDefinition(String name, HAPDefinitionExpression expDef) {  this.m_expressions.put(name, expDef);   }
	public void addExpressionDefinition(Map<String, HAPDefinitionExpression> expDef) {  this.m_expressions.putAll(expDef);   }
	public Map<String, HAPDefinitionExpression> getExpressionDefinitions(){   return this.m_expressions;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPResourceDefinitionContainer cloneResourceDefinitionContainer() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
