package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScript;
import com.nosliw.data.core.script.expression.HAPExecutableScriptEntity;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression.HAPProcessorScript;

public class HAPProcessorContextSolidate {

	static public HAPContextGroup process(
			HAPContextGroup originalContextGroup,
			HAPRuntimeEnvironment runtimeEnv){
		//find all constants
		Map<String, Object> constantsData = buildConstants(originalContextGroup);

		HAPContextGroup out = new HAPContextGroup(originalContextGroup.getInfo());
		for(String categary : HAPContextGroup.getAllContextTypes()) {
			Map<String, HAPContextDefinitionRoot> contextDefRoots = originalContextGroup.getElements(categary);
			for(String name : contextDefRoots.keySet()) {
				HAPContextDefinitionRoot contextDefRoot = contextDefRoots.get(name);
				if(!contextDefRoot.isConstant()) {
					String solidName = getSolidName(name, constantsData, runtimeEnv);
					contextDefRoot.setDefinition(contextDefRoot.getDefinition().toSolidContextDefinitionElement(constantsData, runtimeEnv));
					out.addElement(solidName, contextDefRoot, categary);
				}
				else {
					out.addElement(name, contextDefRoot, categary);
				}
			}
		}
		return out;
	}

	private static Map<String, Object> buildConstants(HAPContextGroup originalContextGroup){
		Map<String, Object> constantsData = new LinkedHashMap<String, Object>();
		String[] categarys = HAPContextGroup.getAllContextTypes(); 
		for(int i=categarys.length-1; i>=0; i--) {
			Map<String, HAPContextDefinitionRoot> nodes = originalContextGroup.getElements(categarys[i]);
			for(String name : nodes.keySet()) {
				if(nodes.get(name).isConstant()){
					HAPContextDefinitionLeafConstant constEleDef = (HAPContextDefinitionLeafConstant)nodes.get(name).getDefinition();
					constantsData.put(name, constEleDef.getValue());
					constantsData.put(new HAPContextDefinitionRootId(categarys[i], name).toString(), constEleDef.getValue());
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

	public static String getSolidName(String name, Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableScriptGroup groupExe = HAPProcessorScript.processSimpleScript(name, null, null, constants, runtimeEnv.getExpressionManager(), HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv, new HAPProcessTracker());
		HAPExecutableScriptEntity scriptExe = groupExe.getScript(null);
		
		String scriptType = scriptExe.getScriptType();
		//if pure data
		if(HAPConstant.SCRIPT_TYPE_TEXT.equals(scriptType))  return name;
		
		//execute script expression
		HAPRuntimeTaskExecuteScript task = new HAPRuntimeTaskExecuteScript(groupExe, null, null, null);
		HAPServiceData out = runtimeEnv.getRuntime().executeTaskSync(task);
		return (String)out.getData();
	}

}
