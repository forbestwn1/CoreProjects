package com.nosliw.data.core.operand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPOperandReference extends HAPOperandImp{

	@HAPAttribute
	public static final String REFERENCENAME = "referenceName";
	
	@HAPAttribute
	public static final String EXPRESSION = "expression";
	
	@HAPAttribute
	public static final String ELEMENTNAME = "elementName";
	
	@HAPAttribute
	public static final String VARMATCHERS = "varMatchers";
	
	@HAPAttribute
	public static final String VARMAPPING = "varMapping";
	
	private String m_referenceName;
	
	//operation parms
	protected Map<String, HAPOperandWrapper> m_mapping = new LinkedHashMap<String, HAPOperandWrapper>();

	private HAPExecutableExpressionGroup m_expression;
	
	private String m_elementName;
	
	private Map<String, HAPMatchers> m_matchers;
	
	private HAPOperandReference(){
		super(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE);
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.setElementName(null);
	}
	
	public HAPOperandReference(String expressionName){
		this();
		this.m_referenceName = expressionName;
	}

	public String getReferenceName(){  return this.m_referenceName;  }
	
	public void addMapping(String varName, HAPOperand operand) {	this.m_mapping.put(varName, this.createOperandWrapper(operand));	}
	public Map<String, HAPOperandWrapper> getMapping(){    return this.m_mapping;      }
	public void setMapping(Map<String, HAPOperandWrapper> mapping) {    
		this.m_mapping.clear();
		this.m_mapping.putAll(mapping);
	}

	public HAPExecutableExpressionGroup getReferedExpression() {   return this.m_expression;   }
	public void setReferedExpression(HAPExecutableExpressionGroup expression) {   this.m_expression = expression;    }

	public String getElementName() {   return this.m_elementName;   }
	public void setElementName(String name) {   
		this.m_elementName = name;    
		if(this.m_elementName==null)  this.m_elementName = HAPConstantShared.NAME_DEFAULT;
	}
	
	@Override
	public List<HAPOperandWrapper> getChildren(){
		List<HAPOperandWrapper> out = new ArrayList<HAPOperandWrapper>();
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
	public List<HAPResourceIdSimple> getResources() {
		List<HAPResourceIdSimple> out = super.getResources();
//		List<HAPResourceId> referenceResources = this.m_referencedTask.getResourceDependency(); 
//		out.addAll(referenceResources);
		return out;
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCENAME, m_referenceName);
//		jsonMap.put(EXPRESSION, this.m_expression.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTNAME, this.m_elementName);
		jsonMap.put(VARMATCHERS, HAPJsonUtility.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
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
		this.m_expression.discover(cs, dataTypeHelper, processTracker);

		//variable
		
		HAPContainerVariableCriteriaInfo internalVariablesInfo = this.m_expression.getVariablesInfo();
		for(String inVarId : this.m_mapping.keySet()) {
			HAPMatchers matchers = this.m_mapping.get(inVarId).getOperand().discover(variablesInfo, internalVariablesInfo.getVariableCriteriaInfo(inVarId).getCriteria(), processTracker, dataTypeHelper);
			this.m_matchers.put(inVarId, matchers);
		}
		
		//output
		HAPDataTypeCriteria outputCriteria = this.m_expression.getExpressionItems().get(this.m_elementName).getOutputCriteria();
		return HAPCriteriaUtility.isMatchable(outputCriteria, expectCriteria, dataTypeHelper);
	}
	
	@Override
	public HAPOperand cloneOperand() {
		HAPOperandReference out = new HAPOperandReference();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandReference operand){
		super.cloneTo(operand);
		operand.m_referenceName = this.m_referenceName;
	}
	
}
