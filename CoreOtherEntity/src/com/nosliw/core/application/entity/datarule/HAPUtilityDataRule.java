package com.nosliw.core.application.entity.datarule;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.datadefinition.HAPDataDefinitionReadonly;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionParm;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionResult;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPDataTypeCriteriaId;

public class HAPUtilityDataRule {

	public static HAPInteractiveExpression buildExpressionInterface(HAPDataTypeCriteria dataCriteria) {
		HAPDefinitionParm parm = new HAPDefinitionParm();
		parm.getDataDefinition().setCriteria(dataCriteria);
		parm.setName(HAPConstantShared.NAME_ROOT_DATA);
		
		HAPDefinitionResult result = new HAPDefinitionResult();
		result.setDataDefinition(new HAPDataDefinitionReadonly());
		result.getDataDefinition().setCriteria(new HAPDataTypeCriteriaId(new HAPDataTypeId("boolean", "1.0.0"), null));

		HAPInteractiveExpression interactive = new HAPInteractiveExpression(List.of(parm), result);
        return interactive;		
	}
	
	
}
