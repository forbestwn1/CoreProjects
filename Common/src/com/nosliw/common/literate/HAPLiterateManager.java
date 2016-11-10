package com.nosliw.common.literate;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;

public class HAPLiterateManager {

	private static HAPLiterateManager m_instance;
	
	private Map<String, HAPLiterateType> m_typesByName;
	private Map<Class, HAPLiterateType> m_typesByClass;
	
	private HAPLiterateManager(){
		m_typesByName = new LinkedHashMap<String, HAPLiterateType>();
		m_typesByClass = new LinkedHashMap<Class, HAPLiterateType>();
		
	}
	
	public static HAPLiterateManager getInstance(){
		if(m_instance==null){
			m_instance = new HAPLiterateManager();
		}
		return m_instance;
	}
	
	private void register(HAPLiterateType typeObj){
		m_typesByName.put(typeObj.getName(), typeObj);
		m_typesByClass.put(typeObj.getObjectClass(), typeObj);
	}
	
	public Object stringToValue(String strValue, String type){
		Object out = null;
		HAPLiterateType typeObj = m_typesByName.get(type);
		if(typeObj!=null){
			out = typeObj.stringToValue(strValue);
		}
		return out;
	}
	
	public String valueToString(Object value){
		HAPLiterateType typeObj = m_typesByClass.get(value.getClass());
		return typeObj.valueToString(typeObj);
	}
	
	public String getType(Object value){
		return m_typesByClass.get(value.getClass()).getName();
	}
	
	public boolean isBasicType(String type){
		if(HAPBasicUtility.isStringEmpty(type))  return false;
		else   return m_typesByName.keySet().contains(type);
	}
}
