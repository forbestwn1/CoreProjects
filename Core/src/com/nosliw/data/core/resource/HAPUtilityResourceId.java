package com.nosliw.data.core.resource;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.component.HAPLocalReferenceBase;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityResourceId {

	public static String buildResourceIdLiterate(HAPResourceId resourceId) {
		return HAPUtilityNamingConversion.cascadeLevel2(new String[]{resourceId.getResourceType(), buildResourceCoreIdLiterate(resourceId)});
	}
	
	//build literate for id part
	public static String buildResourceCoreIdLiterate(HAPResourceId resourceId) {
		StringBuffer out = new StringBuffer();

		//prefix according to structure
		String structure = resourceId.getStructure();
		if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) out.append(HAPConstantShared.RESOURCEID_LITERATE_STARTER_SIMPLE);
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) out.append(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED);
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_DYNAMIC)) out.append(HAPConstantShared.RESOURCEID_LITERATE_STARTER_DYNAMIC);
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) out.append(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL);
		
		//append core id
		out.append(resourceId.getCoreIdLiterate());
		
//		out.append(HAPConstantShared.SEPERATOR_RESOURCEID_START).append(resourceId.getStructure()).append(HAPConstantShared.SEPERATOR_RESOURCEID_STRUCTURE).append(resourceId.getCoreIdLiterate());
		return out.toString();
	}
	
	public static String[] parseResourceIdLiterate(String idLiterate) {
		return HAPUtilityNamingConversion.parseLevel2(idLiterate);
	}

	public static HAPResourceId buildResourceIdByLiterate(String resourceType, String literate) {
		return buildResourceIdByLiterate(resourceType, literate, false);
	}
	
	public static boolean isResourceIdLiterate(String literate) {
		if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED))  return true;
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL))   return true;
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_DYNAMIC))  return true;
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_SIMPLE))  return true;
		return false;
	}
	
	public static HAPResourceId buildResourceIdByLiterate(String resourceType, String literate, boolean strict) {
		String structure = null;
		String coreIdLiterate = literate;
		
		if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED)) {
			//embeded resource id
			structure = HAPConstantShared.RESOURCEID_TYPE_EMBEDED;
			coreIdLiterate = literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED.length());
		}
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL)){
			//local resource id
			structure = HAPConstantShared.RESOURCEID_TYPE_LOCAL;
			coreIdLiterate = literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL.length());
		}
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_DYNAMIC)){
			//local resource id
			structure = HAPConstantShared.RESOURCEID_TYPE_DYNAMIC;
			coreIdLiterate = literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_DYNAMIC.length());
		}
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_SIMPLE)){
			//simple
			structure = HAPConstantShared.RESOURCEID_TYPE_SIMPLE;
			coreIdLiterate = literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_SIMPLE.length());
		}
		else if(!strict) {
			//if no sign, then by default simple
			structure = HAPConstantShared.RESOURCEID_TYPE_SIMPLE;
			coreIdLiterate = literate;
		}

		if(structure==null)  return null;
		
		HAPResourceId out = newInstanceByType(resourceType, structure);
		out.buildCoreIdByLiterate(coreIdLiterate);
		return out;
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
	

	
	
	
	public static HAPInfoResourceLocation getResourceLocationInfo(HAPResourceIdSimple resourceId) {
		File file = null;
		
		String localFile = isFileBased(resourceId);
		if(localFile!=null) {
			//if file base
			return new HAPInfoResourceLocation(new File(localFile), new HAPLocalReferenceBase(localFile));
		}
		else {
			//check single file first
			String basePath = HAPSystemFolderUtility.getResourceFolder(resourceId.getResourceType());
			file = new File(basePath+resourceId.getId()+".res");
			if(!file.exists()) {
				basePath = basePath+resourceId.getId()+"/";
				file = new File(basePath+"/main.res");
				if(!file.exists()) {
					HAPErrorUtility.invalid("Cannot find module resource " + resourceId.getId());
				}
			}
			return new HAPInfoResourceLocation(file, new HAPLocalReferenceBase(basePath));
		}
	}
	
	public static List<HAPResourceDependency> buildResourceDependentFromResourceId(List<HAPResourceIdSimple> ids){
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		for(HAPResourceIdSimple id : ids) 	out.add(new HAPResourceDependency(id));
		return out;
	}


	public static boolean isLocalReference(HAPResourceId resourceId) {
		//kkk
		if(resourceId.getResourceType()==null)   return true;
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
