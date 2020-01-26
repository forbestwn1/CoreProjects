package com.nosliw.data.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;

@HAPEntityWithAttribute
public abstract class HAPResourceId extends HAPSerializableImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String STRUCUTRE = "strucutre";

	@HAPAttribute
	public static String SUP = "supliment";


	private String m_type;

	//all the supplement resource in order for this resource to be valid resource
	private HAPResourceIdSupplement m_supplement;

	public HAPResourceId(String type) {
		this.m_type = type;
	}
	
	public String getType() {  return this.m_type;  }
	protected void setType(String type) {    this.m_type = type;    }
	
	public abstract String getStructure();
	
	public HAPResourceIdSupplement getSupplement() {  return this.m_supplement;  }
	public void setSupplement(HAPResourceIdSupplement sup) {   this.m_supplement = sup;   }
	
	//literate for id part only
	public abstract String getIdLiterate();
	protected abstract void buildCoreJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap);
	
	//parse id part literate
	protected abstract void buildCoreIdByLiterate(String idLiterate);	

	protected abstract void buildCoreIdByJSON(JSONObject jsonObj);	

	@Override
	protected String buildLiterate(){
		return HAPNamingConversionUtility.cascadeLevel2(new String[]{this.getType(), HAPResourceUtility.buildResourceCoreIdLiterate(this)});
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		
		Map<String, String> jsonMapId = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMapId = new LinkedHashMap<String, Class<?>>();
		jsonMapId.put(STRUCUTRE, this.getStructure());
		if(this.m_supplement!=null && !this.m_supplement.isEmpty())   jsonMapId.put(SUP, this.m_supplement.toStringValue(HAPSerializationFormat.JSON));
		this.buildCoreJsonMap(jsonMapId, typeJsonMapId);
		jsonMap.put(ID, HAPJsonUtility.buildMapJson(jsonMapId, typeJsonMapId));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(o instanceof HAPResourceId){
			HAPResourceId resourceId = (HAPResourceId)o;
			if(HAPBasicUtility.isEquals(this.getType(), resourceId.getType())) {
				return HAPBasicUtility.isEquals(this.m_supplement, resourceId.m_supplement);
			}
		}
		return out;
	}
	
	@Override
	public abstract HAPResourceId clone();

	protected void cloneFrom(HAPResourceId resourceId){
		this.m_type = resourceId.m_type;
		this.m_supplement = resourceId.m_supplement;
	}
	
	
	
	
	
	
	
	
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		Object type = jsonObj.opt(TYPE);
		if(type!=null)		this.m_type = (String)type; 
		
		String idLiterate = jsonObj.getString(ID);
		this.buildCoreIdByLiterate(idLiterate);
		return true; 
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		this.buildObjectByFullJson(json);
		return true; 
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
			buildCoreIdByLiterate(segs[1]);
		}
		return true;  
	}

}
