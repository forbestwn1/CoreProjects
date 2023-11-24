package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.domain.entity.HAPManagerDomainEntityExecutable;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityExport {

	public static void exportExecutablePackage(HAPExecutablePackage executablePackage, HAPManagerDomainEntityExecutable complexEntityManager, HAPRuntimeInfo runtimeInfo) {
		String mainFolderUnique = getRootFolderUnique();
		exportExecutablePackage(executablePackage, mainFolderUnique, complexEntityManager, runtimeInfo);

		String mainFolderTemp = getRootFolderTemp();
		exportExecutablePackage(executablePackage, mainFolderTemp, complexEntityManager, runtimeInfo);
	}

	private static void exportExecutablePackage(HAPExecutablePackage executablePackage, String mainFolder, HAPManagerDomainEntityExecutable complexEntityManager, HAPRuntimeInfo runtimeInfo) {
		HAPUtilityFile.deleteFolder(mainFolder);
		
		//writer main info
		Map<String, String> mainInfoJson = new LinkedHashMap<String, String>();
		mainInfoJson.put(HAPExecutablePackage.MAINENTITYID, executablePackage.getMainEntityId().toStringValue(HAPSerializationFormat.JSON));
		HAPUtilityFile.writeJsonFile(mainFolder, "mainInfo.json", HAPUtilityJson.buildMapJson(mainInfoJson));
		
		//write package group
		String packageGroupFolder = getExecutablePackageGroupFolder(mainFolder);
		for(HAPResourceIdSimple resourceId : executablePackage.getDependency()) {
			HAPExecutableBundle bundle = complexEntityManager.getComplexEntityResourceBundle(resourceId);
			String packageFolder = getExecutablePackageFolder(packageGroupFolder, resourceId);
			
			//write attachment domain
			HAPDomainAttachment attachmentDomain = bundle.getAttachmentDomain();
			HAPUtilityFile.writeJsonFile(packageFolder, "attachment.json", attachmentDomain.toStringValue(HAPSerializationFormat.JSON));
			
			//write value structure domain
			HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();
			HAPUtilityFile.writeJsonFile(packageFolder, "valuestructure.json", valueStructureDomain.toStringValue(HAPSerializationFormat.JSON));

			//write package definition
			HAPUtilityFile.writeJsonFile(packageFolder, "definition.json", toExpandedJsonStringDefintionDomain(bundle));
			
			//write package executable
			HAPUtilityFile.writeJsonFile(packageFolder, "executable.json", toResourceJsonStringExecutableDomain(bundle, runtimeInfo));
			
			//write package executable
			HAPUtilityFile.writeJsonFile(packageFolder, "executableexpanded.json", toExpandedJsonStringExecutableDomain(bundle));
			
			//external complex entity dependency
			Set<HAPResourceIdSimple> dependency = bundle.getComplexResourceDependency();
			List<String> dependencyArray = new ArrayList<String>();
			for(HAPResourceIdSimple dependencyId : dependency)  dependencyArray.add(dependencyId.toStringValue(HAPSerializationFormat.LITERATE));
			HAPUtilityFile.writeJsonFile(packageFolder, "dependency.json", HAPUtilityJson.buildArrayJson(dependencyArray.toArray(new String[0])));
		}
	}
	
	private static String getRootFolderUnique(){  
		return HAPUtilityFile.getValidFolder(HAPUtilityFile.buildFullFolderPath(HAPSystemFolderUtility.getExecutablePackageExportFolder(), System.currentTimeMillis()+""));  
	}

	private static String getRootFolderTemp(){  
		return HAPUtilityFile.getValidFolder(HAPUtilityFile.buildFullFolderPath(HAPSystemFolderUtility.getExecutablePackageExportFolder(), "temp"));  
	}

	private static String getExecutablePackageGroupFolder(String parentFolder){   return HAPUtilityFile.getValidFolder(HAPUtilityFile.buildFullFolderPath(parentFolder, "resourcebundles"));  }

	private static String getExecutablePackageFolder(String parentFolder, HAPResourceIdSimple resourceId){   
		return HAPUtilityFile.getValidFolder(HAPUtilityFile.buildFullFolderPath(parentFolder, resourceId.toStringValue(HAPSerializationFormat.LITERATE)));  
	}
	
	private static String toExpandedJsonStringDefintionDomain(HAPExecutableBundle resourceBundle) {
		HAPDomainEntityDefinitionGlobal definitionDomainGlobal = resourceBundle.getDefinitionDomain();
		return definitionDomainGlobal.getEntityInfoDefinition(resourceBundle.getDefinitionRootEntityId()).toExpandedJsonString(definitionDomainGlobal);
	}

	private static String toExpandedJsonStringExecutableDomain(HAPExecutableBundle resourceBundle) {
		HAPDomainEntityExecutableResourceComplex executableDomain = resourceBundle.getExecutableDomain();
		return executableDomain.getRootEntity().toExpandedJsonString(executableDomain);		
	}

	private static String toResourceJsonStringExecutableDomain(HAPExecutableBundle resourceBundle, HAPRuntimeInfo runtimeInfo) {
		HAPDomainEntityExecutableResourceComplex executableDomain = resourceBundle.getExecutableDomain();
		return executableDomain.getRootEntity().toResourceData(runtimeInfo).toString();
	}
}
