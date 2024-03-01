package com.nosliw.data.core.domain.entity.adapter.interactive;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainDataAssociationInteractive  extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainDataAssociationInteractive(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONINTERACTIVE, HAPDefinitionEntityDataAssociationInteractive.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		HAPDefinitionEntityDataAssociationInteractive entity = (HAPDefinitionEntityDataAssociationInteractive)this.getEntity(entityId, parserContext);
		HAPDefinitionGroupDataAssociationForTask da = new HAPDefinitionGroupDataAssociationForTask();
		da.buildObject(jsonValue, HAPSerializationFormat.JSON);
		entity.setDataAssciation(da);
	}
}
