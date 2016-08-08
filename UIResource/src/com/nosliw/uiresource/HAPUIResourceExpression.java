package com.nosliw.uiresource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;

/*
 * class for ui resource expression
 * ui resource is combination of js expression and data expression
 */
public class HAPUIResourceExpression implements HAPStringable{

	//original expression
	private String m_expression;
	//function name for this expression
	private String m_expressionId;
	//functin script for this expression
	private String m_functionScript;

	//all expressions within uiResourceExpression
	private List<HAPUIResourceExpressionUnit> m_expressionUnits;
	
	protected HAPDataTypeManager m_dataTypeMan;
	
	public HAPUIResourceExpression(String exp, String expressionId, Map<String, HAPData> constants, HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
		this.m_expression = exp;
		this.m_expressionId = expressionId;

		String expression = exp.trim(); 

		m_expressionUnits = new ArrayList<HAPUIResourceExpressionUnit>();
		int i = 0;
		int start = expression.indexOf(HAPConstant.CONS_UIRESOURCE_EXPRESSION_TOKEN_OPEN);
		while(start!=-1){
			int end = expression.indexOf(HAPConstant.CONS_UIRESOURCE_EXPRESSION_TOKEN_CLOSE, start);
			String expStr = expression.substring(start+HAPConstant.CONS_UIRESOURCE_EXPRESSION_TOKEN_OPEN.length(), end);

			//create expression object
			HAPExpression expObj = new HAPExpression(new HAPExpressionInfo(expStr, constants, null), this.getDataTypeManager());
			m_expressionUnits.add(new HAPUIResourceExpressionUnit(expObj));
			
			//build script
			expression = expression.substring(0, start) + "expressionResultsArray[" + i + "]" + expression.substring(end+HAPConstant.CONS_UIRESOURCE_EXPRESSION_TOKEN_CLOSE.length());
			
			i++;
			start = expression.indexOf(HAPConstant.CONS_UIRESOURCE_EXPRESSION_TOKEN_OPEN);
		}
		
		//build function script for expression
		this.m_functionScript = this.buildFunctionScript(expression);
	}
	
	public String getFunctionScript(){return this.m_functionScript;}
	public String getExpressionId(){return this.m_expressionId;}
	
	protected HAPDataTypeManager getDataTypeManager(){ return this.m_dataTypeMan; }
	
	
	/*
	 * build function script
	 */
	private String buildFunctionScript(String expScript){
		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(this.getClass(), "UIExpressionScript.txt");
		
		Map<String, String> parms = new LinkedHashMap<String, String>();
		parms.put("expressionScript", expScript);
		
		return HAPStringTemplateUtil.getStringValue(templateStream, parms);
	}
	
	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCEEXPRESSION_EXPRESSIONID, this.m_expressionId);
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCEEXPRESSION_EXPRESSIONUNITS, HAPJsonUtility.getListObjectJson(m_expressionUnits));

		
//don't need create functin script here,   		
//		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCEEXPRESSION_FUNCTIONSCRIPT, this.m_functionScript);
		
//		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCEEXPRESSION_VARIABLES, HAPJsonUtility.getSetObjectJson(m_variables));
		
		return HAPJsonUtility.getMapJson(jsonMap);
	}
}

/*
 * class store expression in uiresource
 */
class HAPUIResourceExpressionUnit implements HAPStringable{
	//all context variables (it may contain constant name, which one is real context variable, it is left determined on script side)
	public Set<HAPContextVariable> m_contextVariables;
	//expression object
	public HAPExpression m_expression;

	public HAPUIResourceExpressionUnit(HAPExpression expression){
		this.m_expression = expression;
		this.processContextVariable();
	}
	
	/*
	 * get all context variables
	 */
	private void processContextVariable(){
		m_contextVariables = new HashSet<HAPContextVariable>();
		Set<String> paths = m_expression.getAllPathVariables();
		for(String path : paths){
			m_contextVariables.add(new HAPContextVariable(path));
		}
	}

	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();

		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCEEXPRESSION_EXPRESSIONOBJECT, this.m_expression.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCEEXPRESSION_CONTEXTVARIABLES, HAPJsonUtility.getSetObjectJson(m_contextVariables));
		
		return HAPJsonUtility.getMapJson(jsonMap);
	}
}
