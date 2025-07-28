package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

@HAPEntityWithAttribute
public class HAPExecutableEntityExpressionScript extends HAPExecutableEntityComplex{
	
	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";
	
	public HAPExecutableEntityExpressionScript() {
		this.setAttributeValueObject(VARIABLEINFOS, new HAPContainerVariableInfo());
	}

	public void setVariablesInfo(HAPContainerVariableInfo varInfo) {  this.setAttributeValueObject(VARIABLEINFOS, varInfo);  }
	public HAPContainerVariableInfo getVariablesInfo() {   return (HAPContainerVariableInfo)this.getAttributeValue(VARIABLEINFOS);    }
}
