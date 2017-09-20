package com.nosliw.uiresource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;

/**
 * Represent ui expression
 * UI expression is a mix of data expression and javascript expression
 * The result of UI expression is javascript entity (string, boolean number, or object)
 * It is used in:
 * 		part of html text
 * 		tag attribute
 * 		constant definition
 */
public class HAPScriptExpression {

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	private String m_uiId;
	
	private String m_content;

	private HAPExpressionManager m_expressionManager;
	
	//store all elements in ui expression:
	//     	js expression:  HAPScriptExpressionScriptSegment
	//		data expression : expression definition
	private List<Object> m_elements;
	
	//string - expression
	private List<Object> m_processedElements;
	
	public HAPScriptExpression(String uiId, String content, HAPExpressionManager expressionMan){
		this.m_expressionManager = expressionMan;
		this.m_uiId = uiId;
		this.m_content = content;
		this.m_elements = new ArrayList<Object>();
		this.init();
	}
	
	public String getId(){  return this.m_uiId;  }
	
	public List<Object> getElements(){  return this.m_elements;   }
	
	public List<Object> getProcessedElements(){ return this.m_processedElements; }
	
	public void processExpressions(Map<String, HAPData> contextConstants, Map<String, HAPDataTypeCriteria> variableCriterias){
		this.m_processedElements = new ArrayList<Object>();
		for(Object ele : this.m_elements){
			if(ele instanceof HAPExpressionDefinition){
				HAPExpressionDefinition expDef = (HAPExpressionDefinition)ele;
				HAPExpression expression = this.m_expressionManager.processExpression(expDef.getName(), expDef, contextConstants, variableCriterias);
				this.m_processedElements.add(expression);
			}
			else if(ele instanceof HAPScriptExpressionScriptSegment){
				this.m_processedElements.add(ele);
			}
		}
	}
	
	private void init(){
		String content = this.m_content;
		int i = 0;
		while(HAPBasicUtility.isStringNotEmpty(content)){
			int index = content.indexOf(EXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				this.m_elements.add(new HAPScriptExpressionScriptSegment(content));
				content = null;
			}
			else if(index!=0){
				//start with text
				this.m_elements.add(new HAPScriptExpressionScriptSegment(content.substring(0, index)));
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
				HAPExpressionDefinition expressionDefinition = this.m_expressionManager.newExpressionDefinition(expressionStr, this.m_uiId+"_"+i, null, null); 
				this.m_elements.add(expressionDefinition);
			}
			i++;
		}
	}
}
