package com.nosliw.data.core.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJS;
import com.nosliw.data.core.system.HAPSystemUtility;

public class HAPResourceUtility {

	public final static String LOADRESOURCEBYFILE_MODE_NEVER = "never";
	public final static String LOADRESOURCEBYFILE_MODE_ALWAYS = "always";
	public final static String LOADRESOURCEBYFILE_MODE_DEPENDS = "depends";
	
	
	private final static Set<String> loadResourceByFile = HAPSystemUtility.getLoadResoureByFile();
	public static boolean isLoadResoureByFile(String resourceType) {
		String mode = HAPSystemUtility.getLoadResourceByFileMode();
		if(mode==null)  mode = LOADRESOURCEBYFILE_MODE_DEPENDS;
		if(LOADRESOURCEBYFILE_MODE_NEVER.equals(resourceType))  return false;
		if(LOADRESOURCEBYFILE_MODE_ALWAYS.equals(resourceType))  return true;
		return loadResourceByFile.contains(resourceType);
	}
	
	public static List<HAPResourceDependency> buildResourceDependentFromResourceId(List<HAPResourceIdSimple> ids){
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		for(HAPResourceIdSimple id : ids) 	out.add(new HAPResourceDependency(id));
		return out;
	}

	public static Map<String, Object> buildResourceLoadPattern(HAPResourceId resourceId, Map<String, Object> info) {
		if(info==null)   info = new LinkedHashMap<String, Object>();
		if(isLoadResoureByFile(resourceId.getType())) {
			info.put(HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN, HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN_FILE);
		}
		return info;
	}

	//build literate for id part
	public static String buildResourceCoreIdLiterate(HAPResourceId resourceId) {
		StringBuffer out = new StringBuffer();
		out.append(HAPConstantShared.SEPERATOR_RESOURCEID_START).append(resourceId.getStructure()).append(HAPConstantShared.SEPERATOR_RESOURCEID_STRUCTURE).append(resourceId.getIdLiterate());
		return out.toString();
	}
	
	public static String[] parseResourceCoreIdLiterate(String coreIdLiterate) {
		String[] out = new String[2];
		if(coreIdLiterate.startsWith(HAPConstantShared.SEPERATOR_RESOURCEID_START)) {
			int index = coreIdLiterate.indexOf(HAPConstantShared.SEPERATOR_RESOURCEID_STRUCTURE, HAPConstantShared.SEPERATOR_RESOURCEID_START.length());
			out[0] = coreIdLiterate.substring(HAPConstantShared.SEPERATOR_RESOURCEID_START.length(), index);
			out[1] = coreIdLiterate.substring(index+1);
		}
		else {
			//simple structure
			out[0] = HAPResourceUtility.getDefaultResourceStructure();
			out[1] = coreIdLiterate;
		}
		return out;
	}
	
	public static String[] parseResourceIdLiterate(String idLiterate) {
		return HAPNamingConversionUtility.parseLevel2(idLiterate);
	}

	public static Object getImpliedEntity(HAPResourceId resourceId, Object container, HAPManagerResourceDefinition resourceDefMan) {
		Object out = null;
		if(resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE) && isLocalReference(resourceId)) {
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
		if(resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE) && isLocalReference(resourceId)) {
			//for reference local, get it from container
			resourceDef = container.getElementResourceDefinition(((HAPResourceIdSimple)resourceId).getId());
		}
		else {
			resourceDef = resourceDefMan.getResourceDefinition(resourceId);
		}
		return resourceDef;
	}	
	
	public static boolean isLocalReference(HAPResourceId resourceId) {
		//kkk
		if(resourceId.getType()==null)   return true;
		return false;
	}
	
	public static HAPResourceId buildLocalReferenceResourceId(String name) {
		HAPResourceIdSimple out = new HAPResourceIdSimple();
		out.setId(name);
		return out;
	}
	
	public static String getDefaultResourceStructure() {    return HAPConstantShared.RESOURCEID_TYPE_SIMPLE;     }
	
	private static final String FILEBASERESOURCE_STARTER = "file_";
	public static HAPResourceIdSimple createFileBaseResourceId(String resourceType, String fileName) {
		try {
			return new HAPResourceIdSimple(resourceType, FILEBASERESOURCE_STARTER+URLEncoder.encode(fileName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String isFileBased(HAPResourceIdSimple resourceId) {
		String id = resourceId.getId();
		if(id.startsWith(FILEBASERESOURCE_STARTER)) {
			try {
				return URLDecoder.decode(id.substring(FILEBASERESOURCE_STARTER.length()), "UTF-8") ;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
