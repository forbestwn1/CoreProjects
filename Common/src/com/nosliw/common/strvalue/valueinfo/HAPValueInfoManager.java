package com.nosliw.common.strvalue.valueinfo;

import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Element;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;

public class HAPValueInfoManager {

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

	public void registerValueInfo(HAPValueInfo valueInfo){
		this.m_valueInfos.put(valueInfo.getName(), valueInfo);
	}

	public HAPStringableValueEntity readEntityFromXML(Element rootEle){
		HAPStringableValueEntity out = HAPStringableEntityImporterXML.readRootEntity(rootEle, this);
		return out;
	}
	
}
