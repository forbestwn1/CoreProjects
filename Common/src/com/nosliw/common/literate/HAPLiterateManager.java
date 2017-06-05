package com.nosliw.common.literate;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateManager {

	private static HAPLiterateManager m_instance;
	
	private Map<String, HAPLiterateDef> m_typesByName;
	private Map<String, HAPLiterateDef> m_typesByClass;
	
	private HAPLiterateDef m_typeObject; 
	
	private HAPLiterateManager(){
		m_typesByName = new LinkedHashMap<String, HAPLiterateDef>();
		m_typesByClass = new LinkedHashMap<String, HAPLiterateDef>();
		
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
	
	public Object clone(Object obj){
		Object out = null;
		if(obj!=null){
			if(obj instanceof HAPSerializable){
				HAPLiterateType literateType = this.getLiterateType(obj);
				String strValue = this.valueToString(obj);
				out = this.stringToValue(strValue, literateType);
			}
			else{
				out = obj;
			}
		}
		return out;
	}
	
	public Class getClassByLiterateType(HAPLiterateType literateType){
		Class out = null;
		if(HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT.equals(literateType.getType())){
			try {
				out = Class.forName(literateType.getSubType());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else{
			HAPLiterateDef literateDef = this.m_typesByName.get(literateType.getType());
			out = literateDef.getObjectClasses().get(0);
		}
		return out;
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
		return this.getLiterateTypeByClass(value.getClass());
	}

	public HAPLiterateType getLiterateTypeByType(Type type){
		String rawTypeName = HAPBasicUtility.getRawTypeName(type);
		HAPLiterateDef literateDef = this.getLiterateDefByClassName(rawTypeName);
		String subType = literateDef.getSubTypeByType(type);
		return new HAPLiterateType(literateDef.getName(), subType);
	}
	
	public HAPLiterateType getLiterateTypeByClass(Class cs){
		HAPLiterateDef literateDef = this.getLiterateDefByClassName(cs.getName());
		String subType = literateDef.getSubTypeByType(cs);
		return new HAPLiterateType(literateDef.getName(), subType);
	}
	
	public boolean isValidType(String type){
		if(HAPBasicUtility.isStringEmpty(type))  return false;
		else   return m_typesByName.keySet().contains(type);
	}
	
	private HAPLiterateDef getLiterateDefByObject(Object value){
		
		if(value==null){
			int kkkk = 5555;
			kkkk++;
		}
		
		
		return this.getLiterateDefByClassName(value.getClass().getName());	
	}

	private HAPLiterateDef getLiterateDefByClassName(String csName){
		HAPLiterateDef out = m_typesByClass.get(csName);
		if(out==null)				out = this.m_typeObject;
		return out;
	}
	
	private void registerBasic(HAPLiterateDef typeObj){
		m_typesByName.put(typeObj.getName(), typeObj);
		for(Class cs : typeObj.getObjectClasses()){
			m_typesByClass.put(cs.getName(), typeObj);
		}
	}
}
