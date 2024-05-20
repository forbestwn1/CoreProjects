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
	public static final String GROUP = "group";

	@HAPAttribute
	public static final String NAME = "name";

	private String m_group;

	private String m_name;

	public HAPIdValuePort() {
	}
	
	public HAPIdValuePort(String group, String name) {
		this.m_group = group;
		this.m_name = name;
	}
	
	public HAPIdValuePort(String strValue) {
		String[] segs = HAPUtilityNamingConversion.parsePaths(strValue);
		this.m_group = segs[0];
		if(segs.length>1) {
			this.m_name = segs[1];
		}
	}
	
	public String getValuePortGroup() {   return this.m_group;      }
	public void setValuePortGroup(String group) {   this.m_group = group;      }

	
	//name of the port within entity
	public String getValuePortName() {    return this.m_name;     }
	public void setValuePortName(String name) {    this.m_name = name;     }
	
	public String getKey() {    return HAPUtilityNamingConversion.cascadePath(new String[] {this.m_group, this.m_name});     }
	
	@Override
	public HAPIdValuePort cloneValue() {
		return new HAPIdValuePort(this.m_group, this.m_name);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(GROUP, m_group);
		jsonMap.put(NAME, m_name);
	}
}
