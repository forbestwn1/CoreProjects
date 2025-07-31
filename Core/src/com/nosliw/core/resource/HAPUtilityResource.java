package com.nosliw.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPResourceDataBrick;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;
import com.nosliw.core.system.HAPSystemUtility;

public class HAPUtilityResource {

	public static HAPResource getResource(HAPResourceId resourceId, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		return resourceMan.getResources(Lists.asList(resourceId, new HAPResourceId[0]), runtimeInfo).getLoadedResource(resourceId);
	}

	public static HAPBrick getResourceDataBrick(HAPResourceId resourceId, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPResourceDataBrick brickResourceData = (HAPResourceDataBrick)resourceMan.getResources(Lists.asList(resourceId, new HAPResourceId[0]), runtimeInfo).getLoadedResource(resourceId).getResourceData();
		return brickResourceData.getBrick();
	}
	public static HAPResourceDataBrick getResourceData(HAPResourceId resourceId, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPResourceDataBrick brickResourceData = (HAPResourceDataBrick)resourceMan.getResources(Lists.asList(resourceId, new HAPResourceId[0]), runtimeInfo).getLoadedResource(resourceId).getResourceData();
		return brickResourceData;
	}

	public static Map<String, Object> buildResourceLoadPattern(HAPResourceId resourceId, Map<String, Object> info) {
		if(info==null) {
			info = new LinkedHashMap<String, Object>();
		}
		if(isLoadResoureByFile(resourceId.getResourceTypeId().getResourceType())) {
			info.put(HAPConfigureResource.RESOURCE_LOADPATTERN, HAPConfigureResource.RESOURCE_LOADPATTERN_FILE);
		}
		return info;
	}

	public final static String LOADRESOURCEBYFILE_MODE_NEVER = "never";
	public final static String LOADRESOURCEBYFILE_MODE_ALWAYS = "always";
	public final static String LOADRESOURCEBYFILE_MODE_DEPENDS = "depends";
	

	private final static Set<String> loadResourceByFile = HAPSystemUtility.getLoadResoureByFile();
	public static boolean isLoadResoureByFile(String resourceType) {
		String mode = HAPSystemUtility.getLoadResourceByFileMode();
		if(mode==null) {
			mode = LOADRESOURCEBYFILE_MODE_DEPENDS;
		}
		if(LOADRESOURCEBYFILE_MODE_NEVER.equals(resourceType)) {
			return false;
		}
		if(LOADRESOURCEBYFILE_MODE_ALWAYS.equals(resourceType)) {
			return true;
		}
		return loadResourceByFile.contains(resourceType);
	}
	

	public static HAPResourceDefinition1 solidateResource(HAPEntityOrReference entityOrReference, HAPRuntimeEnvironment runtimeEnv) {
		HAPResourceDefinition1 out = null;
		if(entityOrReference.getEntityOrReferenceType().equals(HAPConstantShared.REFERENCE)) {
			out = runtimeEnv.getResourceDefinitionManager().getLocalResourceDefinition((HAPResourceId)entityOrReference);
		}
		else {
			out = (HAPResourceDefinition1)entityOrReference;
		}
		return out;
	}
	
	public static Object getImpliedEntity(HAPResourceId resourceId, Object container, HAPManagerResourceDefinition resourceDefMan) {
		Object out = null;
		if(resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE) && HAPUtilityResourceId.isLocalReference(resourceId)) {
			//for reference local, get it from container
			String id = ((HAPResourceIdSimple)resourceId).getId();
			if(container instanceof HAPDefinitionEntityContainer) {
				out = ((HAPDefinitionEntityContainer)container).getElementResourceDefinition(id);
			}
			else if(container instanceof HAPWithEntityElement) {
				out = ((HAPWithEntityElement)container).getEntityElement(id);
			}
			else if(container instanceof Map) {
				out = ((Map)container).get(id);
			}
		}
		else {
			out = resourceDefMan.getLocalResourceDefinition(resourceId);
		}
		return out;
	}	

	public static HAPResourceDefinition1 getImpliedResourceDefinition(HAPResourceId resourceId, HAPDefinitionEntityContainer container, HAPManagerResourceDefinition resourceDefMan) {
		HAPResourceDefinition1 resourceDef = null;
		if(resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE) && HAPUtilityResourceId.isLocalReference(resourceId)) {
			//for reference local, get it from container
			resourceDef = container.getElementResourceDefinition(((HAPResourceIdSimple)resourceId).getId());
		}
		else {
			resourceDef = resourceDefMan.getLocalResourceDefinition(resourceId);
		}
		return resourceDef;
	}	
	
}
