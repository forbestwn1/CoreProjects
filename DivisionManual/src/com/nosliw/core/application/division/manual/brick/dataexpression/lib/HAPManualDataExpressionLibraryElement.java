package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionElementInLibrary;
import com.nosliw.core.application.common.interactive.HAPInteractive;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.interactive.HAPResultElementInInteractiveExpression;

public class HAPManualDataExpressionLibraryElement extends HAPEntityInfoImp implements HAPInteractive{

	private HAPResultElementInInteractiveExpression m_result;
	
	private List<HAPRequestParmInInteractive> m_requestParms;
	
	private String m_expression;
	
	public HAPManualDataExpressionLibraryElement() {
		this.m_requestParms = new ArrayList<HAPRequestParmInInteractive>();
	}
	
	public List<HAPRequestParmInInteractive> getRequestParms() {  return this.m_requestParms;  }
	public void addRequestParm(HAPRequestParmInInteractive requestParm) {	this.getRequestParms().add(requestParm);	}
	
	public HAPResultElementInInteractiveExpression getResult() {   return this.m_result;  } 
	public void setResult(HAPResultElementInInteractiveExpression result) {   this.m_result = result;      }
	
	public String getExpression() {	return this.m_expression;	}
	public void setExpression(String expressionStr) {   this.m_expression = expressionStr;     }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		super.buildObjectByJson(json);
		
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
			HAPResultElementInInteractiveExpression result = new HAPResultElementInInteractiveExpression();
			result.buildObject(resultObj, HAPSerializationFormat.JSON);
			this.setResult(result);
		}
		
		this.setExpression(jsonObj.getString(HAPDataExpressionElementInLibrary.EXPRESSION));
		
		return true;  
	}
}
