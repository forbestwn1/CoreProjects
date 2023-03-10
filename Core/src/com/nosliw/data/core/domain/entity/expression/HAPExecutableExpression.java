package com.nosliw.data.core.domain.entity.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPOperandConstant;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableExpression extends HAPExecutableImp{

	@HAPAttribute
	public static String OPERAND = "operand";
	
	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";
	
	private HAPOperandWrapper m_operand;

	private HAPMatchers m_outputMatchers;
	
	private HAPContainerVariableCriteriaInfo m_varInfos;
	
	public HAPExecutableExpression(HAPOperandWrapper operand) {
		this.m_operand = operand;
	}
	
	public HAPOperandWrapper getOperand() {		return this.m_operand;	}

	public HAPDataTypeCriteria getOutputCriteria() {  return this.m_operand.getOperand().getOutputCriteria(); }
	
	public HAPMatchers getOutputMatchers() {		return this.m_outputMatchers;	}
	public void setOutputMatchers(HAPMatchers matchers) {    this.m_outputMatchers = matchers;    }

	public HAPContainerVariableCriteriaInfo getVariablesInfo(){   return this.m_varInfos;    }
	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varsInfo) {   this.m_varInfos = varsInfo;     }

	public void updateConstant(Map<String, Object> value) {
		HAPOperandUtility.processAllOperand(this.m_operand, value, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)){
					Map<String, Object> value = (Map<String, Object>)data; 
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
					if(constantOperand.getName()!=null) {
						constantOperand.setData(HAPUtilityData.buildDataWrapperFromObject(value.get(constantOperand.getName())));
					}
				}
				return true;
			}
		});
	}
	
	public Set<HAPDefinitionConstant> getConstantsDefinition(){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();
		HAPOperandUtility.processAllOperand(this.m_operand, out, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)){
					Map<String, HAPDefinitionConstant> out = (Map<String, HAPDefinitionConstant>)data; 
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
					if(constantOperand.getName()!=null) {
						out.put(constantOperand.getName(), new HAPDefinitionConstant(constantOperand.getName(), constantOperand.getData()));
					}
				}
				return true;
			}
		});
		return new HashSet<HAPDefinitionConstant>(out.values());
	}
	
//	public void updateVariableName(HAPUpdateName nameUpdate) {
//		HAPOperandUtility.updateNameInOperand(this.m_operand, nameUpdate, new String[]{HAPConstantShared.EXPRESSION_OPERAND_VARIABLE});
//		HAPOperandUtility.processAllOperand(this.m_operand, null, new HAPOperandTask(){
//			@Override
//			public boolean processOperand(HAPOperandWrapper operand, Object data) {
//				String opType = operand.getOperand().getType();
//				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
//					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
//					HAPDefinitionDataAssociation inputMapping = referenceOperand.getInputMapping();
//					
//					String inputMappingType = inputMapping.getType();
//					if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
//						HAPDefinitionDataAssociationMapping mappingDa = (HAPDefinitionDataAssociationMapping)inputMapping;
//						HAPValueStructureDefinitionFlat da = mappingDa.getMapping();
//						da.updateReferenceName(nameUpdate);
//					}
//					else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR)) {
//					}
//					else if(inputMappingType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_NONE)) {
//					}
//				}
//				return true;
//			}
//		});
//	}
	
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
				List<HAPResourceId> operandResourceIds = (List)operand.getOperand().getResources(runtimeInfo, resourceManager);
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
		jsonMap.put(OUTPUTMATCHERS, HAPUtilityJson.buildJson(this.getOutputMatchers(), HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEINFOS, HAPUtilityJson.buildJson(this.m_varInfos, HAPSerializationFormat.JSON));
	}
}
