package com.nosliw.core.application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.system.HAPSystem;
import com.nosliw.core.system.HAPSystemFolderUtility;

public class HAPUtilityExport {

	private static final String SESSIONID = "sessionId";
	
	public static void exportBundle(HAPResourceIdSimple resourceId, HAPBundle bundle) {
		exportBundle(resourceId, bundle, HAPSystem.id);
	}
	
	public static void exportBundle(HAPResourceIdSimple resourceId, HAPBundle bundle, String sessionId) {
		String mainFolderSession = getResourceFolder(getRootFolderSession(sessionId), resourceId);
		exportBundleToFolder(bundle, mainFolderSession);
		
		String rootFolderTmp = getRootFolderTemp(sessionId);
		String mailFolderTemp = getResourceFolder(rootFolderTmp, resourceId);
		exportBundleToFolder(bundle, mailFolderTemp);
	}
	
	private static String getResourceFolder(String baseFolder, HAPResourceIdSimple resourceId) {
		return baseFolder + "/" + resourceId.toStringValue(HAPSerializationFormat.LITERATE).replaceAll("[^a-zA-Z0-9-_\\.]", "_");
	}

	private static String getRootFolderTemp(String sessionId){
		String tempFolder = HAPUtilityFile.buildFullFolderPath(HAPSystemFolderUtility.getBundleExportFolder(), "temp");
		String infoFilePath = tempFolder+"/info.properties";
		
		String si = null; 
		try {
			Properties prop = new Properties();
			FileInputStream inputStream = new FileInputStream(infoFilePath);
			prop.load(inputStream);
			si = prop.getProperty(SESSIONID);
			inputStream.close();
		}
		catch(Exception e) {
//			e.printStackTrace();
		}

		String out = null;
		if(!sessionId.equals(si)) {
			HAPUtilityFile.deleteFolder(tempFolder);
			out = HAPUtilityFile.getValidFolder(tempFolder);
			
			try {
				Properties prop = new Properties();
				prop.setProperty(SESSIONID, sessionId);
				prop.store(new FileOutputStream(infoFilePath), null);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			out = HAPUtilityFile.getValidFolder(tempFolder);
		}
		return out;
	}
	
	private static String getRootFolderSession(String sessionId){  
		return HAPUtilityFile.getValidFolder(HAPUtilityFile.buildFullFolderPath(HAPSystemFolderUtility.getBundleExportFolder(), sessionId+""));  
	}

	
	private static void exportBundleToFolder(HAPBundle bundle, String bundleFolder) {
		HAPUtilityFile.deleteFolder(bundleFolder);
		
		//write value structure domain
		HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();
		if(valueStructureDomain!=null) {
			HAPUtilityFile.writeJsonFile(bundleFolder, "valuestructure.json", valueStructureDomain.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		} else {
			HAPUtilityFile.writeJsonFile(bundleFolder, "valuestructure.json", "");
		}

		//bundle infor
		Map<String, String> bundleJsonMap = new LinkedHashMap<String, String>();
		bundleJsonMap.put(HAPBundle.DYNAMIC, bundle.getDynamicInfo().toStringValue(HAPSerializationFormat.JSON));
		HAPUtilityFile.writeJsonFile(bundleFolder, "bundle.json", HAPUtilityJson.buildMapJson(bundleJsonMap));
		
		//write package definition
		HAPUtilityFile.writeJsonFile(bundleFolder, "extra.json", HAPManagerSerialize.getInstance().toStringValue(bundle.getExtraData(), HAPSerializationFormat.JSON));
		
		//write main executable
		HAPUtilityFile.writeJsonFile(bundleFolder, "executable.json", HAPManagerSerialize.getInstance().toStringValue(bundle.getMainBrickWrapper(), HAPSerializationFormat.JAVASCRIPT));

		//write branch executable
		HAPUtilityFile.writeJsonFile(bundleFolder, "branches.json", HAPManagerSerialize.getInstance().toStringValue(bundle.getBranchBrickWrappers(), HAPSerializationFormat.JAVASCRIPT));

		//external complex entity dependency
		Set<HAPResourceIdSimple> dependency = bundle.getResourceDependency();
		List<String> dependencyArray = new ArrayList<String>();
		for(HAPResourceIdSimple dependencyId : dependency) {
			dependencyArray.add(dependencyId.toStringValue(HAPSerializationFormat.LITERATE));
		}
		HAPUtilityFile.writeJsonFile(bundleFolder, "dependency.json", HAPUtilityJson.buildArrayJson(dependencyArray.toArray(new String[0])));
	}

/*	
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
			
			HAPBundle bundle = HAPUtilityBrick.getBrickBundle(rootResourceId, entityManager); 
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
			HAPUtilityFile.writeJsonFile(packageFolder, "extra.json", HAPManagerSerialize.getInstance().toStringValue(bundle.getExtraData(), HAPSerializationFormat.JSON));
			
			//write package executable
			HAPUtilityFile.writeJsonFile(packageFolder, "executable.json", HAPManagerSerialize.getInstance().toStringValue(bundle.getBrickWrapper(), HAPSerializationFormat.JAVASCRIPT));
			
			//external complex entity dependency
			Set<HAPResourceIdSimple> dependency = bundle.getResourceDependency();
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
*/	
}
