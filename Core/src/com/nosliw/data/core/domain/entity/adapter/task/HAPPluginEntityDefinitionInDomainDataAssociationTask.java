package com.nosliw.data.core.domain.entity.adapter.task;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainDataAssociationTask  extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainDataAssociationTask(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONTASK, HAPDefinitionEntityDataAssociationTask.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		HAPDefinitionEntityDataAssociationTask entity = (HAPDefinitionEntityDataAssociationTask)this.getEntity(entityId, parserContext);
		HAPDefinitionGroupDataAssociationForTask da = new HAPDefinitionGroupDataAssociationForTask();
		da.buildObject(jsonValue, HAPSerializationFormat.JSON);
		entity.setDataAssciation(da);
	}
}
