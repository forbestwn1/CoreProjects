package com.nosliw.data.core.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPExecutablePackage;
import com.nosliw.data.core.entity.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityExport {

	public static void exportEntityPackage(HAPEntityPackage executablePackage, HAPManagerEntity entityManager, HAPRuntimeInfo runtimeInfo) {
		String mainFolderUnique = getRootFolderUnique();
		exportExecutablePackage(executablePackage, mainFolderUnique, entityManager, runtimeInfo);

		String mainFolderTemp = getRootFolderTemp();
		exportExecutablePackage(executablePackage, mainFolderTemp, entityManager, runtimeInfo);
	}

	private static void exportExecutablePackage(HAPEntityPackage executablePackage, String mainFolder, HAPManagerEntity entityManager, HAPRuntimeInfo runtimeInfo) {
		HAPUtilityFile.deleteFolder(mainFolder);
		
		//writer main info
		Map<String, String> mainInfoJson = new LinkedHashMap<String, String>();
		mainInfoJson.put(HAPExecutablePackage.MAINENTITYREF, executablePackage.getMainEntityId().toStringValue(HAPSerializationFormat.JSON));
		HAPUtilityFile.writeJsonFile(mainFolder, "mainInfo.json", HAPUtilityJson.buildMapJson(mainInfoJson));
		
		//write package group
		String packageGroupFolder = getExecutablePackageGroupFolder(mainFolder);
		for(HAPResourceIdSimple resourceId : executablePackage.getDependency()) {
			HAPEntityBundle bundle = entityManager.getEntityBundle(resourceId);
			String packageFolder = getExecutablePackageFolder(packageGroupFolder, resourceId);
			
			//write attachment domain
//			HAPDomainAttachment attachmentDomain = bundle.getAttachmentDomain();
//			HAPUtilityFile.writeJsonFile(packageFolder, "attachment.json", attachmentDomain.toStringValue(HAPSerializationFormat.JSON));
			
			//write value structure domain
			if(bundle instanceof HAPEntityBundleComplex) {
				HAPDomainValueStructure valueStructureDomain = ((HAPEntityBundleComplex)bundle).getValueStructureDomain();
				HAPUtilityFile.writeJsonFile(packageFolder, "valuestructure.json", valueStructureDomain.toStringValue(HAPSerializationFormat.JSON));
			}

			//write package definition
			HAPUtilityFile.writeJsonFile(packageFolder, "extra.json", HAPSerializeManager.getInstance().toStringValue(bundle.getExtraData(), HAPSerializationFormat.JSON));
			
			//write package executable
			HAPUtilityFile.writeJsonFile(packageFolder, "executable.json", HAPSerializeManager.getInstance().toStringValue(bundle.getEntityInfo(), HAPSerializationFormat.JSON));
			
			//external complex entity dependency
			Set<HAPResourceIdSimple> dependency = bundle.getComplexResourceDependency();
			List<String> dependencyArray = new ArrayList<String>();
			for(HAPResourceIdSimple dependencyId : dependency) {
				dependencyArray.add(dependencyId.toStringValue(HAPSerializationFormat.LITERATE));
			}
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
