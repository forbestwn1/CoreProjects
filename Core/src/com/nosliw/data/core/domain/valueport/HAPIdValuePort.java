package com.nosliw.data.core.domain.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPIdValuePort extends HAPSerializableImp{

	@HAPAttribute
	public static final String ENTITYID = "entityId";

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String NAME = "name";

	private String m_entityId;
	
	private String m_type;
	
	private String m_name;
	
	public HAPIdValuePort() {}
	
	public HAPIdValuePort(String entityId, String type, String name) {
		this.m_entityId = entityId;
		this.m_type = type;
		this.m_name = name;
	}
	
	//which entity this value port belong
	public String getEntityId() {    return this.m_entityId;     }
	
	public String getType() {    return this.m_type;     }
	
	//name of the port within entity
	public String getValuePortName() {    return this.m_name;     }
	
	public String getKey() {    return HAPUtilityNamingConversion.cascadePath(new String[] {this.m_name, this.m_type, this.m_entityId});     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYID, this.m_entityId);
		jsonMap.put(TYPE, m_type);
		jsonMap.put(NAME, m_name);
	}
}
