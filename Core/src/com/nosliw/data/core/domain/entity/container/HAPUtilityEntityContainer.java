package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPInfoBrickType;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityEntityContainer {

	public static void newComplexEntityContainerAttribute(HAPIdEntityInDomain parentEntityId, String attrName, String childEntityType, HAPConfigureParentRelationComplex childRelationConfigure, HAPContextParser parserContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPIdEntityInDomain containerEntityId = HAPUtilityEntityDefinition.newTransparentAttribute(parentEntityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTAINERCOMPLEX, attrName, parserContext, runtimeEnv);
		HAPDefinitionEntityContainerComplex containerEntity = (HAPDefinitionEntityContainerComplex)parserContext.getGlobalDomain().getEntityInfoDefinition(containerEntityId).getEntity();
		containerEntity.setElementValueTypeInfo(new HAPInfoBrickType(childEntityType, true));
		if(childRelationConfigure!=null)   containerEntity.setElementRelationConfigure(childRelationConfigure);
	}

	public static void newSimpleEntityContainerAttribute(HAPIdEntityInDomain parentEntityId, String attrName, String childEntityType, HAPContextParser parserContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPIdEntityInDomain containerEntityId = HAPUtilityEntityDefinition.newTransparentAttribute(parentEntityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTAINERSIMPLE, attrName, parserContext, runtimeEnv);
		HAPDefinitionEntityContainerSimple containerEntity = (HAPDefinitionEntityContainerSimple)parserContext.getGlobalDomain().getEntityInfoDefinition(containerEntityId).getEntity();
		containerEntity.setElementValueTypeInfo(childEntityType);
	}

	public static String addComplexElementAttribute(HAPIdEntityInDomain containerEntityId, HAPIdEntityInDomain elementId, HAPContextParser parserContext) {
		HAPDefinitionEntityContainerComplex containerEntity = (HAPDefinitionEntityContainerComplex)parserContext.getGlobalDomain().getEntityInfoDefinition(containerEntityId).getEntity();
		String attrName = containerEntity.addElementAttribute(elementId, true);
		HAPUtilityEntityDefinition.buildParentRelation(elementId, containerEntityId, containerEntity.getElementRelationConfigure(), parserContext);
		return attrName;
	}
	
	public static String addComplexElementAttribute(HAPIdEntityInDomain containerEntityId, HAPIdEntityInDomain elementId, HAPConfigureParentRelationComplex childRelationConfigure, HAPContextParser parserContext) {
		HAPDefinitionEntityContainerComplex containerEntity = (HAPDefinitionEntityContainerComplex)parserContext.getGlobalDomain().getEntityInfoDefinition(containerEntityId).getEntity();
		String attrName = containerEntity.addElementAttribute(elementId, true);
		
		HAPConfigureParentRelationComplex relationConfigure = childRelationConfigure;
		if(relationConfigure==null)   relationConfigure = containerEntity.getElementRelationConfigure();
		
		HAPUtilityEntityDefinition.buildParentRelation(elementId, containerEntityId, relationConfigure, parserContext);
		return attrName;
	}
}
