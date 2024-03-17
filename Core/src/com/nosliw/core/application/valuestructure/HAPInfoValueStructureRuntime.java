package com.nosliw.core.application.valuestructure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;

@HAPEntityWithAttribute
public class HAPInfoValueStructureRuntime extends HAPSerializableImp{

	@HAPAttribute
	public static final String ID = "id";
	
	@HAPAttribute
	public static final String NAME = "name";
	
	@HAPAttribute
	public static final String INFO = "info";

	private String m_id;
	
	private String m_name;
	
	private HAPInfo m_info;

	public HAPInfoValueStructureRuntime(String id, HAPInfo info, String name) {
		this.m_id = id;
		this.m_name = name;
		this.m_info = info;
	}
	
	public String getName() {   return this.m_name;    }
	
	public String getId() {   return this.m_id;    }
	
	public String getInfoValue(String name) {   return (String)this.m_info.getValue(name);     }
	
	public void setInfoValue(String name, String value) {    this.m_info.setValue(name, value);     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(INFO, HAPUtilityJson.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}
}
