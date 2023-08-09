package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPInfoValueType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityEntityContainer {

	public static void newComplexEntityContainerAttribute(HAPIdEntityInDomain parentEntityId, String attrName, String childEntityType, HAPContextParser parserContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPIdEntityInDomain containerEntityId = HAPUtilityEntityDefinition.newTransparentAttribute(parentEntityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_COMPLEXCONTAINER, attrName, parserContext, runtimeEnv);
		HAPDefinitionEntityComplexContainer containerEntity = (HAPDefinitionEntityComplexContainer)parserContext.getGlobalDomain().getEntityInfoDefinition(containerEntityId).getEntity();
		containerEntity.setElementValueTypeInfo(new HAPInfoValueType(childEntityType, true));
	}
	
}
