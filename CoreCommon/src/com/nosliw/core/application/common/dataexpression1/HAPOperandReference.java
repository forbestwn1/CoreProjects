package com.nosliw.core.application.common.dataexpression1;

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
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.valueport.HAPIdElement;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPOperandReference extends HAPOperandImp{

	@HAPAttribute
	public static final String REFERENCE = "reference";
	
	@HAPAttribute
	public static final String REFDATAEXPRESSION = "referenceDataExpression";

	@HAPAttribute
	public static final String RESOURCEID = "resourceId";

	@HAPAttribute
	public static final String VARMAPPING = "varMapping";
	
	@HAPAttribute
	public static final String VARRESOLVE = "varResolve";
	
	@HAPAttribute
	public static final String VARMATCHERS = "varMatchers";
	
	//reference to expression (attachent name or resource id)
	private String m_reference;
	
	private HAPElementInLibraryDataExpression m_referedDataExpression;
	private HAPResourceId m_referedDataExpressionLibElementResourceId;
	
	//mapping from this expression to referenced expression variable (ref variable id path --  source operand)
	private Map<String, HAPWrapperOperand> m_variableMapping;
	private Map<String, HAPIdElement> m_resolvedVariable;
	private Map<String, HAPDataTypeCriteria> m_resolvedVariableCriteria;
	private Map<String, HAPMatchers> m_matchers;
	
	private HAPOperandReference(){
		super(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE);
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_variableMapping = new LinkedHashMap<String, HAPWrapperOperand>();
		this.m_resolvedVariable = new LinkedHashMap<String, HAPIdElement>();
		this.m_resolvedVariableCriteria = new LinkedHashMap<String, HAPDataTypeCriteria>();
	}
	
	public HAPOperandReference(String reference){
		this();
		this.m_reference = reference;
	}

	public String getReference(){  return this.m_reference;  }
	
	public void addMapping(String varName, HAPOperand operand) {	this.m_variableMapping.put(varName, this.createOperandWrapper(operand));	}
	public Map<String, HAPWrapperOperand> getMapping(){    return this.m_variableMapping;      }
	public void setMapping(Map<String, HAPWrapperOperand> mapping) {    
		this.m_variableMapping.clear();
		this.m_variableMapping.putAll(mapping);
	}

	public void addResolvedVariable(String varName, HAPIdElement varId, HAPDataTypeCriteria varCriteria) {	
		this.m_resolvedVariable.put(varName, varId);	
		this.m_resolvedVariableCriteria.put(varName, varCriteria);
	}
	
	public void setResourceId(HAPResourceId resourceId) {   this.m_referedDataExpressionLibElementResourceId = resourceId;     }
	
//	public void setReferencedDataExpression(HAPElementInLibraryScriptExpression referedDataExpression) {   this.m_referedDataExpression = referedDataExpression; }
//	public HAPElementInLibraryScriptExpression getReferencedDataExpression() {    return this.m_referedDataExpression;      }
	
	@Override
	public List<HAPWrapperOperand> getChildren(){
		List<HAPWrapperOperand> out = new ArrayList<HAPWrapperOperand>();
		out.addAll(this.m_variableMapping.values());
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
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		
		for(String name : this.m_variableMapping.keySet()) {
			HAPMatchers matchers = this.m_variableMapping.get(name).getOperand().discover(variablesInfo, this.m_resolvedVariableCriteria.get(name), processTracker, dataTypeHelper);
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
		jsonMap.put(RESOURCEID, this.m_referedDataExpressionLibElementResourceId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(REFDATAEXPRESSION, HAPUtilityJson.buildJson(this.m_referedDataExpression, HAPSerializationFormat.JSON));
		jsonMap.put(VARMAPPING, HAPUtilityJson.buildJson(this.m_variableMapping, HAPSerializationFormat.JSON));
		jsonMap.put(VARRESOLVE, HAPUtilityJson.buildJson(this.m_resolvedVariable, HAPSerializationFormat.JSON));
		jsonMap.put(VARMATCHERS, HAPUtilityJson.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
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
		for(String name : this.m_variableMapping.keySet()) {
			operand.m_variableMapping.put(name, this.m_variableMapping.get(name).cloneWrapper());
		}
		for(String varId : this.m_matchers.keySet()) {
			operand.m_matchers.put(varId, this.m_matchers.get(varId).cloneMatchers());
		}
	}
	
}
