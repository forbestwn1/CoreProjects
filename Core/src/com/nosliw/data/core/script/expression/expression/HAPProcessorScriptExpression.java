package com.nosliw.data.core.script.expression.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;
import com.nosliw.data.core.expression.HAPParserExpression;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptEntity;
import com.nosliw.data.core.script.expression.HAPScript;

public class HAPProcessorScriptExpression {

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	public static HAPExecutableScriptExpression process(
			String id,
			HAPDefinitionScriptEntity scriptDef,
			Map<String, Object> constantValues,
			HAPDefinitionExpressionGroup expressionDef,
			HAPParserExpression expressionParser 
		) {
		HAPExecutableScriptExpression out = new HAPExecutableScriptExpression();
		scriptDef.cloneToEntityInfo(out);
		List<HAPScript> scriptSegs = parseScript(scriptDef.getScript().getScript());
		for(int j=0; j<scriptSegs.size(); j++) {
			HAPScript scriptSeg = scriptSegs.get(j);
			String scriptType = scriptSeg.getType();
			if(HAPScript.SCRIPT_TYPE_EXPRESSION.equals(scriptType)) {
				HAPDefinitionExpression expressionItem = new HAPDefinitionExpression();
				String expressionId = id+"_"+j;
				expressionItem.setName(expressionId);
				expressionItem.setOperand(expressionParser.parseExpression(scriptSeg.getScript()));
				expressionDef.addExpression(expressionItem);
				out.addSegment(new HAPExecutableScriptSegExpression(expressionId));
			}
			else if(HAPScript.SCRIPT_TYPE_SCRIPT.equals(scriptType)) {
				HAPExecutableScriptSegScript scriptSegExe = new HAPExecutableScriptSegScript(scriptSeg.getScript());
				//update with constant value
				scriptSegExe.updateConstantValue(constantValues);
				out.addSegment(scriptSegExe);
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
				out.add(HAPScript.newScript(content, HAPScript.SCRIPT_TYPE_SCRIPT));
				content = null;
			}
			else if(index!=0){
				//start with text
				out.add(HAPScript.newScript(content.substring(0, index), HAPScript.SCRIPT_TYPE_SCRIPT));
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
				out.add(HAPScript.newScript(expressionStr, HAPScript.SCRIPT_TYPE_EXPRESSION));
			}
			i++;
		}
		return out;
	}
	
}
