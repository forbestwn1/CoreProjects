package com.nosliw.uiresource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSimple;

/**
 * Represent ui expression
 * UI expression is a mix of data expression and javascript expression
 * The result of UI expression is javascript entity (string, boolean number, or object)
 * It is used in:
 * 		part of html text
 * 		tag attribute
 * 		constant definition
 */
public class HAPUIExpression {

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	private String m_uiId;
	
	private String m_content;

	//store all elements in ui expression:
	//     	js expression:  string
	//		data expression : expression definition
	private List<Object> m_elements;
	
	public HAPUIExpression(String uiId, String content){
		this.m_uiId = uiId;
		this.m_content = content;
		this.m_elements = new ArrayList<Object>();
		this.process();
	}
	
	private void process(){
		String content = this.m_content;
		int i = 0;
		while(HAPBasicUtility.isStringNotEmpty(content)){
			int index = content.indexOf(EXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				this.m_elements.add(content);
				content = null;
			}
			else if(index!=0){
				//start with text
				this.m_elements.add(content.substring(0, index));
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
				HAPExpressionDefinitionSimple expressionDefinition = new HAPExpressionDefinitionSimple(expressionStr, this.m_uiId+"_"+i, null, null, null, null);
				this.m_elements.add(expressionDefinition);
			}
			i++;
		}
	}
}
