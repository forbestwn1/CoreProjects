package com.nosliw.core.application.common.structure.reference;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.xxx.application1.HAPExecutableImp;

@HAPEntityWithAttribute
abstract public class HAPPathElementMapping extends HAPExecutableImp{

	public final static String VARIABLE2VARIABLE = "VARIABLE2VARIABLE";

	public final static String CONSTANT2VARIABLE = "CONSTANT2VARIABLE";
	
	public final static String PROVIDE2VARIABLE = "PROVIDE2VARIABLE";
	
	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String PATH = "path";
	
	private String m_type;
	
	private String m_path;
	
	public HAPPathElementMapping(String type, String path) {
		this.m_path = path;
		this.m_type = type;
	}

	public String getType() {   return this.m_type;     } 
	
	public String getPath() {    return this.m_path;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.m_type);
		jsonMap.put(PATH, this.m_path);
	}
}
