package com.nosliw.data.core.operand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPOperandReference extends HAPOperandImp{

	@HAPAttribute
	public static final String REFERENCE = "reference";
	
	@HAPAttribute
	public static final String EXPRESSION = "expression";
	
	@HAPAttribute
	public static final String ELEMENTNAME = "elementName";
	
	@HAPAttribute
	public static final String VARMATCHERS = "varMatchers";
	
	@HAPAttribute
	public static final String VARMAPPING = "varMapping";
	
	//reference to expression (attachent name or resource id)
	private String m_reference;
	
	//mapping from this expression to referenced expression variable (ref variable id path --  source operand)
	protected Map<String, HAPWrapperOperand> m_mapping = new LinkedHashMap<String, HAPWrapperOperand>();

	//referenced expression
	private HAPIdEntityInDomain m_referredExpressionExeId;
	
	//expression element in group
	private String m_elementName;
	
	private Map<String, HAPMatchers> m_matchers;
	
	private HAPOperandReference(){
		super(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE);
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_mapping = new LinkedHashMap<String, HAPWrapperOperand>();
		this.setElementName(null);
	}
	
	public HAPOperandReference(String reference){
		this();
		this.m_reference = reference;
	}

	public String getReference(){  return this.m_reference;  }
	
	public void addMapping(String varName, HAPOperand operand) {	this.m_mapping.put(varName, this.createOperandWrapper(operand));	}
	public Map<String, HAPWrapperOperand> getMapping(){    return this.m_mapping;      }
	public void setMapping(Map<String, HAPWrapperOperand> mapping) {    
		this.m_mapping.clear();
		this.m_mapping.putAll(mapping);
	}

	public HAPIdEntityInDomain getReferedExpression() {   return this.m_referredExpressionExeId;   }
	public void setReferedExpression(HAPIdEntityInDomain m_referredExpressionExeId) {   this.m_referredExpressionExeId = m_referredExpressionExeId;    }

	public String getElementName() {   return this.m_elementName;   }
	public void setElementName(String elementName) {
		this.m_elementName = elementName;    
		if(this.m_elementName==null)  this.m_elementName = HAPConstantShared.NAME_DEFAULT;
	}
	
	@Override
	public List<HAPWrapperOperand> getChildren(){
		List<HAPWrapperOperand> out = new ArrayList<HAPWrapperOperand>();
		out.addAll(this.m_mapping.values());
		return out;
	}

	@Override
	public Set<HAPDataTypeConverter> getConverters(){
		Set<HAPDataTypeConverter> out = new HashSet<HAPDataTypeConverter>();
		for(String var : m_matchers.keySet()){
			out.addAll(HAPMatcherUtility.getConverterResourceIdFromRelationship(m_matchers.get(var).discoverRelationships()));
		}
		return out;	
	}

	@Override
	public List<HAPResourceIdSimple> getResources(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceIdSimple> out = super.getResources(runtimeInfo, resourceManager);
		List<HAPResourceDependency> referenceResources = this.m_referredExpressionExeId.getResourceDependency(runtimeInfo, resourceManager);
		for(HAPResourceDependency dependency : referenceResources) {
			out.add((HAPResourceIdSimple)dependency.getId());
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCE, m_reference);
		jsonMap.put(ELEMENTNAME, this.m_elementName);
		if(this.m_referredExpressionExeId!=null)  jsonMap.put(EXPRESSION, this.m_referredExpressionExeId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VARMAPPING, HAPUtilityJson.buildJson(this.m_mapping, HAPSerializationFormat.JSON));
		jsonMap.put(VARMATCHERS, HAPUtilityJson.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPMatchers discover(
			HAPContainerVariableCriteriaInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		
		Map<String, HAPDataTypeCriteria> cs = new LinkedHashMap<String, HAPDataTypeCriteria>();
		cs.put(this.m_elementName, expectCriteria);
		this.m_referredExpressionExeId.discover(cs, dataTypeHelper, processTracker);

		//variable
		
		HAPContainerVariableCriteriaInfo internalVariablesInfo = this.m_referredExpressionExeId.getVariablesInfo();
		for(String inVarId : this.m_mapping.keySet()) {
			HAPMatchers matchers = this.m_mapping.get(inVarId).getOperand().discover(variablesInfo, internalVariablesInfo.getVariableCriteriaInfo(inVarId).getCriteria(), processTracker, dataTypeHelper);
			if(matchers!=null)  this.m_matchers.put(inVarId, matchers);
		}
		
		//output
		HAPDataTypeCriteria outputCriteria = this.m_referredExpressionExeId.getExpressionItems().get(this.m_elementName).getOutputCriteria();
		return HAPUtilityCriteria.isMatchable(outputCriteria, expectCriteria, dataTypeHelper);
	}
	
	@Override
	public HAPOperand cloneOperand() {
		HAPOperandReference out = new HAPOperandReference();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandReference operand){
		super.cloneTo(operand);
		operand.m_reference = this.m_reference;
		operand.m_elementName = this.m_elementName;
		for(String name : this.m_mapping.keySet()) 	operand.m_mapping.put(name, this.m_mapping.get(name).cloneWrapper());
		for(String varId : this.m_matchers.keySet())    operand.m_matchers.put(varId, this.m_matchers.get(varId).cloneMatchers());
		operand.m_referredExpressionExeId = this.m_referredExpressionExeId;
	}
	
}
