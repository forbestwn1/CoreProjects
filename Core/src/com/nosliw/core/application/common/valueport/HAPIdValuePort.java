package com.nosliw.core.application.common.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPIdValuePort extends HAPSerializableImp{

	@HAPAttribute
	public static final String ENTITYIDPATH = "entityIdPath";

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String NAME = "name";

	private String m_entityIdPath;
	
	private String m_type;
	
	private String m_name;
	
	public HAPIdValuePort() {}
	
	public HAPIdValuePort(String entityIdPath, String type, String name) {
		this.m_entityIdPath = entityIdPath;
		this.m_type = type;
		this.m_name = name;
	}

	public HAPIdValuePort(HAPRefValuePort valuePortRef) {
		this(valuePortRef.getEntityIdRef().getIdPath(), valuePortRef.getType(), valuePortRef.getValuePortName());
	}
	
	//which entity this value port belong
	public String getEntityIdPath() {    return this.m_entityIdPath;     }
	
	public String getType() {    return this.m_type;     }
	
	//name of the port within entity
	public String getValuePortName() {    return this.m_name;     }
	
	public String getKey() {    return HAPUtilityNamingConversion.cascadePath(new String[] {this.m_name, this.m_type, this.m_entityIdPath});     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYIDPATH, this.m_entityIdPath);
		jsonMap.put(TYPE, m_type);
		jsonMap.put(NAME, m_name);
	}
}
