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
	
	private Map<String, HAPVariableInfo> m_localVarsInfo;

	private Map<String, HAPMatchers> m_varsMatchers;
	
	private HAPDefinitionExpression m_definition;
	
	public HAPExecutableExpressionInSuite(HAPDefinitionExpression definition, String id, HAPOperand operand) {
		this.m_definition = definition;
		this.m_id = id;
		this.m_operand = new HAPOperandWrapper(operand);
		this.m_localVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		this.m_varsMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	@Override
	public HAPDefinitionExpression getDefinition() {   return this.m_definition;    }

	@Override
	public String getId() {  return this.m_id;  }
	public void setId(String id) {   this.m_id = id;    }
	
	@Override
	public HAPOperandWrapper getOperand() {		return this.m_operand;	}

	@Override
	public Map<String, HAPVariableInfo> getVarsInfo() {  return this.m_localVarsInfo;  }
	public void setVarsInfo(Map<String, HAPVariableInfo> varsInfo) {   this.m_localVarsInfo = varsInfo;  }

	@Override
	public void updateVariableName(HAPUpdateName nameUpdate) {
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
	public Map<String, HAPMatchers> getVariableMatchers() {		return this.m_varsMatchers;	}

	@Override
	public void discover(HAPDataTypeCriteria expectOutput, HAPProcessTracker processTracker) {
		Map<String, HAPVariableInfo> discoveredVarsInf = HAPOperandUtility.discover(new HAPOperand[]{this.getOperand().getOperand()}, this.m_localVarsInfo, expectOutput, processTracker);
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
