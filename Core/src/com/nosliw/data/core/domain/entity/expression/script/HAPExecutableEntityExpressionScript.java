package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableEntityComplexWithDataExpressionGroup;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;

@HAPEntityWithAttribute
public class HAPExecutableEntityExpressionScript extends HAPExecutableEntityComplexWithDataExpressionGroup{
	
	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";
	
	public HAPExecutableEntityExpressionScript() {
		this.setAttributeValueObject(VARIABLEINFOS, new HAPContainerVariableCriteriaInfo());
	}

	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varInfo) {  this.setAttributeValueObject(VARIABLEINFOS, varInfo);  }
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return (HAPContainerVariableCriteriaInfo)this.getAttributeValue(VARIABLEINFOS);    }
}
