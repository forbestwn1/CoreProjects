package com.nosliw.common.strvalue.valueinfo;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoManager {

	public static Map<String, HAPEntityValueInfo> m_entityValueInfos = new LinkedHashMap<String, HAPEntityValueInfo>();
	
	private Map<String, HAPValueInfo> m_valueInfos;
	
	public HAPValueInfoManager(){	
		this.m_valueInfos = new LinkedHashMap<String, HAPValueInfo>();
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

	public static HAPValueInfoEntity getEntityValueInfo(Class cs){
		HAPEntityValueInfo info = HAPValueInfoManager.m_entityValueInfos.get(cs.getName());
		return info.getValueInfoEntity();
	}
	
	public void registerValueInfo(HAPValueInfo valueInfo){
		this.m_valueInfos.put(valueInfo.getName(), valueInfo);
		
		if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY.equals(valueInfo.getCategary())){
			HAPValueInfoEntity valueInfoEntity = (HAPValueInfoEntity)valueInfo;
			
			HAPEntityValueInfo entityValueInfo = new HAPEntityValueInfo(valueInfoEntity, this);
			String className = entityValueInfo.getEntityClassName(); 
			if(className!=null){
				if(HAPValueInfoManager.m_entityValueInfos.get(className)==null){
					HAPValueInfoManager.m_entityValueInfos.put(className, entityValueInfo);
				}
				else{
					throw new NullPointerException();
				}
			}
		}
	}
}
