package com.nosliw.data.core.resource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPIdResourceType extends HAPSerializableImp{

	@HAPAttribute
	public static String RESOURCETYPE = "resourceType";

	@HAPAttribute
	public static String VERSION = "version";

	private String m_resourceType;
	
	private String m_version;

	public HAPIdResourceType() {}
	
	protected HAPIdResourceType(String type, String version) {
		this.m_resourceType = type;
		this.m_version = version;
	}

	protected HAPIdResourceType(String key) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(key);
		this.m_resourceType = segs[0];
		if(segs.length>1) {
			this.m_version = segs[1];
		}
	}

	public String getResourceType() {  return this.m_resourceType;  }
	protected void setResourceType(String type) {    this.m_resourceType = type;    }
	
	public String getVersion() {  
		if(HAPUtilityBasic.isStringEmpty(this.m_version)) {
			this.m_version = HAPConstantShared.VERSION_DEFAULT;
		}
		return this.m_version;
	}
	public void setVersion(String version) {    this.m_version = version;      }
	
	public String getKey() {
		return HAPUtilityNamingConversion.cascadeLevel1(this.getResourceType(), this.getVersion());
	}
	
	@Override
	public int hashCode() {		return this.getKey().hashCode();	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(RESOURCETYPE, this.getResourceType());
		jsonMap.put(VERSION, this.getVersion());
	}

	@Override
	protected boolean buildObjectByJson(Object obj){
		JSONObject jsonObj = (JSONObject)obj;
		this.m_resourceType = (String)jsonObj.opt(RESOURCETYPE);
		this.m_version = (String)jsonObj.opt(VERSION);
		return true;  
	}
	
	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(o instanceof HAPIdResourceType){
			HAPIdResourceType resourceTypeId = (HAPIdResourceType)o;
			if(HAPUtilityBasic.isEquals(this.getResourceType(), resourceTypeId.getResourceType())) {
				return HAPUtilityBasic.isEquals(this.getVersion(), resourceTypeId.getVersion());
			}
		}
		return out;
	}
	
	@Override
	public Object cloneValue() {    return new HAPIdResourceType(this.getResourceType(), this.getVersion());     }
	
}
