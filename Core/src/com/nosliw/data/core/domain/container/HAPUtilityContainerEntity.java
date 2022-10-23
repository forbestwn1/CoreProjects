package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;

public class HAPUtilityContainerEntity {

	public static HAPElementContainerExecutable buildExecutableContainerElement(HAPElementContainerDefinition eleDef, String eleId) {
		HAPElementContainerExecutable out = new HAPElementContainerExecutable();
		out.setElementId(eleId);
		out.setInfo(eleDef.getInfo().cloneEntityInfo());
		return out;
	}
	
	public static HAPContainerEntityExecutable buildExecutableContainer(HAPContainerEntityDefinition defContainer, HAPManagerDomainEntityDefinition domainEntityDefMan) {
		return buildExecutionContainer(defContainer.getContainerType(), defContainer.getElementType(), domainEntityDefMan);
	}
	
	public static HAPContainerEntityExecutable buildExecutionContainer(String containerType, String elementType, HAPManagerDomainEntityDefinition domainEntityDefMan) {
		HAPContainerEntityExecutable out = null;
		if(HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_SET.equals(containerType)) {
			out = new HAPContainerEntityExecutableList(elementType, domainEntityDefMan.isComplexEntity(elementType));
		}
		else if(HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_LIST.equals(containerType)) {
			out = new HAPContainerEntityExecutableSet(elementType, domainEntityDefMan.isComplexEntity(elementType));
		}
		return out;
	}

	public static HAPContainerEntityDefinition buildDefinitionContainer(String containerType, String elementType, HAPManagerDomainEntityDefinition domainEntityDefMan) {
		HAPContainerEntityDefinition out = null;
		if(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET.equals(containerType)) {
			out = new HAPContainerEntityDefinitionSet(elementType, domainEntityDefMan.isComplexEntity(elementType));
		}
		else if(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST.equals(containerType)) {
			out = new HAPContainerEntityDefinitionList(elementType, domainEntityDefMan.isComplexEntity(elementType));
		}
		return out;
	}
}
