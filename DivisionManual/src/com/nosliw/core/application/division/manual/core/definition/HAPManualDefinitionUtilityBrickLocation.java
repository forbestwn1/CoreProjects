package com.nosliw.core.application.division.manual.core.definition;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.google.common.io.Files;
import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.location.HAPPathLocationBase;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.system.HAPSystemFolderUtility;

public class HAPManualDefinitionUtilityBrickLocation {

	private static Map<String, HAPSerializationFormat> m_extensionToFormat = new LinkedHashMap<String, HAPSerializationFormat>(); 
	
	static {
		m_extensionToFormat.put("json", HAPSerializationFormat.JSON);
		m_extensionToFormat.put("html", HAPSerializationFormat.HTML);
		m_extensionToFormat.put("js", HAPSerializationFormat.JAVASCRIPT);
	}

	public static HAPManualDefinitionInfoBrickLocation getBrickLocationInfo(HAPIdBrick brickId) {
		return getBrickLocationInfo(HAPSystemFolderUtility.getManualBrickBaseFolder(), brickId);
	}

	public static Map<String, HAPManualDefinitionInfoBrickLocation> getBranchBrickLocationInfos(String basePath) {
		Map<String, HAPManualDefinitionInfoBrickLocation> out = new LinkedHashMap<String, HAPManualDefinitionInfoBrickLocation>();
		
		File[] branchFiles = new File(basePath + "branch/").listFiles();
		if(branchFiles!=null) {
			for(File branchFolder : branchFiles) {
				if(branchFolder.isDirectory()) {
					String branchName = branchFolder.getName();
					JSONObject brickInfoObj = new JSONObject(HAPUtilityFile.readFile(new File(branchFolder.getAbsolutePath()+"/brickinfo.json")));
					if(HAPUtilityEntityInfo.isEnabled(brickInfoObj)) {
						Pair<File, HAPSerializationFormat> result = findBrickFile(branchFolder, "main");
						HAPIdBrickType brickTypeId = HAPUtilityBrickId.parseBrickTypeId(brickInfoObj.get("brickTypeId"));
						out.put(branchName, new HAPManualDefinitionInfoBrickLocation(brickTypeId, result.getLeft(), result.getRight(), new HAPPathLocationBase(basePath), true));
					}				
				}
			}
		}
		return out;
	}

	public static HAPManualDefinitionInfoBrickLocation getLocalBrickLocationInfo(String basePath, HAPIdBrick brickId) {
		return getBrickLocationInfo(basePath+"local/", brickId);
	}

	private static HAPManualDefinitionInfoBrickLocation getBrickLocationInfo(String basePath, HAPIdBrick brickId) {
		HAPIdBrickType brickTypeId = brickId.getBrickTypeId(); 
		basePath = basePath + brickTypeId.getBrickType() + "/";
		if(brickTypeId.getVersion()!=null) {
			basePath = basePath + brickTypeId.getVersion()+ "/";
		}
		
		String newBasePath = basePath+brickId.getId()+ "/";
		File folder = new File(newBasePath);
		Pair<File, HAPSerializationFormat> result;
		boolean isSingleFile;
		if(folder.isDirectory()&&folder.exists()) {
			//from folder
			result = findBrickFile(folder, "main");
			isSingleFile = false;
		}
		else {
			//from file
			newBasePath = basePath;
			result = findBrickFile(new File(newBasePath), brickId.getId());
			isSingleFile = true;
		}
		
		if(result!=null) {
			return new HAPManualDefinitionInfoBrickLocation(brickTypeId, result.getLeft(), result.getRight(), new HAPPathLocationBase(newBasePath), isSingleFile);
		}
		else {
			HAPErrorUtility.invalid("Cannot find module resource " + brickId);
		}
		return null;
	}

	
	private static Pair<File, HAPSerializationFormat> findBrickFile(File dir, String fileName){
		File[] matches = dir.listFiles(new FilenameFilter(){
		  @Override
			public boolean accept(File dir, String name){
			  return fileName.equals(Files.getNameWithoutExtension(name));
		  	}
		});
		
		if(matches!=null&&matches.length>0) {
			File file = matches[0];
			HAPSerializationFormat format = m_extensionToFormat.get(Files.getFileExtension(file.getName()));
			if(format==null) {
				format = HAPSerializationFormat.JSON;
			}
			return Pair.of(file, format); 
		}
		return null;
	}

}
