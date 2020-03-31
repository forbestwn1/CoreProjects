package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;

@HAPEntityWithAttribute(baseName="EXPRESSION")
public class HAPExecutableExpressionInSuite extends HAPExecutableExpressionImp{

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	private String m_id;
	
	private HAPOperandWrapper m_operand;

	private HAPContext m_context;
	
	private Map<String, HAPVariableInfo> m_localVarsInfo;

	private HAPMatchers m_outputMatchers;
	
	private HAPDefinitionExpression m_definition;
	
	public HAPExecutableExpressionInSuite(HAPDefinitionExpression definition, String id, HAPOperand operand) {
		this.m_definition = definition;
		this.m_id = id;
		this.m_operand = new HAPOperandWrapper(operand);
		this.m_localVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
	}
	
	@Override
	public HAPDefinitionExpression getDefinition() {   return this.m_definition;    }

	@Override
	public String getId() {  return this.m_id;  }
	public void setId(String id) {   this.m_id = id;    }
	
	@Override
	public HAPOperandWrapper getOperand() {		return this.m_operand;	}

	@Override
	public HAPContext getContext() {   return this.m_context;  }

	@Override
	public void setContext(HAPContext context) {   this.m_context = context;  }
	
	@Override
	public Map<String, HAPVariableInfo> getVarsInfo() {  return this.m_localVarsInfo;  }
	public void setVarsInfo(Map<String, HAPVariableInfo> varsInfo) {   this.m_localVarsInfo = varsInfo;  }

	@Override
	public HAPDataTypeCriteria getOutputCriteria() {  return this.m_operand.getOperand().getOutputCriteria(); }
	
	@Override
	public HAPMatchers getOutputMatchers() {		return this.m_outputMatchers;	}

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
		
		HAPOperandUtility.updateVariableName(this.m_operand, nameUpdate);
		HAPOperandUtility.processAllOperand(this.m_operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					HAPDefinitionDataAssociation inputMapping = referenceOperand.getInputMapping();
					
					String inputMappingType = inputMapping.getType();
					if(inputMappingType.equals(HAPConstant.DATAASSOCIATION_TYPE_MAPPING)) {
						HAPDefinitionDataAssociationMapping mappingDa = (HAPDefinitionDataAssociationMapping)inputMapping;
						HAPContext da = mappingDa.getAssociation();
						da.updateReferenceName(nameUpdate);
					}
					else if(inputMappingType.equals(HAPConstant.DATAASSOCIATION_TYPE_MIRROR)) {
					}
					else if(inputMappingType.equals(HAPConstant.DATAASSOCIATION_TYPE_NONE)) {
					}
				}
				return true;
			}
		});
	}
	
	@Override
	public void discover(HAPDataTypeCriteria expectOutput, HAPProcessTracker processTracker) {
		Map<String, HAPVariableInfo> discoveredVarsInf = new LinkedHashMap<String, HAPVariableInfo>();
		HAPMatchers[] matchers = new HAPMatchers[1]; 
		HAPOperandUtility.discover(
				new HAPOperand[]{this.getOperand().getOperand()},
				new HAPDataTypeCriteria[] {expectOutput},
				this.m_localVarsInfo,
				discoveredVarsInf,
				matchers,
				processTracker);
		this.m_outputMatchers = matchers[0];
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
		HAPExecutableExpression.buildJsonMap(this, jsonMap, typeJsonMap);
		jsonMap.put(VARIABLEINFOS, HAPJsonUtility.buildJson(this.m_localVarsInfo, HAPSerializationFormat.JSON));

	}
}
