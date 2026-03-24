package com.nosliw.core.application.entity.datarule;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.brick.interactive.interfacee.expression.HAPBlockInteractiveInterfaceExpressionImp;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTaskImp;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractiveTaskValuePort;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;

public abstract class HAPPluginTransformerDataRuleImp implements HAPPluginTransformerDataRule{

	protected void buildInterfaceTask(HAPDataRule dataRule, HAPWithBlockInteractiveTask withTaskBrick, HAPDomainValueStructure valueStructureDomian) {
		HAPBlockInteractiveInterfaceTaskImp taskInterfaceBlock = new HAPBlockInteractiveInterfaceTaskImp();
		taskInterfaceBlock.setValue(this.buildValuePortGroupForRuleTaskBrickTask(dataRule, (HAPBrick)withTaskBrick, valueStructureDomian));
		withTaskBrick.setTaskInterface(taskInterfaceBlock);
	}
	
	protected void buildInterfaceExpression(HAPDataRule dataRule, HAPWithBlockInteractiveExpression withExpressionBrick, HAPDomainValueStructure valueStructureDomian) {
		HAPBlockInteractiveInterfaceExpressionImp expressionInterfaceBlock = new HAPBlockInteractiveInterfaceExpressionImp();
		expressionInterfaceBlock.setValue(this.buildValuePortGroupForRuleTaskBrickExpression(dataRule, (HAPBrick)withExpressionBrick, valueStructureDomian));
		withExpressionBrick.setExpressionInterface(expressionInterfaceBlock);
	}
	
	protected HAPInteractiveTask buildValuePortGroupForRuleTaskBrickTask(HAPDataRule dataRule, HAPBrick brick, HAPDomainValueStructure valueStructureDomian) {
		HAPInteractiveTask interactive = HAPUtilityDataRule.buildTaskInterface(dataRule.getDataCriteria());
		HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveTask(Pair.of(brick.getInternalValuePorts(), brick.getExternalValuePorts()), interactive, valueStructureDomian);
		return interactive;
	}

	protected HAPInteractiveExpression buildValuePortGroupForRuleTaskBrickExpression(HAPDataRule dataRule, HAPBrick brick, HAPDomainValueStructure valueStructureDomian) {
		HAPInteractiveExpression interactive = HAPUtilityDataRule.buildExpressionInterface(dataRule.getDataCriteria());
		HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveExpression(Pair.of(brick.getInternalValuePorts(), brick.getExternalValuePorts()), interactive, valueStructureDomian);
		return interactive;
	}
	
}
