package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPExecutablePackage;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdEmbeded;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityExport {

	public static void exportEntityPackage(HAPApplicationPackage executablePackage, HAPManagerApplicationBrick entityManager, HAPRuntimeInfo runtimeInfo) {
		String mainFolderUnique = getRootFolderUnique();
		exportExecutablePackage(executablePackage, mainFolderUnique, entityManager, runtimeInfo);

		String mainFolderTemp = getRootFolderTemp();
		exportExecutablePackage(executablePackage, mainFolderTemp, entityManager, runtimeInfo);
	}

	private static void exportExecutablePackage(HAPApplicationPackage executablePackage, String mainFolder, HAPManagerApplicationBrick entityManager, HAPRuntimeInfo runtimeInfo) {
		HAPUtilityFile.deleteFolder(mainFolder);
		
		//writer main info
		Map<String, String> mainInfoJson = new LinkedHashMap<String, String>();
		mainInfoJson.put(HAPExecutablePackage.MAINENTITYREF, executablePackage.getMainResourceId().toStringValue(HAPSerializationFormat.JSON));
		HAPUtilityFile.writeJsonFile(mainFolder, "mainInfo.json", HAPUtilityJson.buildMapJson(mainInfoJson));
		
		//write package group
		String packageGroupFolder = getExecutablePackageGroupFolder(mainFolder);
		
		Set<HAPResourceId> resourceIds = new HashSet<HAPResourceId>();
		resourceIds.add(executablePackage.getMainResourceId());
		resourceIds.addAll(executablePackage.getDependency());
		for(HAPResourceId resourceId : resourceIds) {
			HAPResourceIdSimple rootResourceId = null;
			String structure = resourceId.getStructure();
			if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
				rootResourceId = (HAPResourceIdSimple)resourceId;
			} else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
				rootResourceId = ((HAPResourceIdEmbeded)resourceId).getParentResourceId();
			}
			
			HAPBundle bundle = entityManager.getBrickBundle(rootResourceId);
			String packageFolder = getExecutablePackageFolder(packageGroupFolder, resourceId);
			
			//write attachment domain
//			HAPDomainAttachment attachmentDomain = bundle.getAttachmentDomain();
//			HAPUtilityFile.writeJsonFile(packageFolder, "attachment.json", attachmentDomain.toStringValue(HAPSerializationFormat.JSON));
			
			//write value structure domain
			HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();
			if(valueStructureDomain!=null) {
				HAPUtilityFile.writeJsonFile(packageFolder, "valuestructure.json", valueStructureDomain.toStringValue(HAPSerializationFormat.JAVASCRIPT));
			} else {
				HAPUtilityFile.writeJsonFile(packageFolder, "valuestructure.json", "");
			}

			//write package definition
			HAPUtilityFile.writeJsonFile(packageFolder, "extra.json", HAPSerializeManager.getInstance().toStringValue(bundle.getExtraData(), HAPSerializationFormat.JSON));
			
			//write package executable
			HAPUtilityFile.writeJsonFile(packageFolder, "executable.json", HAPSerializeManager.getInstance().toStringValue(bundle.getBrickWrapper(), HAPSerializationFormat.JAVASCRIPT));
			
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

	private static String getExecutablePackageFolder(String parentFolder, HAPResourceId resourceId){   
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
