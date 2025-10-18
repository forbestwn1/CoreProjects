package com.nosliw.core.application.common.dataexpression.definition;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionParm;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionResult;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionStandAlone;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPDefinitionDataExpressionStandAlone extends HAPSerializableImp{

	private HAPInteractiveExpression m_interactiveExpression;
	
	private String m_expressionStr;
	
	private HAPDefinitionDataExpression m_expression;
	
	public HAPDefinitionDataExpressionStandAlone() {
	}
	
	public HAPInteractiveExpression getExpressionInteractive() {   return this.m_interactiveExpression;    }
	public void setExpressionInteractive(HAPInteractiveExpression interactive) {   this.m_interactiveExpression = interactive;       }
	
	public List<HAPDefinitionParm> getRequestParms() {  return this.m_interactiveExpression.getRequestParms();  }
	public void addRequestParm(HAPDefinitionParm requestParm) {	this.getRequestParms().add(requestParm);	}
	
	public HAPDefinitionResult getResult() {   return this.m_interactiveExpression.getResult();  } 
	
	public String getExpressionStr() {    return this.m_expressionStr;     }
	public void setExpressionStr(String expressionStr) {    this.m_expressionStr = expressionStr;      }
	
	public HAPDefinitionDataExpression getExpression() {	return this.m_expression;	}
	public void setExpression(HAPDefinitionDataExpression expression) {   this.m_expression = expression;     }

	public static HAPDefinitionDataExpressionStandAlone parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPDefinitionDataExpressionStandAlone out = new HAPDefinitionDataExpressionStandAlone();
		out.setExpressionInteractive(HAPInteractiveExpression.parse(jsonObj, dataRuleMan));
		out.setExpressionStr(jsonObj.getString(HAPDataExpressionStandAlone.EXPRESSION));
		return out;
	}
	
}
