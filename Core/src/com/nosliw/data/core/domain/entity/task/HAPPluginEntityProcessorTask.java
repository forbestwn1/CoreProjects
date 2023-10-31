package com.nosliw.data.core.domain.entity.task;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorTask extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorTask() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASK, HAPExecutableEntityTask.class);
	}

	@Override
	public void processEntity(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {	
		Pair<HAPDefinitionEntityInDomainComplex,HAPExecutableEntityComplex> entityPair = this.getEntityPair(complexEntityExecutableId, processContext);
		HAPDefinitionEntityTask taskEntityDef = (HAPDefinitionEntityTask)entityPair.getLeft();
		HAPExecutableEntityTask taskEntityExe = (HAPExecutableEntityTask)entityPair.getRight();
		taskEntityExe.setImpEntityType(taskEntityDef.getImpEntityType());
	}
	
}
