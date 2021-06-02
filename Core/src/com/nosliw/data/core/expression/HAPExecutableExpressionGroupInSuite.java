package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPExecutableExpressionGroupInSuite extends HAPExecutableExpressionGroupImp{

	private String m_id;
	
	private HAPValueStructure m_valueStructure;
	
	private Map<String, HAPExecutableExpression> m_expressionItem;
	 
	private HAPContainerVariableCriteriaInfo m_varInfos;

	//temp info
	private Map<String, HAPData> m_dataConstants;
	public void setDataConstants(Map<String, HAPData> dataConstants) {   this.m_dataConstants = dataConstants;    }
	public Map<String, HAPData> getDataConstants(){   return this.m_dataConstants;     }
	
	@Override
	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varInfo) {  this.m_varInfos = varInfo;    }
	@Override
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return this.m_varInfos;    }
	
	
	public HAPExecutableExpressionGroupInSuite(String id) {
		this.m_expressionItem = new LinkedHashMap<String, HAPExecutableExpression>();
		this.m_id = id;
	}
	
	@Override
	public void addExpression(String name, HAPOperandWrapper operand) {
		if(name==null)   name = HAPConstantShared.NAME_DEFAULT;
		this.m_expressionItem.put(name, new HAPExecutableExpression(operand.cloneWrapper()));
	}

	@Override
	public Map<String, HAPExecutableExpression> getExpressionItems(){
		return this.m_expressionItem;
	}

	@Override
	public String getId() {  return this.m_id;  }
	public void setId(String id) {   this.m_id = id;    }
	
	public HAPValueStructure getValueStructureDefinition() {   return this.m_valueStructure;  }

	public void setValueStructureDefinition(HAPValueStructure structureDefinition) {	this.m_valueStructure = structureDefinition;	}
	
	@Override
	public void discover(Map<String, HAPDataTypeCriteria> expectOutput, HAPDataTypeHelper dataTypeHelper, HAPProcessTracker processTracker) {
		Map<String, HAPInfoCriteria> discoveredVarsInf = new LinkedHashMap<String, HAPInfoCriteria>();
		List<String> names = new ArrayList<String>();
		List<HAPOperand> operands = new ArrayList<HAPOperand>();
		List<HAPDataTypeCriteria> outPutCriteria = new ArrayList<HAPDataTypeCriteria>();
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		
		for(String name : this.m_expressionItem.keySet()) {
			names.add(name);
			if(expectOutput==null)  outPutCriteria.add(null);
			else outPutCriteria.add(expectOutput.get(name));
			operands.add(this.m_expressionItem.get(name).getOperand().getOperand());
		}
		
		this.m_varInfos = HAPOperandUtility.discover(
				operands,
				outPutCriteria,
				this.m_varInfos,
				discoveredVarsInf,
				matchers,
				dataTypeHelper,
				processTracker);
		
		for(int i=0; i<names.size(); i++) {
			this.m_expressionItem.get(names.get(i)).setOutputMatchers(matchers.get(i));
		}
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildJson(this.getExpressionItems(), HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEINFOS, HAPJsonUtility.buildJson(this.m_varInfos, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		Map<String, String> expressionsJson = new LinkedHashMap<String, String>();
		for(String id : this.m_expressionItem.keySet()) {
			expressionsJson.put(id, this.m_expressionItem.get(id).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildMapJson(expressionsJson));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		for(String id : this.m_expressionItem.keySet()) {
			dependency.addAll(this.m_expressionItem.get(id).getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
