package com.nosliw.data.core.domain.entity.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute(baseName="EXPRESSIONGROUP")
public class HAPExecutableExpressionGroup extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION;

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	@HAPAttribute
	public static String EXPRESSIONS = "expressions";

	private Map<String, HAPExecutableExpression> m_expressionItem;
	 
	private HAPContainerVariableCriteriaInfo m_varInfos;

	//temp info
	private Map<String, HAPData> m_dataConstants;
	public void setDataConstants(Map<String, HAPData> dataConstants) {   this.m_dataConstants = dataConstants;    }
	public Map<String, HAPData> getDataConstants(){   return this.m_dataConstants;     }
	
	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varInfo) {  this.m_varInfos = varInfo;    }
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return this.m_varInfos;    }
	
	
	public HAPExecutableExpressionGroup() {
		super(ENTITY_TYPE);
		this.m_expressionItem = new LinkedHashMap<String, HAPExecutableExpression>();
	}
	
	public void addExpression(String name, HAPOperandWrapper operand) {
		if(name==null)   name = HAPConstantShared.NAME_DEFAULT;
		this.m_expressionItem.put(name, new HAPExecutableExpression(operand.cloneWrapper()));
	}

	public Map<String, HAPExecutableExpression> getExpressionItems(){
		return this.m_expressionItem;
	}

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
		jsonMap.put(EXPRESSIONS, HAPUtilityJson.buildJson(this.getExpressionItems(), HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEINFOS, HAPUtilityJson.buildJson(this.m_varInfos, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		Map<String, String> expressionsJson = new LinkedHashMap<String, String>();
		for(String id : this.m_expressionItem.keySet()) {
			expressionsJson.put(id, this.m_expressionItem.get(id).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(EXPRESSIONS, HAPUtilityJson.buildMapJson(expressionsJson));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		for(String id : this.m_expressionItem.keySet()) {
			dependency.addAll(this.m_expressionItem.get(id).getResourceDependency(runtimeInfo, resourceManager));
		}
	}
	@Override
	public String toString(HAPDomainEntityExecutableResourceComplex domain) {
		// TODO Auto-generated method stub
		return null;
	}
}
