package com.nosliw.common.strvalue.valueinfo;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoManager {

	public static Map<String, HAPEntityValueInfo> m_entityValueInfos = new LinkedHashMap<String, HAPEntityValueInfo>();
	
	private Map<String, HAPValueInfo> m_valueInfos;

	public HAPValueInfoManager(Set<InputStream> xmlInputStreams){
		this();
		for(InputStream xmlInputStream : xmlInputStreams){
			HAPValueInfo valueInfo = HAPValueInfoImporterXML.importFromXML(xmlInputStream, this);
			this.registerValueInfo(valueInfo);
		}
		
		this.afterInitProcess();
	}
	
	public HAPValueInfoManager(){	
		this.m_valueInfos = new LinkedHashMap<String, HAPValueInfo>();
	}
	
	public void afterInitProcess(){
		for(String name : this.m_valueInfos.keySet()){
			HAPValueInfo valueInfo = this.m_valueInfos.get(name);
			this.registerValueInfo1(valueInfo);
		}
	}

	private void registerValueInfo1(HAPValueInfo vf){
		HAPValueInfo valueInfo = vf.getSolidValueInfo();
		String categary = valueInfo.getCategary();
		if(HAPConstant.STRINGALBE_VALUEINFO_MAP.equals(categary)){
			HAPValueInfo childValueInfo = ((HAPValueInfoMap)valueInfo).getChildValueInfo();
			this.registerValueInfo1(childValueInfo);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(categary)){
			HAPValueInfo childValueInfo = ((HAPValueInfoList)valueInfo).getChildValueInfo();
			this.registerValueInfo1(childValueInfo);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(categary)){
			registerEntityValueInfo((HAPValueInfoEntity)valueInfo);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(categary)){
			HAPValueInfoEntityOptions entityOptionsValueInfo = (HAPValueInfoEntityOptions)valueInfo;
			Set<String> keys = entityOptionsValueInfo.getOptionsKey();
			for(String key : keys){
				registerValueInfo1(entityOptionsValueInfo.getOptionsValueInfo(key));
			}
		}
	}
	
	private void registerEntityValueInfo(HAPValueInfoEntity valueInfo){
		HAPEntityValueInfo entityValueInfo = new HAPEntityValueInfo(valueInfo, this);
		String className = entityValueInfo.getEntityClassName(); 
		if(className!=null){
			HAPEntityValueInfo cached = HAPValueInfoManager.m_entityValueInfos.get(className);
			if(cached==null){
				HAPValueInfoManager.m_entityValueInfos.put(className, entityValueInfo);
			}
			else{
				if(!cached.getValueInfoEntity().equals(valueInfo))			cached.invalid();
			}
		}
	}
	
	public HAPValueInfo getValueInfo(String name){
		HAPPath pathObj = HAPNamingConversionUtility.parsePath(name);
		HAPValueInfo valueInfo = this.m_valueInfos.get(pathObj.getName());
		String[] pathSegs = pathObj.getPathSegs();
		for(String pathSeg : pathSegs){
			valueInfo = valueInfo.getElement(pathSeg);
		}
		return valueInfo;
	}

	public static HAPValueInfoEntity getEntityValueInfoByClass(Class cs){
		HAPEntityValueInfo info = HAPValueInfoManager.m_entityValueInfos.get(cs.getName());
		if(info.isValid())  		return info.getValueInfoEntity();
		return null;
	}
	
	public void registerValueInfo(HAPValueInfo valueInfo){
		this.m_valueInfos.put(valueInfo.getName(), valueInfo);
	}
}
