package com.nosliw.common.literate;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPBasicUtility;

public class HAPLiterateManager {

	private static HAPLiterateManager m_instance;
	
	private Map<String, HAPLiterateDef> m_typesByName;
	private Map<Class, HAPLiterateDef> m_typesByClass;
	
	private HAPLiterateDef m_typeObject; 
	
	private HAPLiterateManager(){
		m_typesByName = new LinkedHashMap<String, HAPLiterateDef>();
		m_typesByClass = new LinkedHashMap<Class, HAPLiterateDef>();
		
		this.registerBasic(new HAPLiterateString());
		this.registerBasic(new HAPLiterateBoolean());
		this.registerBasic(new HAPLiterateFloat());
		this.registerBasic(new HAPLiterateInteger());
		this.registerBasic(new HAPLiterateArray());
				
		this.m_typeObject = new HAPLiterateObject();
		m_typesByName.put(this.m_typeObject.getName(), this.m_typeObject);
	}
	
	public static HAPLiterateManager getInstance(){
		if(m_instance==null){
			m_instance = new HAPLiterateManager();
		}
		return m_instance;
	}
	
	public Object stringToValue(String strValue, HAPLiterateType literateType){
		return this.stringToValue(strValue, literateType.getType(), literateType.getSubType());
	}
	
	public Object stringToValue(String strValue, String type){
		return this.stringToValue(strValue, type, null);
	}
	
	public Object stringToValue(String strValue, String type, String subType){
		Object out = null;
		HAPLiterateDef typeObj = m_typesByName.get(type);
		if(typeObj!=null){
			out = typeObj.stringToValue(strValue, subType);
		}
		return out;
	}
	
	public String valueToString(Object value){
		String out = null;
		HAPLiterateDef typeObj = this.getLiterateDefByObject(value);
		if(typeObj!=null){
			out = typeObj.valueToString(value);
		}
		return out;
	}
	
	public HAPLiterateType getLiterateType(Object value){
		HAPLiterateDef literateDef = this.getLiterateDefByObject(value);
		String subType = literateDef.getSubTypeByObject(value);
		return new HAPLiterateType(literateDef.getName(), subType);
	}
	
	public HAPLiterateType getLiterateTypeByClass(Class cs){
		HAPLiterateDef literateDef = this.getLiterateDefByClass(cs);
		String subType = getSubLiterateTypeByClass(cs);
		return new HAPLiterateType(literateDef.getName(), subType);
	}
	
	public boolean isValidType(String type){
		if(HAPBasicUtility.isStringEmpty(type))  return false;
		else   return m_typesByName.keySet().contains(type);
	}
	
	public String getSubLiterateTypeByClass(Class cs){
		HAPLiterateDef literateDef = m_typesByClass.get(cs);
		if(literateDef!=null){
			return literateDef.getName();
		}
		else{
			return cs.getName();
		}
	}
	
	private HAPLiterateDef getLiterateDefByObject(Object value){
		return this.getLiterateDefByClass(value.getClass());
	}

	private HAPLiterateDef getLiterateDefByClass(Class cs){
		HAPLiterateDef out = m_typesByClass.get(cs);
		if(out==null){
			if(HAPSerializable.class.isAssignableFrom(cs)){
				out = this.m_typeObject;
			}
		}
		return out;
	}
	
	private void registerBasic(HAPLiterateDef typeObj){
		m_typesByName.put(typeObj.getName(), typeObj);
		for(Class cs : typeObj.getObjectClasses()){
			m_typesByClass.put(cs, typeObj);
		}
	}
}
