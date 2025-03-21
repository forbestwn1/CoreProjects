package com.nosliw.core.application.common.dataexpression1;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPDataExpression extends HAPExecutableImp{

	@HAPAttribute
	public static String OPERAND = "operand";
	
	@HAPAttribute
	public static String VARIABLEKEYS = "variableKeys";
	
	private HAPWrapperOperand m_operand;

	private Set<String> m_varKeys = new HashSet<String>();

	public HAPDataExpression(HAPWrapperOperand operand) {
		this.m_operand = operand;
	}
	
	public HAPWrapperOperand getOperand() {		return this.m_operand;	}

	public HAPDataTypeCriteria getOutputCriteria() {  return this.m_operand.getOperand().getOutputCriteria(); }
	
	public Set<String> getVariablesInfo(){   return this.m_varKeys;    }
	public void addVariableKey(String key) {   this.m_varKeys.add(key);    }

	public void updateConstant(Map<String, Object> value) {
		HAPUtilityOperand.processAllOperand(this.m_operand, value, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
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
		HAPUtilityOperand.processAllOperand(this.m_operand, out, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
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
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		HAPUtilityOperand.processAllOperand(this.getOperand(), dependency, new HAPInterfaceProcessOperand(){
			@Override
			public boolean processOperand(HAPWrapperOperand operand, Object data) {
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
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OPERAND, HAPManagerSerialize.getInstance().toStringValue(this.getOperand(), HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEKEYS, HAPUtilityJson.buildJson(this.m_varKeys, HAPSerializationFormat.JSON));
	}
}
