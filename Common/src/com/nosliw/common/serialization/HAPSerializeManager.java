package com.nosliw.common.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manager class to do serialzation job 
 * @param <K>
 */
public class HAPSerializeManager {

	private Map<String, Class> m_classMaps;
	
	private static HAPSerializeManager m_instance;
	
	public static HAPSerializeManager getInstance(){
		if(m_instance==null){
			m_instance = new HAPSerializeManager();
		}
		return m_instance;
	}
	
	private HAPSerializeManager(){
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
		if(obj == null)   return null;
		if(obj instanceof HAPSerializable){
			return ((HAPSerializable)obj).toStringValue(format);
		}
		else{
			return obj.toString();
		}
	}
	
	public HAPSerializable buildObject(String className, Object value, HAPSerializationFormat format){
		HAPSerializable out = null;
		try {
			Class cs = this.m_classMaps.get(className);
			if(cs==null){
				cs = Class.forName(className);
				out = ((HAPSerializable)cs.newInstance());
				out.buildObject(out, format);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
}
