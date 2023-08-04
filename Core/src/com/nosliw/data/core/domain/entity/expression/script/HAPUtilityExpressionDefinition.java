package com.nosliw.data.core.domain.entity.expression.script;

import java.lang.reflect.Constructor;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionExpressionData;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.domain.entity.expression.data.HAPUtilityDataExpressionDefinition;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.script.expression.imp.expression.HAPConstantInScript;
import com.nosliw.data.core.script.expression.imp.expression.HAPVariableInScript;

public class HAPUtilityExpressionDefinition {

	public static final String LITERATE_TOKEN_OPEN = "<%=";
	public static final String LITERATE_TOKEN_CLOSE = "%>";

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	public static boolean isText(String script) {
		return script.indexOf(LITERATE_TOKEN_OPEN)==-1;
	}

	public static HAPDefinitionExpression parseDefinitionExpression(String content, String scriptType, HAPDefinitionEntityExpressionDataGroup dataExpressionGroup, HAPParserDataExpression expressionParser) {
		HAPDefinitionExpression out = null;
		
		if(scriptType==null) {
			if(content.indexOf(LITERATE_TOKEN_OPEN)==-1) {
				scriptType = HAPConstantShared.EXPRESSION_TYPE_TEXT;
			}
			else if(content.indexOf(LITERATE_TOKEN_OPEN)==0&&content.indexOf(LITERATE_TOKEN_CLOSE)==content.length()-LITERATE_TOKEN_CLOSE.length()-1) {
				scriptType = HAPConstantShared.EXPRESSION_TYPE_SCRIPT;
			}
			else {
				scriptType = HAPConstantShared.EXPRESSION_TYPE_LITERATE;
			}
		}
		
		if(scriptType.equals(HAPConstantShared.EXPRESSION_TYPE_TEXT)) {
			out = parseDefinitionExpressionText(content);
		}
		else if(scriptType.equals(HAPConstantShared.EXPRESSION_TYPE_SCRIPT)) {
			out = parseDefinitionExpressionScript(content, dataExpressionGroup, expressionParser);
		}
		else if(scriptType.equals(HAPConstantShared.EXPRESSION_TYPE_LITERATE)) {
			out = parseDefinitionExpressionLiterate(content, dataExpressionGroup, expressionParser);
		}
		
		return out;
	}
	
	public static HAPDefinitionExpressionText parseDefinitionExpressionText(String content) {
		HAPDefinitionExpressionText out = new HAPDefinitionExpressionText();
		out.addSegmentText(parseDefinitionSegmentExpressionText(content));
		return out;
	}
	
	public static HAPDefinitionExpressionScript parseDefinitionExpressionScript(String content, HAPDefinitionEntityExpressionDataGroup dataExpressionGroup, HAPParserDataExpression expressionParser) {
		HAPDefinitionExpressionScript out = new HAPDefinitionExpressionScript();
		HAPDefinitionSegmentExpressionDataScript scriptSeg = parseDefinitionSegmentExpressionDataScript(content, dataExpressionGroup, expressionParser);
		out.addSegmentDataScript(scriptSeg);
		return out;
	}
	
	public static HAPDefinitionExpressionLiterate parseDefinitionExpressionLiterate(String script, HAPDefinitionEntityExpressionDataGroup dataExpressionGroup, HAPParserDataExpression expressionParser) {
		HAPDefinitionExpressionLiterate out = new HAPDefinitionExpressionLiterate();
		
		if(script!=null) {
			int start = script.indexOf(LITERATE_TOKEN_OPEN);
			while(start != -1){
				if(start>0)   out.addSegmentText(parseDefinitionSegmentExpressionText(script.substring(0, start)));
				int expEnd = script.indexOf(LITERATE_TOKEN_CLOSE, start);
				int end = expEnd + LITERATE_TOKEN_CLOSE.length();
				String expression = script.substring(start+LITERATE_TOKEN_OPEN.length(), expEnd);
				out.addSegmentDataScript(parseDefinitionSegmentExpressionDataScript(expression, dataExpressionGroup, expressionParser));
				//keep searching the rest
				script=script.substring(end);
				start = script.indexOf(LITERATE_TOKEN_OPEN);
			}
			if(!HAPUtilityBasic.isStringEmpty(script)){
				out.addSegmentText(parseDefinitionSegmentExpressionText(script));
			}
		}
		return out;
	}
	
	private static HAPDefinitionSegmentExpressionText parseDefinitionSegmentExpressionText(String content) {
		return new HAPDefinitionSegmentExpressionText(content);
	}

	private static HAPDefinitionSegmentExpressionScript parseDefinitionSegmentExpressionScript(String script) {
		HAPDefinitionSegmentExpressionScript out = new HAPDefinitionSegmentExpressionScript(script);
		parseExpressionScriptSegment(script, out);
		return out;
	}

	//define the segment parsing infor
	private static final Object[][] m_definitions = {
			{"&(", ")&", HAPConstantInScript.class}, 
			{"?(", ")?", HAPVariableInScript.class}
	};

	private static void parseExpressionScriptSegment(String orignalScript, HAPDefinitionSegmentExpressionScript expressionScriptSegment){
		try{
			String content = orignalScript;
			int[] indexs = indexScript(content);
			while(indexs[0]!=-1){
				int type = indexs[1];
				String startToken = (String)m_definitions[type][0];
				String endToken = (String)m_definitions[type][1];
				int startIndex = indexs[0];
				if(startIndex>0){
					expressionScriptSegment.addSegment(content.substring(0, startIndex));
				}
				int endIndex = content.indexOf(endToken);
				Class cs = (Class)m_definitions[type][2];
				Constructor cons = cs.getConstructor(String.class);
				String name = content.substring(startIndex+startToken.length(), endIndex);
				expressionScriptSegment.addSegment(cons.newInstance(name));
				content = content.substring(endIndex+endToken.length());
				indexs = indexScript(content);
			}
			if(HAPUtilityBasic.isStringNotEmpty(content)){
				expressionScriptSegment.addSegment(content);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static int[] indexScript(String content){
		int invalidValue = 99999;
		int[] indexs = new int[2];
		int currentIndex = invalidValue;
		int currentType = invalidValue;
		for(int i=0; i<m_definitions.length; i++){
			int index = content.indexOf((String)m_definitions[i][0]);
			if(index==-1)  continue;
			else if(index < currentIndex){
				currentIndex = index;
				currentType = i;
			}
		}
		if(currentIndex==invalidValue){
			indexs[0] = -1;
			indexs[1] = -1;
		}
		else{
			indexs[0] = currentIndex;
			indexs[1] = currentType;
		}
		return indexs;
	}

	
	private static HAPDefinitionSegmentExpressionDataScript parseDefinitionSegmentExpressionDataScript(String script, HAPDefinitionEntityExpressionDataGroup dataExpressionGroup, HAPParserDataExpression expressionParser) {
		HAPDefinitionSegmentExpressionDataScript out = new HAPDefinitionSegmentExpressionDataScript();
		String content = script;
		int i = 0;
		while(HAPUtilityBasic.isStringNotEmpty(content)){
			int index = content.indexOf(EXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				out.addSegmentScript(parseDefinitionSegmentExpressionScript(content));
				content = null;
			}
			else if(index!=0){
				//start with text
				out.addSegmentScript(parseDefinitionSegmentExpressionScript(content.substring(0, index)));
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
				out.addSegmentData(parseDefinitionSegmentExpressionData(expressionStr, dataExpressionGroup, expressionParser));
			}
			i++;
		}
		return out;
	}

	private static HAPDefinitionSegmentExpressionData parseDefinitionSegmentExpressionData(String dataExpressionScript, HAPDefinitionEntityExpressionDataGroup dataExpressionGroup, HAPParserDataExpression expressionParser) {
		HAPDefinitionExpressionData dataExpressionDef = HAPUtilityDataExpressionDefinition.parseExpressionDefinitionScript(dataExpressionScript, expressionParser);
		String dataExpressionId = dataExpressionGroup.addExpression(dataExpressionDef);
		return new HAPDefinitionSegmentExpressionData(dataExpressionId);
	}

	
	
	
	public static HAPDefinitionExpression parseExpressionDefinition(Object obj, HAPContextParser parserContext, HAPParserDataExpression expressionParser, HAPManagerResourceDefinition resourceDefMan) {
		HAPDefinitionExpression out = null;
		if(obj instanceof String) {
			
		}
		return out;
	}


}
