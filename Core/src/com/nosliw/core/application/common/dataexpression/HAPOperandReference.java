package com.nosliw.core.application.common.dataexpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPOperandReference extends HAPOperandImp{

	@HAPAttribute
	public static final String REFERENCE = "reference";
	
	@HAPAttribute
	public static final String VARMAPPING = "varMapping";
	
	@HAPAttribute
	public static final String RESOLVED_VARIABLE = "resolvedVariable";
	
	@HAPAttribute
	public static final String RESOLVED_ELEMENT = "resolvedElement";
	
	@HAPAttribute
	public static final String VARMATCHERS = "varMatchers";
	
	//reference to expression (attachent name or resource id)
	private String m_reference;
	
	private HAPDataExpressionElementInLibrary m_referedDataExpression;
	
	//mapping from this expression to referenced expression variable (ref variable id path --  source operand)
	private Map<String, HAPWrapperOperand> m_mapping = new LinkedHashMap<String, HAPWrapperOperand>();

	private Map<String, HAPMatchers> m_matchers;
	
	
	
	
	//resolve map to reference info 
	private Map<String, HAPIdElement> m_resolvedVariable = new LinkedHashMap<String, HAPIdElement>();
	private Map<String, HAPElementStructureLeafData> m_resolvedElement = new LinkedHashMap<String, HAPElementStructureLeafData>();
	
	private HAPOperandReference(){
		super(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE);
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_mapping = new LinkedHashMap<String, HAPWrapperOperand>();
		this.m_resolvedVariable = new LinkedHashMap<String, HAPIdElement>();
		this.m_resolvedElement = new LinkedHashMap<String, HAPElementStructureLeafData>();
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

	public void addResolvedMappingTo(String name, HAPIdElement variableId, HAPElementStructureLeafData dataStructureEle) {
		this.m_resolvedVariable.put(name, variableId);
		this.m_resolvedElement.put(name, dataStructureEle);
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
	public HAPMatchers discover(
			HAPContainerVariableCriteriaInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		
		for(String name : this.m_mapping.keySet()) {
			HAPMatchers matchers = this.m_mapping.get(name).getOperand().discover(variablesInfo, this.m_resolvedElement.get(name).getCriteria(), processTracker, dataTypeHelper);
			if(matchers!=null) {
				this.m_matchers.put(name, matchers);
			}
		}
		
		return HAPUtilityCriteria.isMatchable(this.getOutputCriteria(), expectCriteria, dataTypeHelper);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCE, m_reference);
		jsonMap.put(VARMAPPING, HAPUtilityJson.buildJson(this.m_mapping, HAPSerializationFormat.JSON));
		jsonMap.put(VARMATCHERS, HAPUtilityJson.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
		jsonMap.put(RESOLVED_VARIABLE, HAPUtilityJson.buildJson(this.m_resolvedVariable, HAPSerializationFormat.JSON));
		jsonMap.put(RESOLVED_ELEMENT, HAPUtilityJson.buildJson(this.m_resolvedElement, HAPSerializationFormat.JSON));
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
		for(String name : this.m_mapping.keySet()) {
			operand.m_mapping.put(name, this.m_mapping.get(name).cloneWrapper());
		}
		for(String varId : this.m_matchers.keySet()) {
			operand.m_matchers.put(varId, this.m_matchers.get(varId).cloneMatchers());
		}
		for(String name : this.m_resolvedVariable.keySet()) {
			operand.m_resolvedVariable.put(name, this.m_resolvedVariable.get(name));
		}
		for(String name : this.m_resolvedElement.keySet()) {
			operand.m_resolvedElement.put(name, this.m_resolvedElement.get(name));
		}
	}
	
}
