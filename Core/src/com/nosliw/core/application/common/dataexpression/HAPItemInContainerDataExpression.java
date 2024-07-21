package com.nosliw.core.application.common.dataexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.container.HAPItemWrapper;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPItemInContainerDataExpression extends HAPItemWrapper{

	@HAPAttribute
	public static String DATAEXPRESSION = "dataExpression";

	public HAPItemInContainerDataExpression() {}
	
	public HAPItemInContainerDataExpression(HAPExpressionData dataExpression) {
		super(dataExpression);
	}
	
	public HAPExpressionData getDataExpression() {    return (HAPExpressionData)this.getValue();     }
	public void setDataExpression(HAPExpressionData expression) {    this.setValue(expression);      }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATAEXPRESSION, this.getDataExpression().toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}
}
