package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;

@HAPEntityWithAttribute
public class HAPExecutableExpressionItem extends HAPExecutableImp{

	@HAPAttribute
	public static String OPERAND = "operand";
	
	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";
	
	private HAPOperandWrapper m_operand;

	private HAPMatchers m_outputMatchers;
	
	private Map<String, HAPVariableInfo> m_varInfos;
	
	public HAPExecutableExpressionItem(HAPOperandWrapper operand) {
		this.m_operand = operand;
		this.m_varInfos = new LinkedHashMap<String, HAPVariableInfo>();
	}
	
	public HAPOperandWrapper getOperand() {		return this.m_operand;	}

	public HAPDataTypeCriteria getOutputCriteria() {  return this.m_operand.getOperand().getOutputCriteria(); }
	
	public HAPMatchers getOutputMatchers() {		return this.m_outputMatchers;	}
	public void setOutputMatchers(HAPMatchers matchers) {    this.m_outputMatchers = matchers;    }

	public Map<String, HAPVariableInfo> getVariablesInfo(){   return this.m_varInfos;    }
	public void setVariablesInfo(Map<String, HAPVariableInfo> varsInfo) {   this.m_varInfos.putAll(varsInfo);     }
	
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
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		//get converter resource id from var converter in expression 
		HAPMatchers matchers = this.getOutputMatchers();
		if(matchers!=null){
			dependency.addAll(matchers.getResourceDependency(runtimeInfo, resourceManager));
		}
		
		HAPOperandUtility.processAllOperand(this.getOperand(), dependency, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				List<HAPResourceDependency> dependency = (List<HAPResourceDependency>)data;
				List<HAPResourceId> operandResourceIds = (List)operand.getOperand().getResources();
				List<HAPResourceInfo> resourceInfos = resourceManager.discoverResources(operandResourceIds, runtimeInfo);
				for(HAPResourceInfo resourceInfo : resourceInfos) {
					dependency.addAll(resourceInfo.getDependency());
				}
				return true;
			}
		});
	}

	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(OPERAND, HAPSerializeManager.getInstance().toStringValue(this.getOperand(), HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(this.getOutputMatchers(), HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEINFOS, HAPJsonUtility.buildJson(this.m_varInfos, HAPSerializationFormat.JSON));
	}
}
