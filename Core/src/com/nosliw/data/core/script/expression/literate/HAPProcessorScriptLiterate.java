package com.nosliw.data.core.script.expression.literate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;
import com.nosliw.data.core.expression.HAPParserExpression;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptEntity;
import com.nosliw.data.core.script.expression.HAPScript;

public class HAPProcessorScriptLiterate {

	public static final String UIEXPRESSION_TOKEN_OPEN = "<%=";
	public static final String UIEXPRESSION_TOKEN_CLOSE = "%>";
	

	public static HAPExecutableScriptLiterate process(
		String id,
		HAPDefinitionScriptEntity scriptDef,
		Map<String, Object> constantValues,
		HAPDefinitionExpressionGroup expressionDef,
		HAPParserExpression expressionParser 
	) {
		HAPExecutableScriptLiterate out = new HAPExecutableScriptLiterate();
		scriptDef.cloneToEntityInfo(out);
		List<Object> scriptSegs = parseScript(scriptDef.getScript());
		for(int j=0; j<scriptSegs.size(); j++) {
			Object scriptSeg = scriptSegs.get(j);
			
		}
		
		
		
		return out;
	}
	
	
	
	/**
	 * parse text to discover script expression in it
	 * @param script
	 * @return a list of text and script expression definition
	 */
	private static List<HAPScript> parseScript(String script){
		List<HAPScript> out = new ArrayList<HAPScript>();
		
		if(script==null) return out;
		
		int start = script.indexOf(UIEXPRESSION_TOKEN_OPEN);
		while(start != -1){
			if(start>0)   out.add(script.substring(0, start));
			int expEnd = script.indexOf(UIEXPRESSION_TOKEN_CLOSE, start);
			int end = expEnd + UIEXPRESSION_TOKEN_CLOSE.length();
			String expression = script.substring(start+UIEXPRESSION_TOKEN_OPEN.length(), expEnd);
			out.add(HAPScript.newScript(expression, HAPScript.));
			//keep searching the rest
			script=script.substring(end);
			start = script.indexOf(UIEXPRESSION_TOKEN_OPEN);
		}
		if(!HAPBasicUtility.isStringEmpty(script)){
			out.add(script);
		}
		return out;
	}

}
