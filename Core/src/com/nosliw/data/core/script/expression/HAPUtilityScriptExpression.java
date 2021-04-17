package com.nosliw.data.core.script.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.script.expression.literate.HAPUtilityScriptLiterate;

public class HAPUtilityScriptExpression {

	public static Set<HAPVariableInfo> getDataVariables(HAPExecutableScriptGroup scriptExpressionGroup, String scriptEleName){
		HAPExecutableScriptEntity scriptExe = scriptExpressionGroup.getScript(scriptEleName);
		HAPContainerVariableCriteriaInfo varInfos = scriptExe.discoverVariablesInfo(scriptExpressionGroup.getExpression());
		Set<HAPVariableInfo> out = new HashSet<HAPVariableInfo>();
		for(String name : varInfos.keySet()) {
			HAPVariableInfo varInfo = HAPVariableInfo.buildVariableInfo(name, varInfos.get(name).getCriteria());
			out.add(varInfo);
		}
		return out;
	}
	
	public static HAPScript newScript(String content) {
		String type = null;
		String script = null;
		if(HAPUtilityScriptLiterate.isText(content)) {
			type = HAPConstantShared.SCRIPT_TYPE_TEXT;
			script = content;
		}
		else {
			List<HAPScript> iterateSegs = HAPUtilityScriptLiterate.parseScriptLiterate(content);
			if(iterateSegs.size()==1) {
				type = HAPConstantShared.SCRIPT_TYPE_EXPRESSION;
				script = iterateSegs.get(0).getScript();
			}
			else {
				type = HAPConstantShared.SCRIPT_TYPE_LITERATE;
				script = content;
			}
		}
		return HAPScript.newScript(script, type);
	}
	
	public static void addConstantDefinition(Map<String, HAPDefinitionConstant> to, Set<HAPDefinitionConstant> from) {
		for(HAPDefinitionConstant def : from) {
			addConstantDefinition(to, def);
		}
	}
	
	public static void addConstantDefinition(Map<String, HAPDefinitionConstant> constantDefinitions, HAPDefinitionConstant constantDefinition) {
		HAPDefinitionConstant existing = constantDefinitions.get(constantDefinition.getId());
		if(existing==null)  constantDefinitions.put(constantDefinition.getId(), constantDefinition);
	}

	public static void addVariableInfo(Map<String, HAPInfoCriteria> variableInfos, HAPInfoCriteria variableInfo, String varName) {
		HAPInfoCriteria existing = variableInfos.get(varName);
		if(existing==null)   variableInfos.put(varName, variableInfo);
		else {
			if(existing.getCriteria()==null && variableInfo.getCriteria()!=null) {
				existing.setCriteria(variableInfo.getCriteria());
			}
		}
	}

	public static Map<String, HAPData> getConstantData(Map<String, Object> constantsValue){
		Map<String, HAPData> constantsData = new LinkedHashMap<String, HAPData>();
		for(String name : constantsValue.keySet()) {
			if(constantsValue.get(name) instanceof HAPData) {
				constantsData.put(name, (HAPData)constantsValue.get(name));
			}
		}
		return constantsData;
	}
	
	//process variables in expression, 
	//	for attribute operation a.b.c.d which have responding definition in context, 
	//			replace attribute operation with one variable operation
	//  for attribute operation a.b.c.d which have responding definition a.b.c in context, 
	//			replace attribute operation with one variable operation(a.b.c) and getChild operation
//	public static void processAttributeOperandInExpression(HAPResourceDefinitionExpressionGroup expressionDefinition, final Map<String, HAPVariableInfo> varsInfo){
//		HAPOperandUtility.processAttributeOperandInExpressionOperand(expressionDefinition.getOperand(), varsInfo);
//	}
	

}

