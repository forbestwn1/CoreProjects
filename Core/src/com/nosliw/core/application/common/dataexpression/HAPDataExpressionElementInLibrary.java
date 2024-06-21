package com.nosliw.core.application.common.dataexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPWithInteractive;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPWithValuePortGroup;

@HAPEntityWithAttribute
public class HAPDataExpressionElementInLibrary extends HAPEntityInfoImp implements HAPWithInteractive, HAPDataExpressionUnit, HAPWithValuePortGroup{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	private HAPDataExpression m_dataExpression;
	
	private HAPInteractiveExpression m_interactive;
	
	private HAPContainerVariableCriteriaInfo m_variableInfo;
	
	public HAPDataExpressionElementInLibrary() {
		this.m_variableInfo = new HAPContainerVariableCriteriaInfo();
	}
	
	public HAPDataExpression getExpression() {	return m_dataExpression;	}
	public void setExpression(HAPDataExpression dataExpression) {	this.m_dataExpression = dataExpression;	}

	public HAPInteractiveExpression getInteractive() {    return this.m_interactive;      }
	public void setInteractive(HAPInteractiveExpression interactive) {    this.m_interactive = interactive;     }
	
	@Override
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return this.m_variableInfo;  }
	
	@Override
	public HAPGroupValuePorts getExternalValuePortGroup() {   return this.m_interactive.getExternalValuePortGroup();  }
	
	@Override
	public HAPGroupValuePorts getInternalValuePortGroup() {   return this.m_interactive.getInternalValuePortGroup();  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(INTERACTIVE, HAPManagerSerialize.getInstance().toStringValue(this.m_interactive, HAPSerializationFormat.JSON));
		jsonMap.put(HAPDataExpressionUnit.VARIABLEINFOS, HAPManagerSerialize.getInstance().toStringValue(this.getVariablesInfo(), HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSION, HAPManagerSerialize.getInstance().toStringValue(this.getExpression(), HAPSerializationFormat.JSON));
	}

}
