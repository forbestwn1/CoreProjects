package com.nosliw.data.core.resource;

import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityResource {

	public static HAPResourceDefinition solidateResource(HAPEntityOrReference entityOrReference, HAPRuntimeEnvironment runtimeEnv) {
		HAPResourceDefinition out = null;
		if(entityOrReference.getEntityOrReferenceType().equals(HAPConstantShared.REFERENCE)) {
			out = runtimeEnv.getResourceDefinitionManager().getResourceDefinition((HAPResourceId)entityOrReference);
		}
		else {
			out = (HAPResourceDefinition)entityOrReference;
		}
		return out;
	}
	
	public static Object getImpliedEntity(HAPResourceId resourceId, Object container, HAPManagerResourceDefinition resourceDefMan) {
		Object out = null;
		if(resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE) && HAPUtilityResourceId.isLocalReference(resourceId)) {
			//for reference local, get it from container
			String id = ((HAPResourceIdSimple)resourceId).getId();
			if(container instanceof HAPResourceDefinitionContainer) {
				out = ((HAPResourceDefinitionContainer)container).getElementResourceDefinition(id);
			}
			else if(container instanceof HAPWithEntityElement) {
				out = ((HAPWithEntityElement)container).getEntityElement(id);
			}
			else if(container instanceof Map) {
				out = ((Map)container).get(id);
			}
		}
		else {
			out = resourceDefMan.getResourceDefinition(resourceId);
		}
		return out;
	}	

	public static HAPResourceDefinition getImpliedResourceDefinition(HAPResourceId resourceId, HAPResourceDefinitionContainer container, HAPManagerResourceDefinition resourceDefMan) {
		HAPResourceDefinition resourceDef = null;
		if(resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE) && HAPUtilityResourceId.isLocalReference(resourceId)) {
			//for reference local, get it from container
			resourceDef = container.getElementResourceDefinition(((HAPResourceIdSimple)resourceId).getId());
		}
		else {
			resourceDef = resourceDefMan.getResourceDefinition(resourceId);
		}
		return resourceDef;
	}	
	
}
