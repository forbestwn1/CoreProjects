package com.nosliw.data.core.structure.temp;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementLeafConstant;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPProcessorContextSolidate {

	static public HAPValueStructureDefinitionGroup process(
			HAPValueStructureDefinitionGroup originalContextGroup,
			HAPRuntimeEnvironment runtimeEnv){
		//find all constants
		Map<String, Object> constantsData = buildConstants(originalContextGroup);

		HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup(originalContextGroup.getInfo());
		for(String categary : HAPValueStructureDefinitionGroup.getAllCategaries()) {
			Map<String, HAPRoot> contextDefRoots = originalContextGroup.getRootsByCategary(categary);
			for(String name : contextDefRoots.keySet()) {
				HAPRoot contextDefRoot = contextDefRoots.get(name);
				if(!contextDefRoot.isConstant()) {
					String solidName = solidateLiterate(name, constantsData, runtimeEnv);
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
			Map<String, HAPRoot> nodes = originalContextGroup.getRootsByCategary(categarys[i]);
			for(String name : nodes.keySet()) {
				if(nodes.get(name).isConstant()){
					HAPElementLeafConstant constEleDef = (HAPElementLeafConstant)nodes.get(name).getDefinition();
					constantsData.put(name, constEleDef.getValue());
					constantsData.put(new HAPIdContextDefinitionRoot(categarys[i], name).toString(), constEleDef.getValue());
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
