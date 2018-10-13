package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.task111.HAPDefinitionTask;
import com.nosliw.data.core.task111.HAPDefinitionTaskSuiteForTest;
import com.nosliw.data.core.task111.expression.HAPExpressionTaskUtility;
import com.nosliw.data.core.task111.expression.HAPReferenceInfo;

public class HAPExpressionDefinitionSuiteImp{ 
/*
extends HAPStringableValueEntity implements HAPDefinitionTaskSuiteForTest {

	public static String _VALUEINFO_NAME;

	@HAPAttribute
	public static String VARIABLESDATA = "variablesData";

	@HAPAttribute
	public static String RESULT = "result";

	@HAPAttribute
	public static String ERRORCODE = "errorCode";

	@HAPAttribute
	public static String CONFIGURE = "configure";
	
	private Map<String, HAPDefinitionTask> m_expressionDefinitions;

	private String m_name;
	
	private Map<String, HAPData> m_constants;
	
	public HAPExpressionDefinitionSuiteImp(){}

	public HAPExpressionDefinitionSuiteImp(String name){
		this.m_name = name;
		this.m_constants = new LinkedHashMap<String, HAPData>();
	}
	
	@Override
	public String getName() {
		if(this.m_name!=null)   return this.m_name;
		else return this.getAtomicAncestorValueString(NAME);	
	}
	
	@Override
	public HAPDefinitionTask getTaskDefinition(String name) {
		return this.getAllTaskDefinitions().get(name);
	}

	@Override
	public Map<String, HAPData> getConstants() {
		if(this.m_constants==null)	this.m_constants = this.getMapValueAncestorByPath(CONSTANTS);
		if(this.m_constants==null)  this.m_constants = new LinkedHashMap<String, HAPData>();
		return this.m_constants;
	}
	
	public void addConstant(String name, HAPData data){		this.getConstants().put(name, data);	}

	@Override
	public Map<String, HAPDefinitionTask> getAllTaskDefinitions(){
		if(this.m_expressionDefinitions==null){
			this.m_expressionDefinitions = this.getMapValueAncestorByPath(EXPRESSIONDEFINITIONS);
			if(this.m_expressionDefinitions==null)   this.m_expressionDefinitions = new LinkedHashMap<String, HAPDefinitionTask>();
			else{
				for(String name : this.m_expressionDefinitions.keySet()){
					this.processExpressionDefinition(this.m_expressionDefinitions.get(name));
				}
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

	public void addExpressionDefinition(HAPDefinitionTask expressionDefinition){
		this.processExpressionDefinition(expressionDefinition);
		this.getAllTaskDefinitions().put(expressionDefinition.getName(), expressionDefinition);
	}
		
	private void processExpressionDefinition(HAPDefinitionTask expressionDefinition){
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
				toVar = HAPExpressionTaskUtility.updateVaraible(referenceName, toVar);
				newVarMap.put(fromVar, toVar);
			}
		}
		
	}

	public void merge(HAPExpressionDefinitionSuiteImp suite){
		this.getAllTaskDefinitions().putAll(suite.getAllTaskDefinitions());
		this.getVariableData().putAll(suite.getVariableData());
	}
*/
}
