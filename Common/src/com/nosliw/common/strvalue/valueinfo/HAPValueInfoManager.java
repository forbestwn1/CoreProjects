package com.nosliw.common.strvalue.valueinfo;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;

public class HAPValueInfoManager {

	private static HAPValueInfoManager m_instance;
	
	private Map<String, HAPValueInfo> m_valueInfos;
	
	private HAPValueInfoManager(){	
		this.m_valueInfos = new LinkedHashMap<String, HAPValueInfo>();
	}
	
	public static HAPValueInfoManager getInstance(){
		if(m_instance==null){
			m_instance = new HAPValueInfoManager();
		}
		return m_instance;
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

	public void registerValueInfo(String name, HAPValueInfo valueInfo){
		this.m_valueInfos.put(name, valueInfo);
	}
	
}
