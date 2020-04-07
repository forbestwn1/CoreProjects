package com.nosliw.data.core.script.expression.literate;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.script.expression.expression.HAPDefinitionScriptExpression;

public class HAPDefinitionEmbededScriptExpression {

	public static final String UIEXPRESSION_TOKEN_OPEN = "<%=";
	public static final String UIEXPRESSION_TOKEN_CLOSE = "%>";
	
	//a list of elements
	//   string 
	//   script expression definition(HAPDefinitionScriptExpression)
	private List<Object> m_elements;
	
	public HAPDefinitionEmbededScriptExpression(HAPDefinitionScriptExpression scriptExpression){
		this.m_elements = new ArrayList<Object>();
		this.m_elements.add(scriptExpression);
	}
	
	public HAPDefinitionEmbededScriptExpression(String content){
		this.m_elements = this.discoverEmbededScript(content);
	}

	public List<Object> getElements(){   return this.m_elements;  }

	public boolean isString() {
		boolean out = true;
		for(Object ele : this.m_elements) {
			if(!(ele instanceof String)) {
				out = false;
				break;
			}
		}
		return out;
	}
	
	/**
	 * parse text to discover script expression in it
	 * @param text
	 * @return a list of text and script expression definition
	 */
	private List<Object> discoverEmbededScript(String text){
		List<Object> out = new ArrayList<Object>();
		
		if(text==null) return out;
		
		int start = text.indexOf(UIEXPRESSION_TOKEN_OPEN);
		while(start != -1){
			if(start>0)   out.add(text.substring(0, start));
			int expEnd = text.indexOf(UIEXPRESSION_TOKEN_CLOSE, start);
			int end = expEnd + UIEXPRESSION_TOKEN_CLOSE.length();
			String expression = text.substring(start+UIEXPRESSION_TOKEN_OPEN.length(), expEnd);
			HAPDefinitionScriptExpression uiExpression = new HAPDefinitionScriptExpression(expression);
			out.add(uiExpression);
			//keep searching the rest
			text=text.substring(end);
			start = text.indexOf(UIEXPRESSION_TOKEN_OPEN);
		}
		if(!HAPBasicUtility.isStringEmpty(text)){
			out.add(text);
		}
		return out;
	}
	
}
