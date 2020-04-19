package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPExecutableExpressionGroupInSuite extends HAPExecutableExpressionGroupImp{

	private String m_id;
	
	private HAPContext m_context;
	
	private Map<String, HAPVariableInfo> m_localVarsInfo;

	private Map<String, HAPExecutableExpression> m_expressionItem;
	 
	public HAPExecutableExpressionGroupInSuite(String id) {
		this.m_expressionItem = new LinkedHashMap<String, HAPExecutableExpression>();
		this.m_id = id;
		this.m_localVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
	}
	
	@Override
	public void addExpression(String name, HAPOperandWrapper operand) {
		this.m_expressionItem.put(name, new HAPExecutableExpression(operand.cloneWrapper()));
	}

	@Override
	public Map<String, HAPExecutableExpression> getExpressionItems(){
		return this.m_expressionItem;
	}

	@Override
	public String getId() {  return this.m_id;  }
	public void setId(String id) {   this.m_id = id;    }
	
	@Override
	public HAPContext getContext() {   return this.m_context;  }

	@Override
	public void setContext(HAPContext context) {   this.m_context = context;  }
	
	@Override
	public Map<String, HAPVariableInfo> getVarsInfo() {  return this.m_localVarsInfo;  }
	public void setVarsInfo(Map<String, HAPVariableInfo> varsInfo) {   this.m_localVarsInfo = varsInfo;  }

	@Override
	public void updateVariableName(HAPUpdateName nameUpdate) {
		Map<String, HAPVariableInfo> localVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String name : this.m_localVarsInfo.keySet()) {
			localVarsInfo.put(nameUpdate.getUpdatedName(name), this.m_localVarsInfo.get(name));
		}
		this.m_localVarsInfo = localVarsInfo;
		
		HAPContext updatedContext = new HAPContext();
		for(String name : this.m_context.getElementNames()) {
			updatedContext.addElement(nameUpdate.getUpdatedName(name), this.m_context.getElement(name));
		}
		this.m_context = updatedContext;
		
		for(String name : this.m_expressionItem.keySet()) {
			this.m_expressionItem.get(name).updateVariableName(nameUpdate);
		}
	}
	
	@Override
	public void discover(Map<String, HAPDataTypeCriteria> expectOutput, HAPDataTypeHelper dataTypeHelper, HAPProcessTracker processTracker) {
		Map<String, HAPVariableInfo> discoveredVarsInf = new LinkedHashMap<String, HAPVariableInfo>();
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
		
		HAPOperandUtility.discover(
				operands,
				outPutCriteria,
				this.m_localVarsInfo,
				discoveredVarsInf,
				matchers,
				dataTypeHelper,
				processTracker);
		
		for(int i=0; i<names.size(); i++) {
			this.m_expressionItem.get(names.get(i)).setOutputMatchers(matchers.get(i));
		}
		this.m_localVarsInfo.clear();
		this.m_localVarsInfo.putAll(discoveredVarsInf);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildJson(this.getExpressionItems(), HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEINFOS, HAPJsonUtility.buildJson(this.m_localVarsInfo, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		Map<String, String> expressionsJson = new LinkedHashMap<String, String>();
		for(String id : this.m_expressionItem.keySet()) {
			expressionsJson.put(id, this.m_expressionItem.get(id).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildMapJson(jsonMap));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		for(String id : this.m_expressionItem.keySet()) {
			dependency.addAll(this.m_expressionItem.get(id).getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
