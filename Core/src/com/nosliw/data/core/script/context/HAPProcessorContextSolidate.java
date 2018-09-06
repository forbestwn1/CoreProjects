package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.exception.HAPServiceDataException;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteEmbededExpression;
import com.nosliw.data.core.script.expressionscript.HAPEmbededScriptExpression;

public class HAPProcessorContextSolidate {

	static public HAPContextGroup process(
			HAPContextGroup originalContextGroup,
			HAPEnvContextProcessor contextProcessorEnv){
		//find all constants
		Map<String, Object> constantsData = buildConstants(originalContextGroup);

		HAPContextGroup out = new HAPContextGroup(originalContextGroup.getInfo());
		for(String categary : HAPContextGroup.getAllContextTypes()) {
			Map<String, HAPContextNodeRoot> nodes = originalContextGroup.getElements(categary);
			for(String name : nodes.keySet()) {
				HAPContextNodeRoot node = nodes.get(name);
				if(node.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE) || node.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE)) {
					String solidName = getSolidName(name, constantsData, contextProcessorEnv);
					out.addElement(solidName, node.toSolidContextNodeRoot(constantsData, contextProcessorEnv), categary);
				}
				else if(nodes.get(name).getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT)) {
					out.addElement(name, node, categary);
				}
			}
		}
		return out;
	}

	private static Map<String, Object> buildConstants(HAPContextGroup originalContextGroup){
		Map<String, Object> constantsData = new LinkedHashMap<String, Object>();
		String[] categarys = HAPContextGroup.getVisibleContextTypes(); 
		for(int i=categarys.length-1; i>=0; i--) {
			Map<String, HAPContextNodeRoot> nodes = originalContextGroup.getElements(categarys[i]);
			for(String name : nodes.keySet()) {
				if(nodes.get(name).getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT)){
					HAPContextNodeRootConstant constNode = (HAPContextNodeRootConstant)nodes.get(name);
					constantsData.put(name, constNode.getValue());
					constantsData.put(new HAPContextRootNodeId(categarys[i], name).toString(), constNode.getValue());
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
			HAPRuntimeTaskExecuteEmbededExpression task = new HAPRuntimeTaskExecuteEmbededExpression(se, null, constants);
			HAPServiceData serviceData = contextProcessorEnv.runtime.executeTaskSync(task);
			if(serviceData.isSuccess())   return (String)serviceData.getData();
			else{
				System.err.println("Fail to solidate name : " + name);
				return null;
			}
		}
	}

	//build context node with solid name
	//def : original node
	//solid : out, sold node
	public static void buildSolidContextNode(HAPContextNode def, HAPContextNode solid, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv){
		solid.setDefinition(def.getDefinition());
		for(String name : def.getChildren().keySet()){
			String solidName = getSolidName(name, constants, contextProcessorEnv);
			HAPContextNode child = def.getChildren().get(name);
			HAPContextNode solidChild = new HAPContextNode();
			buildSolidContextNode(child, solidChild, constants, contextProcessorEnv);
			solid.addChild(solidName, solidChild);
		}
	}
	
}
