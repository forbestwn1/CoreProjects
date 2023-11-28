package com.nosliw.data.core.domain.entity.task;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorTask extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorTask() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASK, HAPExecutableEntityTask.class);
	}

	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {	
		HAPDefinitionEntityTask taskEntityDef = (HAPDefinitionEntityTask)this.getEntityDefinition(complexEntityExecutable, processContext);
		HAPExecutableEntityTask taskEntityExe = (HAPExecutableEntityTask)complexEntityExecutable;
		taskEntityExe.setImpEntityType(taskEntityDef.getImpEntityType());
	}
}
