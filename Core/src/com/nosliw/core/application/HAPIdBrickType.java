package com.nosliw.core.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPIdBrickType extends HAPSerializableImp{

	@HAPAttribute
	public static final String BRICKTYPE = "brickType";
	
	@HAPAttribute
	public static final String VERSION = "version";
	
	//type of entity
	private String m_brickType;
	
	//version, what entity executable looks like
	private String m_version;

	public HAPIdBrickType() {}
	
	public HAPIdBrickType(String entityType, String version) {
		this.m_brickType = entityType;
		this.m_version = version;
	}
	
	public HAPIdBrickType(String key) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(key);
		this.m_brickType = segs[0];
		if(segs.length>1) {
			this.m_version = segs[1];
		}
	}
	
	public String getBrickType() {    return this.m_brickType;    }
	
	public String getVersion() {   return this.m_version;     }
	public void setVersion(String version) {   this.m_version = version;     }

	public String getKey() {
		return HAPUtilityNamingConversion.cascadeLevel1(this.m_brickType, this.m_version);
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPIdBrickType) {
			HAPIdBrickType brickTypeId = (HAPIdBrickType)obj;
			if(this.getKey().equals(brickTypeId.getKey())) {
				out = true;
			}
		}
		return out;
	}
	
	@Override
	public int hashCode() {		return this.getKey().hashCode();	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(BRICKTYPE, this.m_brickType);
		jsonMap.put(VERSION, this.m_version);
	}

	@Override
	protected boolean buildObjectByJson(Object obj){
		JSONObject jsonObj = (JSONObject)obj;
		this.m_brickType = (String)jsonObj.opt(BRICKTYPE);
		this.m_version = jsonObj.optString(VERSION);
		return true;  
	}

}
