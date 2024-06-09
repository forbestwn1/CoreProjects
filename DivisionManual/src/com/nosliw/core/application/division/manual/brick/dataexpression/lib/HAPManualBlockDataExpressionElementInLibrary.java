package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.interactive.HAPResultInInteractiveExpression;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

public class HAPManualBlockDataExpressionElementInLibrary extends HAPManualBrickBlockSimple implements HAPInteractiveExpression{

	protected HAPManualBlockDataExpressionElementInLibrary(HAPIdBrickType brickType) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100);
		this.setAttributeWithValueValue(REQUEST, new ArrayList<HAPRequestParmInInteractive>());
	}

	@Override
	public List<HAPRequestParmInInteractive> getRequestParms() {  return (List<HAPRequestParmInInteractive>)this.getAttributeValueWithValue(REQUEST);  }
	public void addRequestParm(HAPRequestParmInInteractive requestParm) {	this.getRequestParms().add(requestParm);	}
	
	public void setResult(HAPDataTypeCriteria result) {   this.setAttributeWithValueValue(RESULT, result);      }
	
	@Override
	public HAPResultInInteractiveExpression getResult() {   return (HAPResultInInteractiveExpression)this.getAttributeValueWithValue(RESULT);  } 

	public String getExpression() {	return (String)this.getAttributeValueWithValue(HAPBlockDataExpressionElementInLibrary.EXPRESSION);	}
	public void setExpression(String expressionStr) {   this.setAttributeWithValueValue(HAPBlockDataExpressionElementInLibrary.EXPRESSION, expressionStr);     }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		return true;  
	}
	
}
