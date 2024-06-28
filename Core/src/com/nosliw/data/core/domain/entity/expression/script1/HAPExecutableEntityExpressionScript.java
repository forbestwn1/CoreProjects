package com.nosliw.data.core.domain.entity.expression.script1;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.valueport.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

@HAPEntityWithAttribute
public class HAPExecutableEntityExpressionScript extends HAPExecutableEntityComplex{
	
	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";
	
	public HAPExecutableEntityExpressionScript() {
		this.setAttributeValueObject(VARIABLEINFOS, new HAPContainerVariableCriteriaInfo());
	}

	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varInfo) {  this.setAttributeValueObject(VARIABLEINFOS, varInfo);  }
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return (HAPContainerVariableCriteriaInfo)this.getAttributeValue(VARIABLEINFOS);    }
}
