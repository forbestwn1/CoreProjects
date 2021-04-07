package com.nosliw.data.core.resource;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJS;
import com.nosliw.data.core.system.HAPSystemFolderUtility;
import com.nosliw.data.core.system.HAPSystemUtility;

public class HAPUtilityResourceId {

	public final static String LOADRESOURCEBYFILE_MODE_NEVER = "never";
	public final static String LOADRESOURCEBYFILE_MODE_ALWAYS = "always";
	public final static String LOADRESOURCEBYFILE_MODE_DEPENDS = "depends";
	

	public static HAPInfoResourceLocation getResourceLocationInfo(HAPResourceIdSimple resourceId) {
		File file = null;
		//check single file first
		String basePath = HAPSystemFolderUtility.getResourceFolder(resourceId.getType());
		file = new File(basePath+resourceId.getId()+".res");
		if(!file.exists()) {
			basePath = basePath+resourceId.getId()+"/";
			file = new File(basePath+"/main.res");
			if(!file.exists()) {
				HAPErrorUtility.invalid("Cannot find module resource " + resourceId.getId());
			}
		}
		return new HAPInfoResourceLocation(file, basePath);
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

	private final static Set<String> loadResourceByFile = HAPSystemUtility.getLoadResoureByFile();
	public static boolean isLoadResoureByFile(String resourceType) {
		String mode = HAPSystemUtility.getLoadResourceByFileMode();
		if(mode==null)  mode = LOADRESOURCEBYFILE_MODE_DEPENDS;
		if(LOADRESOURCEBYFILE_MODE_NEVER.equals(resourceType))  return false;
		if(LOADRESOURCEBYFILE_MODE_ALWAYS.equals(resourceType))  return true;
		return loadResourceByFile.contains(resourceType);
	}
	
	//build literate for id part
	public static String buildResourceCoreIdLiterate(HAPResourceId resourceId) {
		StringBuffer out = new StringBuffer();
		out.append(HAPConstantShared.SEPERATOR_RESOURCEID_START).append(resourceId.getStructure()).append(HAPConstantShared.SEPERATOR_RESOURCEID_STRUCTURE).append(resourceId.getCoreIdLiterate());
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
			out[0] = getDefaultResourceStructure();
			out[1] = coreIdLiterate;
		}
		return out;
	}
	
	public static HAPResourceId buildResourceCoreIdByLiterate(String resourceType, String literate) {
		if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED)){
			//embeded resource id
			return buildEmbededResourceIdByLiterate(resourceType, literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED.length()));
		}
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL)){
			//local resource id
			return buildLocalResourceIdByLiterate(resourceType, literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL.length()));
		}
		else {
			HAPResourceIdSimple simpleResourceId = new HAPResourceIdSimple(resourceType);
			simpleResourceId.buildObjectByLiterate(literate);
			return simpleResourceId;
		}
	}
	
	private static HAPResourceIdLocal buildLocalResourceIdByLiterate(String resourceType, String literate) {
		HAPResourceIdLocal localResourceId = (HAPResourceIdLocal)newInstanceByType(resourceType, HAPConstantShared.RESOURCEID_TYPE_LOCAL);
		localResourceId.setName(literate);
		return localResourceId;
	}
	
	private static HAPResourceIdEmbeded buildEmbededResourceIdByLiterate(String resourceType, String literate) {
		HAPResourceIdEmbeded embededResourceId = (HAPResourceIdEmbeded)newInstanceByType(resourceType, HAPConstantShared.RESOURCEID_TYPE_EMBEDED);
		String[] idSegs = HAPNamingConversionUtility.parseLevel3(literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED.length()));
		HAPResourceIdSimple parentResourceId = new HAPResourceIdSimple();
		parentResourceId.buildObjectByLiterate(idSegs[0]);
		embededResourceId.setParentResourceId(parentResourceId);
		embededResourceId.setPath(idSegs[1]);
		return embededResourceId;
	}
	
	public static HAPResourceId newInstanceByType(String resourceType, String structure) {
		HAPResourceId out = null;
		if(structure==null)   structure = getDefaultResourceStructure();
		if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
			out = new HAPResourceIdSimple(resourceType);
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
			out = new HAPResourceIdEmbeded(resourceType);
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_DYNAMIC)) {
			out = new HAPResourceIdDynamic(resourceType);
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
			out = new HAPResourceIdLocal(resourceType);
		}
		return out;
	}
	

	public static String[] parseResourceIdLiterate(String idLiterate) {
		return HAPNamingConversionUtility.parseLevel2(idLiterate);
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
