package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuiteForTest;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPReferenceInfo;

public class HAPExpressionDefinitionSuiteImp extends HAPStringableValueEntity implements HAPExpressionDefinitionSuiteForTest {

	public static String _VALUEINFO_NAME;

	@HAPAttribute
	public static String VARIABLESDATA = "variablesData";

	@HAPAttribute
	public static String RESULT = "result";

	@HAPAttribute
	public static String ERRORCODE = "errorCode";

	@HAPAttribute
	public static String CONFIGURE = "configure";
	
	private Map<String, HAPExpressionDefinition> m_expressionDefinitions;

	private String m_name;
	
//	Map<String, HAPData> m_variablesData;
	
	public HAPExpressionDefinitionSuiteImp(){}

	public HAPExpressionDefinitionSuiteImp(String name){
		this.m_name = name;
	}
	
	@Override
	public String getName() {
		if(this.m_name!=null)   return this.m_name;
		else return this.getAtomicAncestorValueString(NAME);	
	}
	
	@Override
	public HAPExpressionDefinition getExpressionDefinition(String name) {
		return this.getAllExpressionDefinitions().get(name);
	}

	@Override
	public Map<String, HAPData> getConstants() {
		Map<String, HAPData> constants = this.getMapValueAncestorByPath(CONSTANTS); 
		return constants;
	}

	@Override
	public Map<String, HAPExpressionDefinition> getAllExpressionDefinitions(){
		if(this.m_expressionDefinitions==null){
			this.m_expressionDefinitions = this.getMapValueAncestorByPath(EXPRESSIONDEFINITIONS);
			for(String name : this.m_expressionDefinitions.keySet()){
				this.processExpressionDefinition(this.m_expressionDefinitions.get(name));
			}
		}
		return this.m_expressionDefinitions;
	}
	
	@Override
	public Map<String, String> getConfigure(){
		return this.getMapValueAncestorByPath(CONFIGURE);
	}
	
	public String getErrorCode() {
		return this.getAtomicAncestorValueString(ERRORCODE);	
	}

	public HAPDataWrapper getResult(){		return (HAPDataWrapper)this.getAtomicAncestorValue(RESULT);	}
	
	@Override
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
			expressionDefinition.setName(name);
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

	public void merge(HAPExpressionDefinitionSuiteImp suite){
		this.getAllExpressionDefinitions().putAll(suite.getAllExpressionDefinitions());
		this.getVariableData().putAll(suite.getVariableData());
	}

}
