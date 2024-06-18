package com.nosliw.core.application.common.dataexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.interactive.HAPResultInInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractive;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPWithValuePort;

@HAPEntityWithAttribute
public class HAPDataExpressionElementInLibrary extends HAPEntityInfoImp implements HAPInteractiveExpression, HAPDataExpressionUnit, HAPWithValuePort{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	private HAPDataExpression m_dataExpression;
	
	private List<HAPRequestParmInInteractive> m_requestParms;
	
	private HAPResultInInteractiveExpression m_result;
	
	private HAPContainerVariableCriteriaInfo m_variableInfo;
	
	public HAPDataExpressionElementInLibrary() {
		this.m_requestParms = new ArrayList<HAPRequestParmInInteractive>();
		this.m_variableInfo = new HAPContainerVariableCriteriaInfo();
	}
	
	public HAPDataExpression getExpression() {	return m_dataExpression;	}
	
	public void setExpression(HAPDataExpression dataExpression) {	this.m_dataExpression = dataExpression;	}
	
	@Override
	public List<HAPRequestParmInInteractive> getRequestParms() {   return this.m_requestParms;  }

	@Override
	public HAPResultInInteractiveExpression getResult() {   return this.m_result;  }
	public void setResult(HAPResultInInteractiveExpression result) {    this.m_result = result;     }

	@Override
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return this.m_variableInfo;  }
	
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
		HAPGroupValuePorts valuePortGroup = HAPUtilityInteractive.buildInternalInteractiveExpressionValuePortGroup(this);
		out.addValuePortGroup(valuePortGroup, true);
		return out;	
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(REQUEST, HAPManagerSerialize.getInstance().toStringValue(this.getRequestParms(), HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPManagerSerialize.getInstance().toStringValue(this.getResult(), HAPSerializationFormat.JSON));
		jsonMap.put(HAPDataExpressionUnit.VARIABLEINFOS, HAPManagerSerialize.getInstance().toStringValue(this.getVariablesInfo(), HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSION, HAPManagerSerialize.getInstance().toStringValue(this.getExpression(), HAPSerializationFormat.JSON));
		
		
	}

}
