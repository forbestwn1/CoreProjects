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
	
	public static HAPContainerEntityExecutable buildExecutionContainer(String containerType) {
		HAPContainerEntityExecutable out = null;
		if(HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_SET.equals(containerType)) {
			out = new HAPContainerEntityExecutableSet();
		}
		else if(HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_LIST.equals(containerType)) {
			out = new HAPContainerEntityExecutableList();
		}
		return out;
	}

	public static HAPContainerEntityDefinition buildDefinitionContainer(String containerType) {
		HAPContainerEntityDefinition out = null;
		if(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET.equals(containerType)) {
			out = new HAPContainerEntityDefinitionSet();
		}
		else if(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST.equals(containerType)) {
			out = new HAPContainerEntityDefinitionList();
		}
		return out;
	}
}
