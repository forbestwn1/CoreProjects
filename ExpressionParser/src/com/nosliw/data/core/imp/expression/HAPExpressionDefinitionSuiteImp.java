package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPReferenceInfo;

public class HAPExpressionDefinitionSuiteImp extends HAPStringableValueEntity implements HAPExpressionDefinitionSuite {

	public static String _VALUEINFO_NAME;

	@HAPAttribute
	public static String VARIABLESDATA = "variablesData";

	@HAPAttribute
	public static String RESULT = "result";

	public HAPExpressionDefinitionSuiteImp(){}

	Map<String, HAPExpressionDefinition> m_expressionDefinitions;

	Map<String, HAPData> m_variablesData;
	
	@Override
	public String getName() {  return this.getAtomicAncestorValueString(NAME);	}

	@Override
	public HAPExpressionDefinition getExpressionDefinition(String name) {
		return this.getExpressionDefinitions().get(name);
	}

	public HAPDataWrapper getResult(){		return (HAPDataWrapper)this.getAtomicAncestorValue(RESULT);	}
	
	public Map<String, HAPData> getVariableData() {
		Map<String, HAPData> varsData = this.getMapValueAncestorByPath(VARIABLESDATA); 
		return varsData;
	}

	public void addExpressionDefinition(HAPExpressionDefinition expressionDefinition){
		this.processExpressionDefinition(expressionDefinition);
		this.m_expressionDefinitions.put(expressionDefinition.getExpression(), expressionDefinition);
	}
		
	private void processExpressionDefinition(HAPExpressionDefinition expressionDefinition){
		String name = expressionDefinition.getName();
		if(HAPBasicUtility.isStringEmpty(name)){
			name = System.currentTimeMillis()+"";
			((HAPExpressionDefinitionImp)expressionDefinition).setName(name);
		}
		
		//process reference variable: update mapping variable name in "to" part by adding reference name 
		Map<String, HAPReferenceInfo> references = expressionDefinition.getReferences();
		for(String referenceName : references.keySet()){
			Map<String, String> oldVarMap = references.get(referenceName).getVariablesMap();
			Map<String, String> newVarMap = new LinkedHashMap<String, String>();
			for(String fromVar : oldVarMap.keySet()){
				String toVar = oldVarMap.get(fromVar);
				toVar = HAPExpressionUtility.updateVaraible(referenceName, toVar);
				newVarMap.put(fromVar, toVar);
			}
		}
		
	}

	
	private Map<String, HAPExpressionDefinition> getExpressionDefinitions(){
		if(this.m_expressionDefinitions==null){
			this.m_expressionDefinitions = this.getMapValueAncestorByPath(EXPRESSIONDEFINITIONS);
			for(String name : this.m_expressionDefinitions.keySet()){
				this.processExpressionDefinition(this.m_expressionDefinitions.get(name));
			}
		}
		return this.m_expressionDefinitions;
	}
	
	public void merge(HAPExpressionDefinitionSuiteImp suite){
		this.getExpressionDefinitions().putAll(suite.getExpressionDefinitions());
		this.getVariableData().putAll(suite.getVariableData());
	}
}
