package com.nosliw.core.application.entity.datarule.enum1;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.brick.task.script.task.HAPBlockTaskTaskScriptImp;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.entity.datarule.HAPDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginTransformerDataRuleImp;
import com.nosliw.core.resource.HAPFactoryResourceId;

public class HAPPluginTransformerDataRuleEnum extends HAPPluginTransformerDataRuleImp{

	public HAPPluginTransformerDataRuleEnum() {
	}
	
	@Override
	public HAPEntityOrReference transformDataRule(HAPDataRule dataRule, HAPDomainValueStructure valueStructureDomian) {
		HAPBlockTaskTaskScriptImp out = new HAPBlockTaskTaskScriptImp();
		
		HAPInteractiveTask interactive = this.buildValuePortGroupForRuleTaskBrickTask(dataRule, out, valueStructureDomian); 
		
		out.setScriptResourceId(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, "1.0.0", "task_datavalidation_enum"));

		out.setAttributeValueWithValue(, dataRule);;
		
		return out;
	}

}
