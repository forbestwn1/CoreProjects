package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;

/**
 * Resource Id to identify resource 
 * unchangeable object
 */
@HAPEntityWithAttribute
public class HAPResourceId extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String TYPE = "type";
	
	@HAPAttribute
	public static String SUP = "sup";
	
	protected String m_type;
	protected String m_id;
	
	//all the supplement resource in order for this resource to be valid resource
	protected HAPResourceIdSupplement m_supplement;
	
	protected HAPResourceId(){
	}
	
	public static HAPResourceId newInstance(String literate) {    
		HAPResourceId out = new HAPResourceId();
		out.buildObjectByLiterate(literate);
		return out;
	}

	public static HAPResourceId newInstance(JSONObject jsonObj) {    
		HAPResourceId out = new HAPResourceId();
		out.buildObjectByJson(jsonObj);
		return out;
	}
	
	public static List<HAPResourceId> newInstanceList(JSONArray resourceJsonArray){
		List<HAPResourceId> resourceIds = new ArrayList<>();
		for(int i=0; i<resourceJsonArray.length(); i++) {
			resourceIds.add(HAPResourceId.newInstance(resourceJsonArray.getJSONObject(i)));
		}
		return resourceIds;
	}

	public static HAPResourceId newInstance(String type, String id) {    
		HAPResourceId out = new HAPResourceId();
		out.init(type, id, null);
		return out;
	}

	public static HAPResourceId newInstance(String type, String id, List<HAPResourceDependency> supplement){
		HAPResourceId out = new HAPResourceId();
		out.init(type, id, HAPResourceIdSupplement.newInstance(supplement));
		return out;
	}

	public static HAPResourceId newInstance(String type, String id, HAPResourceIdSupplement supplement){
		HAPResourceId out = new HAPResourceId();
		out.init(type, id, supplement);
		return out;
	}

//	public HAPResourceId(String literate){
//		this.buildObjectByLiterate(literate);
//	}
	
//	public HAPResourceId(String type, String id){
//		this.init(type, id, null);
//	}

//	public HAPResourceId(String type, String id, List<HAPResourceDependency> supplement){
//		this.init(type, id, new HAPResourceIdSupplement(supplement));
//	}

//	public HAPResourceId(String type, String id, HAPResourceIdSupplement supplement){
//		this.init(type, id, supplement);
//	}

	protected void init(String type, String id, HAPResourceIdSupplement supplement){
		this.m_type = type;
		if(id!=null)		this.setId(id);
		if(supplement!=null)   this.m_supplement = supplement;
	}
	
	public String getId() {		return this.m_id;	}
	
	public String getType() {  return this.m_type;  }

	protected void setId(String id){  this.m_id = id; }
	
	public HAPResourceIdSupplement getSupplement() {  return this.m_supplement;  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.getId());
		jsonMap.put(TYPE, this.getType());
		if(this.m_supplement!=null && !this.m_supplement.isEmpty())  jsonMap.put(SUP, this.m_supplement.toStringValue(HAPSerializationFormat.JSON));
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
		
		JSONObject supJson = jsonObj.optJSONObject(SUP);
		this.m_supplement = HAPResourceIdSupplement.newInstance(supJson);
		return true; 
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		this.buildObjectByFullJson(json);
		return true; 
	}
	
	@Override
	protected String buildLiterate(){
		if(this.m_supplement==null)		return HAPNamingConversionUtility.cascadeLevel2(new String[]{this.getType(), this.getId()});
		else  return HAPNamingConversionUtility.cascadeLevel2(new String[]{this.getType(), this.getId(), this.m_supplement.toStringValue(HAPSerializationFormat.LITERATE)});
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPNamingConversionUtility.parseLevel2(literateValue);
		if(segs.length==1) {
			//json structure
			JSONObject jsonObj = new JSONObject(literateValue);
			this.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		else {
			this.m_type = segs[0];
			this.m_id = segs[1];
			if(segs.length==3) {
				this.m_supplement = HAPResourceIdSupplement.newInstance(segs[2]);
			}
		}
		return true;  
	}
	
	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(o instanceof HAPResourceId){
			HAPResourceId resourceId = (HAPResourceId)o;
			if(HAPBasicUtility.isEquals(this.getType(), resourceId.getType()) &&
					HAPBasicUtility.isEquals(this.getId(), resourceId.getId())) {
				return HAPBasicUtility.isEquals(this.m_supplement, resourceId.m_supplement);
			}
		}
		return out;
	}
	
	@Override
	public int hashCode() {
		return this.toStringValue(HAPSerializationFormat.LITERATE).hashCode();
	}
	
	@Override
	public HAPResourceId clone(){
		HAPResourceId out = new HAPResourceId();
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResourceId resourceId){
		this.setId(resourceId.m_id);
		this.m_type = resourceId.m_type;
		this.m_supplement = resourceId.m_supplement;
	}
}
