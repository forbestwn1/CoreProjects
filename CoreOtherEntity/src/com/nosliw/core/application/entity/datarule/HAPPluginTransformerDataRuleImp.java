package com.nosliw.core.application.entity.datarule;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractiveTaskValuePort;

public abstract class HAPPluginTransformerDataRuleImp implements HAPPluginTransformerDataRule{

	protected HAPInteractiveExpression buildValuePortGroupForRuleTaskBrickExpression(HAPDataRule dataRule, HAPBrick brick, HAPDomainValueStructure valueStructureDomian) {
		HAPInteractiveExpression interactive = HAPUtilityDataRule.buildExpressionInterface(dataRule.getDataCriteria());
		HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveExpression(Pair.of(brick.getInternalValuePorts(), brick.getExternalValuePorts()), interactive, valueStructureDomian);
		return interactive;
	}
	
	protected HAPInteractiveTask buildValuePortGroupForRuleTaskBrickTask(HAPDataRule dataRule, HAPBrick brick, HAPDomainValueStructure valueStructureDomian) {
		HAPInteractiveTask interactive = HAPUtilityDataRule.buildTaskInterface(dataRule.getDataCriteria());
		HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveTask(Pair.of(brick.getInternalValuePorts(), brick.getExternalValuePorts()), interactive, valueStructureDomian);
		return interactive;
	}
	
}
