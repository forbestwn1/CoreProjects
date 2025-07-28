package com.nosliw.core.resource;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityResourceId {

	public static HAPInfoResourceIdNormalize normalizeResourceId(HAPResourceId resourceId) {
		HAPInfoResourceIdNormalize out = null;
		String resourceStructure = resourceId.getStructure();
		if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
			HAPResourceIdSimple simpleId = (HAPResourceIdSimple)resourceId;
			out = new HAPInfoResourceIdNormalize(simpleId, "", simpleId.getResourceTypeId());
		}
		else if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
			HAPResourceIdEmbeded embededId = (HAPResourceIdEmbeded)resourceId;
			out = new HAPInfoResourceIdNormalize(embededId.getParentResourceId(), embededId.getPath(), embededId.getResourceTypeId());
		}
		return out;
	}
	
	public static boolean isEqual(HAPResource resource1, HAPResource resource2) {
		return resource1.equals(resource2);
	}
	
	public static String buildResourceIdLiterate(HAPResourceId resourceId) {
		return HAPUtilityNamingConversion.cascadeLevel2(new String[]{resourceId.getResourceTypeId().getResourceType(), resourceId.getResourceTypeId().getVersion(), buildResourceCoreIdLiterate(resourceId)});
	}
	
	//build literate for id part
	public static String buildResourceCoreIdLiterate(HAPResourceId resourceId) {
		StringBuffer out = new StringBuffer();

		//prefix according to structure
		String structure = resourceId.getStructure();
		if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
			out.append(HAPConstantShared.RESOURCEID_LITERATE_STARTER_SIMPLE);
		} else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
			out.append(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED);
		} else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_DYNAMIC)) {
			out.append(HAPConstantShared.RESOURCEID_LITERATE_STARTER_DYNAMIC);
		} else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
			out.append(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL);
		}
		
		//append core id
		out.append(resourceId.getCoreIdLiterate());
		
//		out.append(HAPConstantShared.SEPERATOR_RESOURCEID_START).append(resourceId.getStructure()).append(HAPConstantShared.SEPERATOR_RESOURCEID_STRUCTURE).append(resourceId.getCoreIdLiterate());
		return out.toString();
	}
	
	public static boolean isResourceIdLiterate(String literate) {
		if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED)) {
			return true;
		} else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL)) {
			return true;
		} else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_DYNAMIC)) {
			return true;
		} else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_SIMPLE)) {
			return true;
		}
		return false;
	}
	

	public static HAPInfoResourceLocation getResourceLocationInfo(HAPResourceIdSimple resourceId) {
		String localFile = isFileBased(resourceId);
		if(localFile!=null) {
			//if file base
			return new HAPInfoResourceLocation(new File(localFile), null, new HAPPathLocationBase(localFile));
		}
		else {
			//check single file first
			String basePath = HAPSystemFolderUtility.getResourceFolder(resourceId.getResourceType());
			return getResourceLocationInfo(basePath, resourceId.getId());
		}
	}

	public static List<HAPResourceDependency> buildResourceDependentFromResourceId(List<HAPResourceIdSimple> ids){
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		for(HAPResourceIdSimple id : ids) {
			out.add(new HAPResourceDependency(id));
		}
		return out;
	}


	
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
