package com.nosliw.uiresource;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeManager;

/*
 * class for ui resource expression
 * ui resource is combination of js expression and data expression
 */
@HAPEntityWithAttribute
public class HAPUIResourceExpression extends HAPSerializableImp{

	@HAPAttribute
	public static final String EXPRESSIONID = "expressionId";
	@HAPAttribute
	public static final String FUNCTIONSCRIPT = "functionScript";
	@HAPAttribute
	public static final String EXPRESSIONUNITS = "expressionUnits";
	@HAPAttribute
	public static final String CONTEXTVARIABLES = "contextVariables";
	@HAPAttribute
	public static final String EXPRESSIONOBJECT = "expressionObject";

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
//		this.m_dataTypeMan = dataTypeMan;
//		this.m_expression = exp;
//		this.m_expressionId = expressionId;
//
//		String expression = exp.trim(); 
//
//		m_expressionUnits = new ArrayList<HAPUIResourceExpressionUnit>();
//		int i = 0;
//		int start = expression.indexOf(HAPConstant.UIRESOURCE_EXPRESSION_TOKEN_OPEN);
//		while(start!=-1){
//			int end = expression.indexOf(HAPConstant.UIRESOURCE_EXPRESSION_TOKEN_CLOSE, start);
//			String expStr = expression.substring(start+HAPConstant.UIRESOURCE_EXPRESSION_TOKEN_OPEN.length(), end);
//
//			//create expression object
//			HAPExpression expObj = new HAPExpression(new HAPExpressionInfo(expStr, constants, null), this.getDataTypeManager());
//			m_expressionUnits.add(new HAPUIResourceExpressionUnit(expObj));
//			
//			//build script
//			expression = expression.substring(0, start) + "expressionResultsArray[" + i + "]" + expression.substring(end+HAPConstant.UIRESOURCE_EXPRESSION_TOKEN_CLOSE.length());
//			
//			i++;
//			start = expression.indexOf(HAPConstant.UIRESOURCE_EXPRESSION_TOKEN_OPEN);
//		}
//		
//		//build function script for expression
//		this.m_functionScript = this.buildFunctionScript(expression);
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
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(EXPRESSIONID, this.m_expressionId);
		jsonMap.put(EXPRESSIONUNITS, HAPJsonUtility.buildJson(m_expressionUnits, HAPSerializationFormat.JSON_FULL));

		
//don't need create functin script here,   		
//		jsonMap.put(FUNCTIONSCRIPT, this.m_functionScript);
		
//		jsonMap.put(VARIABLES, HAPJsonUtility.getSetObjectJson(m_variables));
		
	}
}

/*
 * class store expression in uiresource
 */
class HAPUIResourceExpressionUnit extends HAPSerializableImp{
//	//all context variables (it may contain constant name, which one is real context variable, it is left determined on script side)
//	public Set<HAPContextVariable> m_contextVariables;
//	//expression object
//	public HAPExpression m_expression;
//
//	public HAPUIResourceExpressionUnit(HAPExpression expression){
//		this.m_expression = expression;
//		this.processContextVariable();
//	}
//	
//	/*
//	 * get all context variables
//	 */
//	private void processContextVariable(){
//		m_contextVariables = new HashSet<HAPContextVariable>();
//		Set<String> paths = m_expression.getAllPathVariables();
//		for(String path : paths){
//			m_contextVariables.add(new HAPContextVariable(path));
//		}
//	}
//
//	@Override
//	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
//		jsonMap.put(HAPUIResourceExpression.EXPRESSIONOBJECT, this.m_expression.toStringValue(HAPSerializationFormat.JSON_FULL));
//		jsonMap.put(HAPUIResourceExpression.CONTEXTVARIABLES, HAPJsonUtility.buildJson(m_contextVariables, HAPSerializationFormat.JSON_FULL));
//	}
}
