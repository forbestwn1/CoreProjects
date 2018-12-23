package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteEmbededExpression;
import com.nosliw.data.core.script.expression.HAPContextScriptExpressionProcess;
import com.nosliw.data.core.script.expression.HAPEmbededScriptExpression;

public class HAPProcessorContextSolidate {

	static public HAPContextGroup process(
			HAPContextGroup originalContextGroup,
			HAPEnvContextProcessor contextProcessorEnv){
		//find all constants
		Map<String, Object> constantsData = buildConstants(originalContextGroup);

		HAPContextGroup out = new HAPContextGroup(originalContextGroup.getInfo());
		for(String categary : HAPContextGroup.getAllContextTypes()) {
			Map<String, HAPContextDefinitionRoot> contextDefRoots = originalContextGroup.getElements(categary);
			for(String name : contextDefRoots.keySet()) {
				HAPContextDefinitionRoot contextDefRoot = contextDefRoots.get(name);
				if(!contextDefRoot.isConstant()) {
					String solidName = getSolidName(name, constantsData, contextProcessorEnv);
					contextDefRoot.setDefinition(contextDefRoot.getDefinition().toSolidContextDefinitionElement(constantsData, contextProcessorEnv));
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
	public static String getSolidName(String name, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv){
		HAPEmbededScriptExpression se = new HAPEmbededScriptExpression(name);
		if(se.isString())  return name;
		else {
			HAPContextScriptExpressionProcess expProcessContext = new HAPContextScriptExpressionProcess();
			for(String constantName : constants.keySet()) {
				HAPDataWrapper constantData = HAPDataUtility.buildDataWrapperFromObject(constants.get(constantName));
				if(constantData!=null)   expProcessContext.addConstant(constantName, constantData);
			}
			se.processExpressions(expProcessContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), contextProcessorEnv.expressionManager);
			HAPRuntimeTaskExecuteEmbededExpression task = new HAPRuntimeTaskExecuteEmbededExpression(se, null, constants);
			HAPServiceData serviceData = contextProcessorEnv.runtime.executeTaskSync(task);
			if(serviceData.isSuccess())   return (String)serviceData.getData();
			else{
				System.err.println("Fail to solidate name : " + name);
				return null;
			}
		}
	}

}
