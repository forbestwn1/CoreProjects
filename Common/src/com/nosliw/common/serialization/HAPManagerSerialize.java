package com.nosliw.common.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.literate.HAPLiterateManager;

/**
 * Manager class to do serialzation job 
 * @param <K>
 */
public class HAPManagerSerialize {

	private Map<String, Class> m_classMaps;
	
	private static HAPManagerSerialize m_instance;
	
	public static HAPManagerSerialize getInstance(){
		if(m_instance==null){
			m_instance = new HAPManagerSerialize();
		}
		return m_instance;
	}
	
	private HAPManagerSerialize(){
		this.m_classMaps = new LinkedHashMap<String, Class>();
	}
	
	/**
	 * Register the real class name to do deserializing according to original name
	 * This is used when interface name is exposed only when do deserialzation
	 *  
	 */
	public void registerClassName(String original, String className){
		try {
			this.m_classMaps.put(original, Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String toStringValue(Object obj, HAPSerializationFormat format){
		if(obj == null) {
			return null;
		}

		if(obj instanceof HAPSerializable){
			return ((HAPSerializable)obj).toStringValue(format);
		}

		switch(format){
		case LITERATE:
			return HAPLiterateManager.getInstance().valueToString(obj);
		case JSON:
		case JSON_FULL:
		case JAVASCRIPT:
			return HAPUtilityJson.buildJson(obj, format);
		default:
			return obj.toString();
		}
	}
	
	public HAPSerializable buildObject(String className, Object value, HAPSerializationFormat format){
		if(value==null) {
			return null;
		}
		HAPSerializable out = null;
		try {
			Class cs = this.m_classMaps.get(className);
			if(cs==null) {
				cs = Class.forName(className);
			}
			if(value.getClass()==cs) {
				out = (HAPSerializable)value;
			} else {
				out = ((HAPSerializable)cs.newInstance());
				if(!out.buildObject(value, format)){
					//build failed
					out = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
}
