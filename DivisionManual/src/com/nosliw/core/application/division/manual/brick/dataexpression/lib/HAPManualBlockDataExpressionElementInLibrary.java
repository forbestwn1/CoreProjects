package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.interactive.HAPResultInInteractiveExpression;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;

public class HAPManualBlockDataExpressionElementInLibrary extends HAPManualBrickBlockSimple implements HAPInteractiveExpression{

	public HAPManualBlockDataExpressionElementInLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100);
		this.setAttributeWithValueValue(REQUEST, new ArrayList<HAPRequestParmInInteractive>());
	}

	@Override
	public List<HAPRequestParmInInteractive> getRequestParms() {  return (List<HAPRequestParmInInteractive>)this.getAttributeValueWithValue(REQUEST);  }
	public void addRequestParm(HAPRequestParmInInteractive requestParm) {	this.getRequestParms().add(requestParm);	}
	
	@Override
	public HAPResultInInteractiveExpression getResult() {   return (HAPResultInInteractiveExpression)this.getAttributeValueWithValue(RESULT);  } 
	public void setResult(HAPResultInInteractiveExpression result) {   this.setAttributeWithValueValue(RESULT, result);      }
	
	public String getExpression() {	return (String)this.getAttributeValueWithValue(HAPBlockDataExpressionElementInLibrary.EXPRESSION);	}
	public void setExpression(String expressionStr) {   this.setAttributeWithValueValue(HAPBlockDataExpressionElementInLibrary.EXPRESSION, expressionStr);     }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		JSONArray parmArray = jsonObj.optJSONArray(REQUEST);
		if(parmArray!=null) {
			for(int i=0; i<parmArray.length(); i++) {
				JSONObject parmObj = parmArray.getJSONObject(i);
				HAPRequestParmInInteractive parm = HAPRequestParmInInteractive.buildParmFromObject(parmObj);
				this.getRequestParms().add(parm);
			}
		}
		
		JSONObject resultObj = jsonObj.optJSONObject(RESULT);
		if(resultObj!=null) {
			HAPResultInInteractiveExpression result = new HAPResultInInteractiveExpression();
			result.buildObject(resultObj, HAPSerializationFormat.JSON);
			this.setResult(result);
		}
		
		this.setExpression(jsonObj.getString(HAPBlockDataExpressionElementInLibrary.EXPRESSION));
		
		return true;  
	}
	
}
