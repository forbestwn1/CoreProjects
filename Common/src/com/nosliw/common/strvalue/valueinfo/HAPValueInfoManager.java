package com.nosliw.common.strvalue.valueinfo;

import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.strvalue.mode.HAPValueInfoModeUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPValueInfoManager {

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
	
	public void importFromXML(Set<InputStream> xmlInputStreams){
		for(InputStream xmlInputStream : xmlInputStreams){
			HAPValueInfo valueInfo = HAPValueInfoImporterXML.importFromXML(xmlInputStream);
			this.registerValueInfo(valueInfo);
		}
		this.afterImportProcess();
	}
	
	public void importFromXML(Class<?> cs, Set<String> files){
		Set<InputStream> inputStreams = new HashSet<InputStream>();
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
	}
	
	private void registerValueInfo(HAPValueInfo valueInfo){
		this.m_valueInfos.put(valueInfo.getName(), valueInfo);
	}

	private void afterImportProcess(){
		for(String name : this.m_valueInfos.keySet()){
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
					String className = valueInfo.getClassName();
					HAPDBTableInfo tableInfo = new HAPDBTableInfo(table, className);
					this.readColumnInfoFromEntity(tableInfo, valueInfo, null);
					this.m_dbTables.put(valueInfo.getName(), tableInfo);
				}
			}
		}
	}
	
	private void readColumnInfoFromEntity(HAPDBTableInfo tableInfo, HAPValueInfoEntity valueInfoEntity, String path){
		Set<String> properties = valueInfoEntity.getProperties();
		for(String property : properties){
			HAPValueInfo propertyValueInfo = valueInfoEntity.getPropertyInfo(property);
			HAPStringableValueList<HAPDBColumnInfo> columns = propertyValueInfo.getDBColumnInfos();
			for(int i=0; i<columns.size(); i++){
				HAPDBColumnInfo column = columns.get(i).clone(HAPDBColumnInfo.class);
//				column.updateAtomicChild(HAPDBColumnInfo.ATTRPATH, path);
				tableInfo.addColumnInfo(column, property);
			}
			
			String propertyValueInfoType = propertyValueInfo.getValueInfoType();
			if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(propertyValueInfoType)){
				readColumnInfoFromEntity(tableInfo, (HAPValueInfoEntity)propertyValueInfo, HAPNamingConversionUtility.cascadePath(path, property));
			}
		}
	}
	
	public HAPDBTableInfo getDBTableInfo(String name){
		return this.m_dbTables.get(name);
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
