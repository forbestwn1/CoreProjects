package com.nosliw.core.application.brick.dataexpression.lib;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.entityinfo.HAPBrickWithEntityInfoSimple;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.interactive.HAPResultInInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractive;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.data.core.dataexpression.HAPExecutableExpressionData;

@HAPEntityWithAttribute
public class HAPBlockDataExpressionElementInLibrary extends HAPBrickWithEntityInfoSimple implements HAPInteractiveExpression{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	@Override
	public void init() {
		this.setAttributeValueWithValue(REQUEST, new ArrayList<HAPRequestParmInInteractive>());
	}
	
	public HAPExecutableExpressionData getExpression() {	return (HAPExecutableExpressionData)this.getAttributeValueOfValue(EXPRESSION);	}
	
	public void setExpression(HAPExecutableExpressionData dataExpression) {	this.setAttributeValueWithValue(EXPRESSION, dataExpression);	}
	
	@Override
	public List<HAPRequestParmInInteractive> getRequestParms() {   return (List<HAPRequestParmInInteractive>)this.getAttributeValueOfValue(REQUEST);  }

	@Override
	public HAPResultInInteractiveExpression getResult() {   return (HAPResultInInteractiveExpression)this.getAttributeValueOfValue(RESULT);  }

	@Override
	public HAPContainerValuePorts getExternalValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		HAPGroupValuePorts valuePortGroup = HAPUtilityInteractive.buildExternalInteractiveExpressionValuePortGroup(this);
		out.addValuePortGroup(valuePortGroup, true);
		return out;	
	}
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		HAPGroupValuePorts valuePortGroup = HAPUtilityInteractive.buildExternalInteractiveExpressionValuePortGroup(this);
		out.addValuePortGroup(valuePortGroup, true);
		return out;	
	}
	
}
