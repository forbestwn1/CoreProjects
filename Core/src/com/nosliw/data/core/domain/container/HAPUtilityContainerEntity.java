package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPUtilityContainerEntity {

	public static HAPElementContainerExecutable buildExecutableContainerElement(HAPElementContainerDefinition eleDef, String eleId) {
		HAPElementContainerExecutable out = new HAPElementContainerExecutable();
		out.setElementId(eleId);
		out.setInfo(eleDef.getInfo().cloneEntityInfo());
		return out;
	}
	
	public static HAPContainerEntityExecutable buildExecutableContainer(HAPContainerEntityDefinition defContainer) {
		HAPContainerEntityExecutable out = null;
		String containerType = defContainer.getContainerType();
		if(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST.equals(containerType)) {
			out = new HAPContainerEntityExecutableList(defContainer.getElementType());
		}
		else if(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET.equals(containerType)) {
			out = new HAPContainerEntityExecutableSet(defContainer.getElementType());
		}
		out.setExtraInfo(defContainer.getExtraInfo().cloneEntityInfo());
		return out;
	}
	
	public static HAPContainerEntityExecutable buildExecutionContainer(String containerType, String elementType) {
		HAPContainerEntityExecutable out = null;
		if(HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_SET.equals(containerType)) {
			out = new HAPContainerEntityExecutableList(elementType);
		}
		else if(HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_LIST.equals(containerType)) {
			out = new HAPContainerEntityExecutableSet(elementType);
		}
		return out;
	}

	public static HAPContainerEntityDefinition buildDefinitionContainer(String containerType, String elementType) {
		HAPContainerEntityDefinition out = null;
		if(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET.equals(containerType)) {
			out = new HAPContainerEntityDefinitionSet(elementType);
		}
		else if(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST.equals(containerType)) {
			out = new HAPContainerEntityDefinitionList(elementType);
		}
		return out;
	}

}
