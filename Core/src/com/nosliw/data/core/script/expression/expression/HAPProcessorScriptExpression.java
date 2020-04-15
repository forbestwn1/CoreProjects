package com.nosliw.data.core.script.expression.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptEntity;
import com.nosliw.data.core.script.expression.HAPExecutableScript;
import com.nosliw.data.core.script.expression.HAPScript;

public class HAPProcessorScriptExpression {

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	public static HAPExecutableScriptExpression process(
			String id,
			HAPDefinitionScriptEntity scriptDef,
			Map<String, Object> constantValues,
			HAPDefinitionExpressionGroup expressionDef
		) {
		HAPExecutableScriptExpression out = new HAPExecutableScriptExpression(id);
		scriptDef.cloneToEntityInfo(out);
		out.addSegments(process(id, scriptDef.getScript(), constantValues, expressionDef));
		return out;
	}
	
	public static List<HAPExecutableScript> process(
			String id,
			HAPScript script,
			Map<String, Object> constantValues,
			HAPDefinitionExpressionGroup expressionGroup
		) {
		List<HAPExecutableScript> out = new ArrayList<HAPExecutableScript>();
		List<HAPScript> scriptSegs = parseScript(script.getScript());
		for(int j=0; j<scriptSegs.size(); j++) {
			HAPScript scriptSeg = scriptSegs.get(j);
			String scriptType = scriptSeg.getType();
			String scriptId = id+"_"+j;
			if(HAPConstant.SCRIPT_TYPE_SEG_EXPRESSION.equals(scriptType)) {
				HAPDefinitionExpression expressionItem = new HAPDefinitionExpression(scriptSeg.getScript());
				expressionItem.setName(scriptId);
				expressionGroup.addEntityElement(expressionItem);
				out.add(new HAPExecutableScriptSegExpression(scriptId, scriptId));
			}
			else if(HAPConstant.SCRIPT_TYPE_SEG_SCRIPT.equals(scriptType)) {
				HAPExecutableScriptSegScript scriptSegExe = new HAPExecutableScriptSegScript(scriptId, scriptSeg.getScript());
				//update with constant value
				scriptSegExe.updateConstantValue(constantValues);
				out.add(scriptSegExe);
			}
		}
		return out;
	}
	
	
	
//	public boolean isDataExpression() {
//		if(this.m_segments.size()==1 && this.m_segments.get(0) instanceof HAPDefinitionDataExpression) return true;
//		return false;
//	}
//	
//	public List<HAPDefinitionDataExpression> getExpressionDefinitions(){
//		List<HAPDefinitionDataExpression> out = new ArrayList<HAPDefinitionDataExpression>();
//		for(Object element : this.m_segments){
//			if(element instanceof HAPDefinitionDataExpression){
//				out.add((HAPDefinitionDataExpression)element);
//			}
//		}
//		return out;
//	}
//
//	public List<HAPScriptInScriptExpression> getScriptSegments(){
//		List<HAPScriptInScriptExpression> out = new ArrayList<HAPScriptInScriptExpression>();
//		for(Object element : this.m_segments){
//			if(element instanceof HAPScriptInScriptExpression){
//				out.add((HAPScriptInScriptExpression)element);
//			}
//		}
//		return out;
//	}
	
	//parse definition to get segments
	private static List<HAPScript> parseScript(String script){
		List<HAPScript> out = new ArrayList<HAPScript>();
		String content = script;
		int i = 0;
		while(HAPBasicUtility.isStringNotEmpty(content)){
			int index = content.indexOf(EXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				out.add(HAPScript.newScript(content, HAPConstant.SCRIPT_TYPE_SEG_SCRIPT));
				content = null;
			}
			else if(index!=0){
				//start with text
				out.add(HAPScript.newScript(content.substring(0, index), HAPConstant.SCRIPT_TYPE_SEG_SCRIPT));
				content = content.substring(index);
			}
			else{
				//start with expression
				int expEnd = content.indexOf(EXPRESSION_TOKEN_CLOSE);
				int expStart = index + EXPRESSION_TOKEN_OPEN.length();
				//get expression element
				String expressionStr = content.substring(expStart, expEnd);
				content = content.substring(expEnd + EXPRESSION_TOKEN_CLOSE.length());
				//build expression definition
				out.add(HAPScript.newScript(expressionStr, HAPConstant.SCRIPT_TYPE_SEG_EXPRESSION));
			}
			i++;
		}
		return out;
	}
	
}
