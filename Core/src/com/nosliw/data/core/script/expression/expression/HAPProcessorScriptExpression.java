package com.nosliw.data.core.script.expression.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;
import com.nosliw.data.core.expression.HAPParserExpression;
import com.nosliw.data.core.script.expression.HAPDefinitionScript;
import com.nosliw.data.core.script.expression.HAPScriptInScriptExpression;

public class HAPProcessorScriptExpression {

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	public static HAPExecutableScriptExpression process(
			String id,
			HAPDefinitionScript scriptDef,
			Map<String, Object> constantValues,
			HAPDefinitionExpressionGroup expressionDef,
			HAPParserExpression expressionParser 
		) {
		HAPExecutableScriptExpression out = new HAPExecutableScriptExpression();
		scriptDef.cloneToEntityInfo(out);
		List<Object> scriptSegs = parseScript(scriptDef.getScript());
		for(int j=0; j<scriptSegs.size(); j++) {
			Object scriptSeg = scriptSegs.get(j);
			if(scriptSeg instanceof HAPDefinitionDataExpression) {
				HAPDefinitionDataExpression expressionSeg = (HAPDefinitionDataExpression)scriptSeg;
				HAPDefinitionExpression expressionItem = new HAPDefinitionExpression();
				String expressionId = id+"_"+j;
				expressionItem.setName(expressionId);
				expressionItem.setOperand(expressionParser.parseExpression(expressionSeg.getExpression()));
				expressionDef.addExpression(expressionItem);
				out.addSegment(new HAPExecutableScriptSegExpression(expressionId));
			}
			else if(scriptSeg instanceof HAPScriptInScriptExpression) {
				HAPScriptInScriptExpression sSeg = (HAPScriptInScriptExpression)scriptSeg;
				HAPExecutableScriptSegScript scriptSegExe = new HAPExecutableScriptSegScript(sSeg.getScript());
				//update with constant value
//				Map<String, Object> cstValues = HAPUtilityDataComponent.buildConstantValue(scriptGroupDef.getAttachmentContainer());
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
	private static List<Object> parseScript(String script){
		List<Object> out = new ArrayList<Object>();
		String content = script;
		int i = 0;
		while(HAPBasicUtility.isStringNotEmpty(content)){
			int index = content.indexOf(EXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				out.add(new HAPScriptInScriptExpression(content));
				content = null;
			}
			else if(index!=0){
				//start with text
				HAPScriptInScriptExpression scriptSegment = new HAPScriptInScriptExpression(content.substring(0, index));
				out.add(scriptSegment);
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
				HAPDefinitionDataExpression expressionDefinition = new HAPDefinitionDataExpression(expressionStr);
				out.add(expressionDefinition);
			}
			i++;
		}
		return out;
	}
	

	
	
	
	
}
