package com.nosliw.data.core.resource;

import java.util.Map;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPBasicUtility;

/**
 * Resource Id to identify resource 
 */
@HAPEntityWithAttribute
public class HAPResourceId extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String TYPE = "type";
	
	protected String m_type;
	protected String m_id;
	
	public HAPResourceId(){
	}
	
	public HAPResourceId(String literate){
		this.buildObjectByLiterate(literate);
	}
	
	public HAPResourceId(String type, String id){
		this.init(type, id);
	}
	
	protected void init(String type, String id){
		this.m_type = type;
		if(id!=null)		this.setId(id);
	}
	
	public String getId() {		return this.m_id;	}
	
	public String getType() {  return this.m_type;  }

	protected void setId(String id){  this.m_id = id; }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.getId());
		jsonMap.put(TYPE, this.getType());
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.setId(jsonObj.optString(ID));
		this.m_type = jsonObj.optString(TYPE);
		return true; 
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		this.buildObjectByFullJson(json);
		return true; 
	}
	
	@Override
	protected String buildLiterate(){
		return HAPNamingConversionUtility.cascadeLevel2(new String[]{this.getType(), this.getId()});
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPNamingConversionUtility.parseLevel2(literateValue);
		this.m_type = segs[0];
		this.m_id = segs[1];
		return true;  
	}
	

	@Override
	public boolean equals(Object o){
		if(o instanceof HAPResourceId){
			HAPResourceId resourceId = (HAPResourceId)o;
			return HAPBasicUtility.isEquals(this.getType(), resourceId.getType()) &&
					HAPBasicUtility.isEquals(this.getId(), resourceId.getId());
		}
		else{
			return false;
		}
	}
	
	public HAPResourceId clone(){
		HAPResourceId out = new HAPResourceId();
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResourceId resourceId){
		this.setId(resourceId.m_id);
		this.m_type = resourceId.m_type;
	}
}
