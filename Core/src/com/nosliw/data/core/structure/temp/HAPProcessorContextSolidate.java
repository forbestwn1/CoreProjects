package com.nosliw.data.core.structure.temp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valuecontext.HAPWrapperExecutableValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression1.HAPUtilityScriptExpression1;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPProcessorContextSolidate {

	
	
	private static Map<String, Object> buildConstants(List<HAPWrapperExecutableValueStructure> valueStructureGroup, HAPDomainValueStructure valueStructureDomain){
		Map<String, Object> constantsData = new LinkedHashMap<String, Object>();
		for(HAPWrapperExecutableValueStructure valueStructureWrapper : valueStructureGroup) {
			HAPDefinitionEntityValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
			for(String rootName : valueStructure.getRootNames()) {
				HAPRootStructure rootNode = valueStructure.getRootByName(rootName);
				if(rootNode.isConstant()){
					HAPElementStructureLeafConstant constEleDef = (HAPElementStructureLeafConstant)rootNode.getDefinition();
					constantsData.put(name, constEleDef.getValue());
//					constantsData.put(new HAPIdContextDefinitionRoot(categarys[i], name).toString(), constEleDef.getValue());
				}
			}
		}
		
		
		
		String[] categarys = HAPValueStructureDefinitionGroup.getAllCategaries(); 
		for(int i=categarys.length-1; i>=0; i--) {
			Map<String, HAPRootStructure> nodes = originalContextGroup.getRootsByCategary(categarys[i]);
			for(String name : nodes.keySet()) {
				if(nodes.get(name).isConstant()){
					HAPElementStructureLeafConstant constEleDef = (HAPElementStructureLeafConstant)nodes.get(name).getDefinition();
					constantsData.put(name, constEleDef.getValue());
//					constantsData.put(new HAPIdContextDefinitionRoot(categarys[i], name).toString(), constEleDef.getValue());
				}
			}
		}
		return constantsData;
	}

	
	static public HAPValueStructureDefinitionGroup process(
			HAPValueStructureDefinitionGroup originalContextGroup,
			HAPRuntimeEnvironment runtimeEnv){
		//find all constants
		Map<String, Object> constantsData = buildConstants(originalContextGroup);

		HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup(originalContextGroup.getInfo());
		for(String categary : HAPValueStructureDefinitionGroup.getAllCategaries()) {
			Map<String, HAPRootStructure> contextDefRoots = originalContextGroup.getRootsByCategary(categary);
			for(String name : contextDefRoots.keySet()) {
				HAPRootStructure contextDefRoot = contextDefRoots.get(name);
				if(!contextDefRoot.isConstant()) {
					String solidName = HAPUtilityScriptExpression1.solidateLiterate(name, constantsData, runtimeEnv);
					contextDefRoot.setDefinition(contextDefRoot.getDefinition().solidateConstantScript(constantsData, runtimeEnv));
					out.addRoot(solidName, contextDefRoot, categary);
				}
				else {
					out.addRoot(name, contextDefRoot, categary);
				}
			}
		}
		return out;
	}

	private static Map<String, Object> buildConstants(HAPValueStructureDefinitionGroup originalContextGroup){
		Map<String, Object> constantsData = new LinkedHashMap<String, Object>();
		String[] categarys = HAPValueStructureDefinitionGroup.getAllCategaries(); 
		for(int i=categarys.length-1; i>=0; i--) {
			Map<String, HAPRootStructure> nodes = originalContextGroup.getRootsByCategary(categarys[i]);
			for(String name : nodes.keySet()) {
				if(nodes.get(name).isConstant()){
					HAPElementStructureLeafConstant constEleDef = (HAPElementStructureLeafConstant)nodes.get(name).getDefinition();
					constantsData.put(name, constEleDef.getValue());
//					constantsData.put(new HAPIdContextDefinitionRoot(categarys[i], name).toString(), constEleDef.getValue());
				}
			}
		}
		return constantsData;
	}
	
	//evaluate embeded script expression
//	public static String getSolidName(String name, Map<String, Object> constants, HAPRequirementContextProcessor contextProcessRequirement){
//		HAPDefinitionEmbededScriptExpression embededScriptExpDef = new HAPDefinitionEmbededScriptExpression(name);
//		if(embededScriptExpDef.isString())  return name;
//		else {
//			HAPContextProcessScriptExpression expProcessContext = new HAPContextProcessScriptExpression();
//			for(String constantName : constants.keySet()) {
//				HAPDataWrapper constantData = HAPUtilityData.buildDataWrapperFromObject(constants.get(constantName));
//				if(constantData!=null)   expProcessContext.addConstant(constantName, constantData);
//			}
//			HAPEmbededScriptExpression embededScriptExp = HAPProcessorScript.processEmbededScriptExpression(embededScriptExpDef, expProcessContext, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), contextProcessRequirement.expressionManager, contextProcessRequirement.runtime);
//			HAPRuntimeTaskExecuteEmbededExpression task = new HAPRuntimeTaskExecuteEmbededExpression(embededScriptExp, null, constants);
//			HAPServiceData serviceData = contextProcessRequirement.runtime.executeTaskSync(task);
//			if(serviceData.isSuccess())   return (String)serviceData.getData();
//			else{
//				System.err.println("Fail to solidate name : " + name);
//				return null;
//			}
//		}
//	}

}
