package com.nosliw.core.application.brick.dataexpression.lib;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.entityinfo.HAPBrickWithEntityInfoSimple;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractiveInterface;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.dataexpression.HAPExecutableExpressionData;

@HAPEntityWithAttribute
public class HAPBlockDataExpressionElementInLibrary extends HAPBrickWithEntityInfoSimple implements HAPInteractiveExpression{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	@HAPAttribute
	public static String PARMS = "parms";
	
	@HAPAttribute
	public static String RESULT = "result";
	
	@Override
	public void init() {
		this.setAttributeValueWithValue(PARMS, new ArrayList<HAPRequestParmInInteractiveInterface>());
	}
	
	public HAPExecutableExpressionData getDataExpression() {	return (HAPExecutableExpressionData)this.getAttributeValueOfValue(EXPRESSION);	}
	
	public void setDataExpression(HAPExecutableExpressionData dataExpression) {	this.setAttributeValueWithValue(EXPRESSION, dataExpression);	}
	
	@Override
	public List<HAPRequestParmInInteractiveInterface> getRequestParms() {   return (List<HAPRequestParmInInteractiveInterface>)this.getAttributeValueOfValue(PARMS);  }

	@Override
	public HAPDataTypeCriteria getResult() {   return (HAPDataTypeCriteria)this.getAttributeValueOfValue(RESULT);  }
	
}
