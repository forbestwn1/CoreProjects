package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.dataexpression.library.HAPElementInLibraryDataExpression;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpression;
import com.nosliw.core.application.common.interactive.HAPInteractive;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveResultExpression;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;

public class HAPManualDataExpressionLibraryElement extends HAPEntityInfoImp implements HAPInteractive{

	private HAPInteractiveExpression m_interactiveExpression;
	
	private String m_expressionStr;
	
	private HAPDefinitionDataExpression m_expression;
	
	public HAPManualDataExpressionLibraryElement() {
	}
	
	public List<HAPRequestParmInInteractive> getRequestParms() {  return this.m_interactiveExpression.getRequestParms();  }
	public void addRequestParm(HAPRequestParmInInteractive requestParm) {	this.getRequestParms().add(requestParm);	}
	
	public HAPInteractiveResultExpression getResult() {   return this.m_interactiveExpression.getResult();  } 
	
	public String getExpressionStr() {    return this.m_expressionStr;     }
	
	public HAPDefinitionDataExpression getExpression() {	return this.m_expression;	}
	public void setExpression(HAPDefinitionDataExpression expression) {   this.m_expression = expression;     }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		
		this.m_interactiveExpression = new HAPInteractiveExpression();
		this.m_interactiveExpression.buildObject(jsonObj, HAPSerializationFormat.JSON);
		
		this.m_expressionStr = jsonObj.getString(HAPElementInLibraryDataExpression.EXPRESSION);
		
		return true;  
	}
}
