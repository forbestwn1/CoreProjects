package com.nosliw.core.application.common.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

//value port id within entity
@HAPEntityWithAttribute
public class HAPIdValuePort extends HAPSerializableImp{

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String NAME = "name";

	private String m_type;
	
	private String m_name;

	public HAPIdValuePort(String type, String name) {
		this.m_type = type;
		this.m_name = name;
	}
	
	public HAPIdValuePort(String strValue) {
		String[] segs = HAPUtilityNamingConversion.parsePaths(strValue);
		this.m_type = segs[0];
		if(segs.length>1) {
			this.m_name = segs[1];
		}
	}
	
	public String getType() {    return this.m_type;     }
	
	//name of the port within entity
	public String getValuePortName() {    return this.m_name;     }
	
	public String getKey() {    return HAPUtilityNamingConversion.cascadePath(new String[] {this.m_type, this.m_name});     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, m_type);
		jsonMap.put(NAME, m_name);
	}
}