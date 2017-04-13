package com.nosliw.common.strvalue.valueinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.strvalue.mode.HAPValueInfoModeUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPValueInfoManager {

	public static final String VALUEINFO_NAME = "_VALUEINFO_NAME";
	
	private static HAPValueInfoManager m_instance;
	
	//entity value info by entity class name
	private Map<String, HAPEntityValueInfo> m_entityValueInfos = new LinkedHashMap<String, HAPEntityValueInfo>();
	
	//value info by name
	private Map<String, HAPValueInfo> m_valueInfos;

	//database table infos
	private Map<String, HAPDBTableInfo> m_dbTables;
	
	public static HAPValueInfoManager getInstance(){
		if(m_instance==null){
			m_instance = new HAPValueInfoManager();
			m_instance.loadValueInfos();
		}
		return m_instance;
	}
	
	public void importFromFolder(String folderPath, boolean recur){
		List<File> files = new ArrayList<File>();
		this.importFromFolder(folderPath, files, recur);
		
		List<InputStream> inputStreams = new ArrayList<InputStream>();
		for(File file : files){
    		try {
				InputStream inputStream = new FileInputStream(file);
				inputStreams.add(inputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		this.importFromXML(inputStreams);
	}
	
	private void importFromFolder(String folderPath, List<File> files, boolean recur){
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	File file = listOfFiles[i];
	    	if (file.isFile()) {
	    		if(file.getName().startsWith("valueinfo")){
		    		files.add(file);
	    		}
	    	} else if (file.isDirectory()) {
	    		if(recur){
		    		this.importFromFolder(file.getPath(), files, recur);
	    		}
	    	}
	    }		
	}
	
	public void importFromXML(List<InputStream> xmlInputStreams){
		Set<String> valueInfoNames = new HashSet<String>();
		
		for(InputStream xmlInputStream : xmlInputStreams){
			HAPValueInfo valueInfo = HAPValueInfoImporterXML.importFromXML(xmlInputStream);
			this.registerValueInfo(valueInfo);
			valueInfoNames.add(valueInfo.getName());
		}
		this.afterImportProcess(valueInfoNames);
	}
	
	public void importFromXML(Class<?> cs, List<String> files){
		this.importFromXML(cs, files.toArray(new String[0]));
	}

	public void importFromXML(Class<?> cs, String[] files){
		List<InputStream> inputStreams = new ArrayList<InputStream>();
		for(String file: files){
			InputStream xmlStream = HAPFileUtility.getInputStreamOnClassPath(cs, file);
			inputStreams.add(xmlStream);
		}
		this.importFromXML(inputStreams);
	}
	
	private void loadValueInfos(){
		HAPValueInfoModeUtility.loadValueInfos();
	}
	
	private HAPValueInfoManager(){	
		this.m_valueInfos = new LinkedHashMap<String, HAPValueInfo>();
		this.m_dbTables = new LinkedHashMap<String, HAPDBTableInfo>();
	}
	
	private void registerValueInfo(HAPValueInfo valueInfo){
		this.m_valueInfos.put(valueInfo.getName(), valueInfo);
	}

	private void afterImportProcess(Set<String> valueInfoNames){
		for(String name : valueInfoNames){
			HAPValueInfo valueInfo = this.m_valueInfos.get(name);
			this.registerEntityValueInfoByClass(valueInfo);
		}
		processDBInfo();
	}

	private void registerEntityValueInfoByClass(HAPValueInfo vf){
		HAPValueInfo valueInfo = vf.getSolidValueInfo();
		String valueInfoType = valueInfo.getValueInfoType();
		if(HAPConstant.STRINGALBE_VALUEINFO_MAP.equals(valueInfoType)){
			HAPValueInfo childValueInfo = ((HAPValueInfoMap)valueInfo).getChildValueInfo();
			this.registerEntityValueInfoByClass(childValueInfo);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(valueInfoType)){
			HAPValueInfo childValueInfo = ((HAPValueInfoList)valueInfo).getChildValueInfo();
			this.registerEntityValueInfoByClass(childValueInfo);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(valueInfoType)){
			HAPEntityValueInfo entityValueInfo = new HAPEntityValueInfo((HAPValueInfoEntity)valueInfo, this);
			String className = entityValueInfo.getEntityClassName(); 
			if(className!=null){
				HAPEntityValueInfo cached = this.m_entityValueInfos.get(className);
				if(cached==null){
					this.m_entityValueInfos.put(className, entityValueInfo);
					
					try {
						//if class has valueinfo 
						Field valueInfoNameField = Class.forName(className).getDeclaredField(VALUEINFO_NAME);
						valueInfoNameField.set(null, valueInfo.getName());
					} catch (Exception e) {
//						e.printStackTrace();
					}
				}
				else{
					if(!cached.getValueInfoEntity().equals(valueInfo))			cached.invalid();
				}
			}
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(valueInfoType)){
			HAPValueInfoEntityOptions entityOptionsValueInfo = (HAPValueInfoEntityOptions)valueInfo;
			Set<String> keys = entityOptionsValueInfo.getOptionsKey();
			for(String key : keys){
				registerEntityValueInfoByClass(entityOptionsValueInfo.getOptionsValueInfo(key));
			}
		}
	}

	private void processDBInfo(){
		for(String name : this.m_valueInfos.keySet()){
			HAPValueInfo vf = this.m_valueInfos.get(name);
			String valueInfoType = vf.getValueInfoType();
			if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(valueInfoType)){
				HAPValueInfoEntity valueInfo = (HAPValueInfoEntity)vf.getSolidValueInfo();
				String table = valueInfo.getTable();
				
				if(HAPBasicUtility.isStringNotEmpty(table)){
					HAPDBTableInfo tableInfo = new HAPDBTableInfo(table, new HashSet(valueInfo.getPrimaryKeys()), valueInfo);
					this.readColumnInfoFromEntity(tableInfo, valueInfo, null);
					this.m_dbTables.put(valueInfo.getName(), tableInfo);
				}
			}
		}
	}
	
	private void readColumnInfoFromEntity(HAPDBTableInfo tableInfo, HAPValueInfoEntity valueInfoEntity, String path){
		Set<String> properties = valueInfoEntity.getEntityProperties();
		for(String property : properties){
			HAPValueInfo propertyValueInfo = valueInfoEntity.getPropertyInfo(property).getSolidValueInfo();
			HAPDBColumnsInfo columnsInfo = propertyValueInfo.getDBColumnsInfo();
			if(columnsInfo!=null){
				HAPStringableValueList<HAPDBColumnInfo> columns = columnsInfo.getColumns();
				for(int i=0; i<columns.size(); i++){
					HAPDBColumnInfo column = columns.get(i).clone(HAPDBColumnInfo.class);
//					column.updateAtomicChild(HAPDBColumnInfo.ATTRPATH, path);
					tableInfo.addColumnInfo(column, path, property, columnsInfo.getPathType());
				}
				
				String propertyValueInfoType = propertyValueInfo.getValueInfoType();
				if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(propertyValueInfoType)){
					readColumnInfoFromEntity(tableInfo, (HAPValueInfoEntity)propertyValueInfo, HAPNamingConversionUtility.cascadePath(path, property));
				}
			}
		}
	}
	
	public HAPDBTableInfo getDBTableInfo(String name){
		return this.m_dbTables.get(name);
	}

	public HAPDBTableInfo getDBTableInfoByClass(Class objClass){
		String valuInfoName = this.getEntityValueInfoByClass(objClass).getName();
		HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valuInfoName);
		return dbTableInfo;
	}
	
	public HAPValueInfo getValueInfo(String name){
		HAPValueInfo out = this.m_valueInfos.get(name);
		return out;
	}

	public HAPValueInfoEntity getEntityValueInfoByClass(Class<?> cs){
		return this.getEntityValueInfoByClassName(cs.getName());
	}

	public HAPValueInfoEntity getEntityValueInfoByClassName(String csName){
		HAPEntityValueInfo info = this.m_entityValueInfos.get(csName);
		
		if(info.isValid())  		return info.getValueInfoEntity();
		return null;
	}
}


class HAPEntityValueInfo {

	private String m_className;
	
	private HAPValueInfoEntity m_valueInfoEntity;
	
	private HAPValueInfoManager m_valueInfoMan;
	
	private boolean m_isValid;
	
	public HAPEntityValueInfo(HAPValueInfoEntity valueInfoEntity, HAPValueInfoManager valueInfoMan){
		this.m_className = valueInfoEntity.getClassName();
		this.m_valueInfoEntity = valueInfoEntity;
		this.m_valueInfoMan = valueInfoMan;
		this.m_isValid = true;
	}
	
	public String getEntityClassName(){ return this.m_className; }
	public HAPValueInfoEntity getValueInfoEntity(){ return this.m_valueInfoEntity; }
	public HAPValueInfoManager getValueInfoManager(){  return this.m_valueInfoMan;  }
	public boolean isValid(){   return this.m_isValid; }
	
	public void invalid(){  this.m_isValid = false; }
	
}
