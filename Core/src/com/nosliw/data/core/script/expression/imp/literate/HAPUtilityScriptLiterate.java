package com.nosliw.data.core.script.expression.imp.literate;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.expression.HAPScript;

public class HAPUtilityScriptLiterate {

	public static final String UIEXPRESSION_TOKEN_OPEN = "<%=";
	public static final String UIEXPRESSION_TOKEN_CLOSE = "%>";

	/**
	 * parse text to discover script expression in it
	 * @param script
	 * @return a list of text and script expression definition
	 */
	public static List<HAPScript> parseScriptLiterate(String script){
		List<HAPScript> out = new ArrayList<HAPScript>();
		
		if(script==null) return out;
		
		int start = script.indexOf(UIEXPRESSION_TOKEN_OPEN);
		while(start != -1){
			if(start>0)   out.add(HAPScript.newScript(script.substring(0, start), HAPConstantShared.SCRIPT_TYPE_SEG_TEXT));
			int expEnd = script.indexOf(UIEXPRESSION_TOKEN_CLOSE, start);
			int end = expEnd + UIEXPRESSION_TOKEN_CLOSE.length();
			String expression = script.substring(start+UIEXPRESSION_TOKEN_OPEN.length(), expEnd);
			out.add(HAPScript.newScript(expression, HAPConstantShared.SCRIPT_TYPE_SEG_EXPRESSIONSCRIPT));
			//keep searching the rest
			script=script.substring(end);
			start = script.indexOf(UIEXPRESSION_TOKEN_OPEN);
		}
		if(!HAPUtilityBasic.isStringEmpty(script)){
			out.add(HAPScript.newScript(script, HAPConstantShared.SCRIPT_TYPE_SEG_TEXT));
		}
		return out;
	}
	
	public static String buildScriptLiterate(List<HAPScript> scriptSegs) {
		StringBuffer out = new StringBuffer();
		for(HAPScript scriptSeg : scriptSegs) {
			String scriptType = scriptSeg.getType();
			if(HAPConstantShared.SCRIPT_TYPE_SEG_TEXT.equals(scriptType)) {
				out.append(scriptSeg.getScript());
			}
			else if(HAPConstantShared.SCRIPT_TYPE_SEG_EXPRESSIONSCRIPT.equals(scriptType)) {
				out.append(UIEXPRESSION_TOKEN_OPEN).append(scriptSeg.getScript()).append(UIEXPRESSION_TOKEN_CLOSE);
			}
		}
		return out.toString();
	}
	
	public static boolean isText(String literateScript) {
		return literateScript.indexOf(UIEXPRESSION_TOKEN_OPEN)==-1;
	}
}
