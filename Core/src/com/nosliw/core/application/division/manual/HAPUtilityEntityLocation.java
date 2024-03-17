package com.nosliw.core.application.division.manual;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.io.Files;
import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityEntityLocation {

	private static Map<String, HAPSerializationFormat> m_extensionToFormat = new LinkedHashMap<String, HAPSerializationFormat>(); 
	
	static {
		m_extensionToFormat.put("json", HAPSerializationFormat.JSON);
		m_extensionToFormat.put("html", HAPSerializationFormat.HTML);
		m_extensionToFormat.put("js", HAPSerializationFormat.JAVASCRIPT);
	}

	public static HAPInfoEntityLocation getEntityLocationInfo(HAPIdBrick entityId) {
		return getEntityLocationInfo(HAPSystemFolderUtility.getManualEntityBaseFolder(), entityId);
	}

	public static HAPInfoEntityLocation getLocalEntityLocationInfo(String basePath, HAPIdBrick entityId) {
		return getEntityLocationInfo(basePath, entityId);
	}

	private static HAPInfoEntityLocation getEntityLocationInfo(String basePath, HAPIdBrick entityId) {
		HAPIdBrickType entityTypeId = entityId.getBrickTypeId(); 
		basePath = basePath + entityTypeId.getBrickType() + "/";
		if(entityTypeId.getVersion()!=null) {
			basePath = basePath + entityTypeId.getVersion()+ "/";
		}
		
		String newBasePath = basePath+"/"+entityId.getId();
		File folder = new File(newBasePath);
		Pair<File, HAPSerializationFormat> result;
		if(folder.isDirectory()&&folder.exists()) {
			//from folder
			result = findEntityFile(folder, "main");
		}
		else {
			//from file
			newBasePath = basePath;
			result = findEntityFile(new File(newBasePath), entityId.getId());
		}
		
		if(result!=null) {
			return new HAPInfoEntityLocation(result.getLeft(), result.getRight(), new HAPPathLocationBase(basePath));
		}
		else {
			HAPErrorUtility.invalid("Cannot find module resource " + entityId);
		}
		return null;
	}

	
	private static HAPInfoEntityLocation getEntityLocationInfo(String basePath, String id) {
		String newBasePath = basePath+"/"+id;
		File folder = new File(newBasePath);
		Pair<File, HAPSerializationFormat> result;
		if(folder.isDirectory()&&folder.exists()) {
			//from folder
			result = findEntityFile(folder, "main");
		}
		else {
			//from file
			newBasePath = basePath;
			result = findEntityFile(new File(newBasePath), id);
		}
		
		if(result!=null) {
			return new HAPInfoEntityLocation(result.getLeft(), result.getRight(), new HAPPathLocationBase(newBasePath));
		}
		else {
			HAPErrorUtility.invalid("Cannot find module resource " + id);
		}
		return null;
	}

	private static Pair<File, HAPSerializationFormat> findEntityFile(File dir, String fileName){
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
