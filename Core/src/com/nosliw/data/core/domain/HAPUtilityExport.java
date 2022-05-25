package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityExport {

	public static void exportExecutablePackage(HAPPackageExecutable executablePackage) {
		String mainFolder = getRootFolder();
		
		//writer main info
		Map<String, String> mainInfoJson = new LinkedHashMap<String, String>();
//		mainInfoJson.put(HAPPackageExecutable.MAINRESOURCEID, executablePackage.getMainResourceId().toStringValue(HAPSerializationFormat.JSON));
//		mainInfoJson.put(HAPPackageExecutable.MAINENTITYID, executablePackage.getMainEntityId().toStringValue(HAPSerializationFormat.JSON));
		HAPUtilityFile.writeJsonFile(mainFolder, "mainInfo.json", HAPJsonUtility.buildMapJson(mainInfoJson));
		
		//write package group
		String packageGroupFolder = getExecutablePackageGroupFolder(mainFolder);
		for(HAPResourceIdSimple resourceId : executablePackage.getComplexResourcePackageGroup().getComplexResourceIds()) {
			HAPPackageComplexResource resourcePackage = executablePackage.getComplexResourcePackageGroup().getComplexResourcePackage(resourceId);
			String packageFolder = getExecutablePackageFolder(packageGroupFolder, resourceId);
			
			//write attachment domain
			HAPDomainAttachment attachmentDomain = resourcePackage.getAttachmentDomain();
			HAPUtilityFile.writeJsonFile(packageFolder, "attachment.json", attachmentDomain.toStringValue(HAPSerializationFormat.JSON));
			
			//write value structure domain
			HAPDomainValueStructure valueStructureDomain = resourcePackage.getValueStructureDomain();
			HAPUtilityFile.writeJsonFile(packageFolder, "valuestructure.json", valueStructureDomain.toStringValue(HAPSerializationFormat.JSON));

			//write package definition
			HAPUtilityFile.writeJsonFile(packageFolder, "entity.json", toExpandedJsonStringDefintionDomain(resourcePackage));
			
			//write package executable
			HAPUtilityFile.writeJsonFile(packageFolder, "executable.json", toExpandedJsonStringExecutableDomain(resourcePackage));
			
		}
	}
	
	private static String getRootFolder(){  return HAPUtilityFile.getValidFolder(HAPUtilityFile.buildFullFolderPath(HAPSystemFolderUtility.getExecutablePackageExportFolder(), System.currentTimeMillis()+""));  }

	private static String getExecutablePackageGroupFolder(String parentFolder){   return HAPUtilityFile.getValidFolder(HAPUtilityFile.buildFullFolderPath(parentFolder, "resourcepackages"));  }

	private static String getExecutablePackageFolder(String parentFolder, HAPResourceIdSimple resourceId){   
		return HAPUtilityFile.getValidFolder(HAPUtilityFile.buildFullFolderPath(parentFolder, resourceId.toStringValue(HAPSerializationFormat.LITERATE)));  
	}
	
	private static String toExpandedJsonStringDefintionDomain(HAPPackageComplexResource resourcePackage) {
		HAPDomainEntityDefinitionGlobal definitionDomainGlobal = resourcePackage.getDefinitionDomain();
		return definitionDomainGlobal.getEntityInfoDefinition(resourcePackage.getDefinitionRootEntityId()).toExpandedJsonString(definitionDomainGlobal);
	}

	private static String toExpandedJsonStringExecutableDomain(HAPPackageComplexResource resourcePackage) {
		HAPDomainEntityExecutableResourceComplex executableDomain = resourcePackage.getExecutableDomain();
		return executableDomain.getEntityInfoExecutable(resourcePackage.getExecutableRootEntityId()).toExpandedJsonString(executableDomain);
	}
}
