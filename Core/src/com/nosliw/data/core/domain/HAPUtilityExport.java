package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.complex.HAPManagerComplexEntity;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityExport {

	public static void exportExecutablePackage(HAPExecutablePackage executablePackage, HAPManagerComplexEntity complexEntityManager) {
		String mainFolderUnique = getRootFolderUnique();
		exportExecutablePackage(executablePackage, mainFolderUnique, complexEntityManager);

		String mainFolderTemp = getRootFolderTemp();
		exportExecutablePackage(executablePackage, mainFolderTemp, complexEntityManager);
	}

	private static void exportExecutablePackage(HAPExecutablePackage executablePackage, String mainFolder, HAPManagerComplexEntity complexEntityManager) {
		HAPUtilityFile.deleteFolder(mainFolder);
		
		//writer main info
		Map<String, String> mainInfoJson = new LinkedHashMap<String, String>();
		mainInfoJson.put(HAPExecutablePackage.MAINENTITYID, executablePackage.getMainEntityId().toStringValue(HAPSerializationFormat.JSON));
		HAPUtilityFile.writeJsonFile(mainFolder, "mainInfo.json", HAPJsonUtility.buildMapJson(mainInfoJson));
		
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
			HAPUtilityFile.writeJsonFile(packageFolder, "executable.json", toExpandedJsonStringExecutableDomain(bundle));
			
			//external complex entity dependency
			Set<HAPResourceIdSimple> dependency = bundle.getComplexResourceDependency();
			List<String> dependencyArray = new ArrayList<String>();
			for(HAPResourceIdSimple dependencyId : dependency)  dependencyArray.add(dependencyId.toStringValue(HAPSerializationFormat.LITERATE));
			HAPUtilityFile.writeJsonFile(packageFolder, "dependency.json", HAPJsonUtility.buildArrayJson(dependencyArray.toArray(new String[0])));
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
		return executableDomain.getEntityInfoExecutable(resourceBundle.getExecutableRootEntityId()).toExpandedJsonString(executableDomain);
	}
}
