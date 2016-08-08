package com.nosliw.entity.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.expression.HAPExpression;

public class HAPValidationInfoExpression implements HAPStringable{

	private String m_name;
	private HAPExpression m_expression;
	private String m_description;
	private String m_errorMessage;
	private boolean m_serverOnly = true;
	
	public HAPValidationInfoExpression(String name, HAPExpression expression){
		this.m_name = name;
		this.m_expression = expression;
		this.m_serverOnly = !this.m_expression.isScriptRunnable(HAPConstant.CONS_OPERATIONDEF_SCRIPT_JAVASCRIPT);
	}

	public HAPValidationInfoExpression(HAPExpression expression){
		this.m_name = "default";
		this.m_expression = expression;
		this.m_serverOnly = !this.m_expression.isScriptRunnable(HAPConstant.CONS_OPERATIONDEF_SCRIPT_JAVASCRIPT);
	}

	public String getName(){return this.m_name;}
	public HAPExpression getExpression(){return this.m_expression;}
	public String getDescription(){return this.m_description;}
	public void setDescription(String desc){this.m_description=desc;}
	public String getErrorMessage(){return this.m_errorMessage;}
	public void setErrorMessage(String message){this.m_errorMessage=message;}
	
	public boolean isServerOnly(){return this.m_serverOnly;}
	
	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put("name", this.m_name);
		jsonMap.put("description", this.m_description);
		jsonMap.put("errorMsg", this.m_errorMessage);
		if(this.m_expression!=null)		jsonMap.put("dataExpression", this.m_expression.toStringValue(format));
		return HAPJsonUtility.getMapJson(jsonMap);
	}
}
