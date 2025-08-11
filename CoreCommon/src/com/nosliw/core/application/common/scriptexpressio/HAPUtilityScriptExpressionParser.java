package com.nosliw.core.application.common.scriptexpressio;

import java.lang.reflect.Constructor;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPExpressionData;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicUtilityProcessorDataExpression;

public class HAPUtilityScriptExpressionParser {

	public static final String LITERATE_TOKEN_OPEN = "<%=";
	public static final String LITERATE_TOKEN_CLOSE = "%>";

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	public static boolean isText(String content) {
		if(content==null) {
			return true;
		}
		return content.indexOf(LITERATE_TOKEN_OPEN)==-1;
	}

	public static boolean isScriptExpression(String content) {
		return !isText(content);
	}

	public static HAPExpressionScriptImp parseDefinitionExpression(String content, String scriptType, HAPParserDataExpression expressionParser) {
		HAPExpressionScriptImp out = null;
		
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
			out = parseDefinitionExpressionScript(content, expressionParser);
		}
		else if(scriptType.equals(HAPConstantShared.EXPRESSION_TYPE_LITERATE)) {
			out = parseDefinitionExpressionLiterate(content, expressionParser);
		}
		
		return out;
	}
	
	public static HAPExpressionScriptImp parseDefinitionExpressionText(String content) {
		HAPExpressionScriptImp out = new HAPExpressionScriptImp(HAPConstantShared.EXPRESSION_TYPE_TEXT);
		HAPGeneratorId idGenerator = new HAPGeneratorId();
		out.addSegment(parseDefinitionSegmentExpressionText(content, idGenerator));
		return out;
	}
	
	public static HAPExpressionScriptImp parseDefinitionExpressionScript(String content, HAPParserDataExpression expressionParser) {
		HAPExpressionScriptImp out = new HAPExpressionScriptImp(HAPConstantShared.EXPRESSION_TYPE_SCRIPT);
		HAPGeneratorId idGenerator = new HAPGeneratorId();
		HAPSegmentScriptExpressionScriptComplex scriptSeg = parseDefinitionSegmentExpressionDataScript(content, out.getDataExpressionContainer(), expressionParser, idGenerator);
		out.addSegment(scriptSeg);
		return out;
	}
	
	public static HAPExpressionScriptImp parseDefinitionExpressionLiterate(String script, HAPParserDataExpression expressionParser) {
		HAPExpressionScriptImp out = new HAPExpressionScriptImp(HAPConstantShared.EXPRESSION_TYPE_LITERATE);
		HAPGeneratorId idGenerator = new HAPGeneratorId();
		
		if(script!=null) {
			int start = script.indexOf(LITERATE_TOKEN_OPEN);
			while(start != -1){
				if(start>0) {
					out.addSegment(parseDefinitionSegmentExpressionText(script.substring(0, start), idGenerator));
				}
				int expEnd = script.indexOf(LITERATE_TOKEN_CLOSE, start);
				int end = expEnd + LITERATE_TOKEN_CLOSE.length();
				String expression = script.substring(start+LITERATE_TOKEN_OPEN.length(), expEnd);
				out.addSegment(parseDefinitionSegmentExpressionDataScript(expression, out.getDataExpressionContainer(), expressionParser, idGenerator));
				//keep searching the rest
				script=script.substring(end);
				start = script.indexOf(LITERATE_TOKEN_OPEN);
			}
			if(!HAPUtilityBasic.isStringEmpty(script)){
				out.addSegment(parseDefinitionSegmentExpressionText(script, idGenerator));
			}
		}
		return out;
	}
	
	private static HAPSegmentScriptExpressionText parseDefinitionSegmentExpressionText(String content, HAPGeneratorId idGenerator) {
		return new HAPSegmentScriptExpressionText(idGenerator.generateId(), content);
	}

	private static HAPSegmentScriptExpressionScriptSimple parseDefinitionSegmentExpressionScript(String script, HAPGeneratorId idGenerator) {
		HAPSegmentScriptExpressionScriptSimple out = new HAPSegmentScriptExpressionScriptSimple(idGenerator.generateId());
		parseExpressionScriptSegment(script, out);
		return out;
	}

	//define the segment parsing infor
	private static final Object[][] m_definitions = {
			{"&(", ")&", HAPConstantInScript.class}, 
			{"?(", ")?", HAPVariableInScript.class}
	};

	private static void parseExpressionScriptSegment(String orignalScript, HAPSegmentScriptExpressionScriptSimple expressionScriptSegment){
		try{
			String content = orignalScript;
			int[] indexs = indexScript(content);
			while(indexs[0]!=-1){
				int type = indexs[1];
				String startToken = (String)m_definitions[type][0];
				String endToken = (String)m_definitions[type][1];
				int startIndex = indexs[0];
				if(startIndex>0){
					expressionScriptSegment.addPart(content.substring(0, startIndex));
				}
				int endIndex = content.indexOf(endToken);
				Class cs = (Class)m_definitions[type][2];
				Constructor cons = cs.getConstructor(String.class);
				String name = content.substring(startIndex+startToken.length(), endIndex);
				expressionScriptSegment.addPart(cons.newInstance(name));
				content = content.substring(endIndex+endToken.length());
				indexs = indexScript(content);
			}
			if(HAPUtilityBasic.isStringNotEmpty(content)){
				expressionScriptSegment.addPart(content);
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
			if(index==-1) {
				continue;
			} else if(index < currentIndex){
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

	
	private static HAPSegmentScriptExpressionScriptComplex parseDefinitionSegmentExpressionDataScript(String script, HAPContainerDataExpression dataExpressionGroup, HAPParserDataExpression expressionParser, HAPGeneratorId idGenerator) {
		HAPSegmentScriptExpressionScriptComplex out = new HAPSegmentScriptExpressionScriptComplex(idGenerator.generateId());
		String content = script;
		int i = 0;
		while(HAPUtilityBasic.isStringNotEmpty(content)){
			int index = content.indexOf(EXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				out.addSegmentScriptSimple(parseDefinitionSegmentExpressionScript(content, idGenerator));
				content = null;
			}
			else if(index!=0){
				//start with text
				out.addSegmentScriptSimple(parseDefinitionSegmentExpressionScript(content.substring(0, index), idGenerator));
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
				out.addSegmentDataExpression(parseDefinitionSegmentExpressionData(expressionStr, dataExpressionGroup, expressionParser, idGenerator));
			}
			i++;
		}
		return out;
	}

	private static HAPSegmentScriptExpressionScriptDataExpression parseDefinitionSegmentExpressionData(String dataExpressionScript, HAPContainerDataExpression dataExpressionGroup, HAPParserDataExpression expressionParser, HAPGeneratorId idGenerator) {
		HAPExpressionData dataExpression = HAPBasicUtilityProcessorDataExpression.buildBasicDataExpression(expressionParser.parseExpression(dataExpressionScript));
		String dataExpressionId = dataExpressionGroup.addDataExpression(dataExpression);
		return new HAPSegmentScriptExpressionScriptDataExpression(idGenerator.generateId(), dataExpressionId);
	}

}
