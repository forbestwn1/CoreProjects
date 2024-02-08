package com.nosliw.data.core.domain.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.domain.HAPRefIdEntity;

@HAPEntityWithAttribute
public class HAPRefValuePort extends HAPSerializableImp{

	@HAPAttribute
	public static final String ENTITYIDINFO = "entityId";

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String NAME = "name";

	private HAPRefIdEntity m_entityIdRef;
	
	private String m_type;
	
	private String m_name;
	
	public HAPRefValuePort() {}
	
	public HAPRefValuePort(HAPIdValuePort valuePortId) {
		this.m_type = valuePortId.getType();
		this.m_name = valuePortId.getValuePortName();
		this.m_entityIdRef = new HAPRefIdEntity(valuePortId.getEntityIdPath());
	}
	
	public HAPRefValuePort(HAPRefIdEntity entityIdRef, String type, String name) {
		this.m_entityIdRef = entityIdRef;
		this.m_type = type;
		this.m_name = name;
	}
	
	//which entity this value port belong
	public HAPRefIdEntity getEntityIdRef() {    return this.m_entityIdRef;     }
	
	public String getType() {    return this.m_type;     }
	
	//name of the port within entity
	public String getValuePortName() {    return this.m_name;     }
	
	public String getKey() {    return HAPUtilityNamingConversion.cascadePath(new String[] {this.m_name, this.m_type, this.m_entityIdRef.getIdPath()});     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYIDINFO, this.m_entityIdRef.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TYPE, m_type);
		jsonMap.put(NAME, m_name);
	}
}
