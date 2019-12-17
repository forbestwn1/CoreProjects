package com.nosliw.data.core.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;

//supplement information used in resource id
public class HAPResourceIdSupplement  extends HAPSerializableImp{

	private Map<String, Map<String, HAPResourceId>> m_resources;

	private HAPResourceIdSupplement() {init();}
	
	private HAPResourceIdSupplement(String literate) {
		init();
		this.buildObjectByLiterate(literate);
	}
	
	private HAPResourceIdSupplement(Map<String, Map<String, HAPResourceId>> supplementResources) {
		init();
		this.m_resources.putAll(supplementResources);
	}

	private HAPResourceIdSupplement(List<HAPResourceDependency> supplementResources) {
		init();
		for(HAPResourceDependency sup : supplementResources) {
			for(String name : sup.getAlias()) {
				this.addSupplymentResource(name, sup.getId());
			}
		}
	}

	public static HAPResourceIdSupplement newInstance(String literate) {  return new HAPResourceIdSupplement(literate);	}
	public static HAPResourceIdSupplement newInstance(JSONObject jsonObj) {  
		HAPResourceIdSupplement out = new HAPResourceIdSupplement();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	public static HAPResourceIdSupplement newInstance(Map<String, Map<String, HAPResourceId>> supplementResources) {  return new HAPResourceIdSupplement(supplementResources);   }
	public static HAPResourceIdSupplement newInstance(List<HAPResourceDependency> supplementResources) {   return new HAPResourceIdSupplement(supplementResources);    }
	

	public Map<String, Map<String, HAPResourceId>> getAllSupplymentResourceId(){   return this.m_resources;     }
	
	public HAPResourceId getSupplymentResourceId(String type, String name) {
		Map<String, HAPResourceId> resourceIdByName = this.m_resources.get(type);
		if(resourceIdByName==null)   return null;
		return resourceIdByName.get(name);
	}

	public boolean isEmpty() {	return this.m_resources.isEmpty();	}

	private void init() {
		this.m_resources = new LinkedHashMap<String, Map<String, HAPResourceId>>();
	}
	
	private void addSupplymentResource(String name, HAPResourceId resourceId) {
		Map<String, HAPResourceId> byName = this.m_resources.get(resourceId.getType());
		if(byName==null) {
			byName = new LinkedHashMap<String, HAPResourceId>();
			this.m_resources.put(resourceId.getType(), byName);
		}
		byName.put(name, resourceId);
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String type : this.m_resources.keySet()) {
			Map<String, HAPResourceId> byName = this.m_resources.get(type);
			Map<String, String> byNameMap = new LinkedHashMap<String, String>();
			for(String name : byName.keySet()) {
				byNameMap.put(name, byName.get(name).toStringValue(HAPSerializationFormat.JSON));
			}
			jsonMap.put(type, HAPJsonUtility.buildMapJson(byNameMap));
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		for(Object typeKey : jsonObj.keySet()) {
			String type  = (String)typeKey;
			JSONObject byNameJsonObj = jsonObj.getJSONObject(type);
			for(Object nameKey : byNameJsonObj.keySet()) {
				String name = (String)nameKey;
				JSONObject resourceIdJsonObj = byNameJsonObj.getJSONObject(name);
				HAPResourceId resourceId = HAPResourceHelper.getInstance().buildResourceIdObject(resourceIdJsonObj);
				this.addSupplymentResource(name, resourceId);
			}
		}
		return true; 
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		this.buildObjectByFullJson(json);
		return true; 
	}
	
	@Override
	protected String buildLiterate(){	return this.toStringValue(HAPSerializationFormat.JSON);	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		JSONObject jsonObj = new JSONObject(literateValue);
		this.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(o instanceof HAPResourceIdSupplement){
			HAPResourceIdSupplement resourceIdSupplement = (HAPResourceIdSupplement)o;
			if(this.m_resources.keySet().size()==resourceIdSupplement.m_resources.keySet().size()) {
				out = true;
				for(String type : this.m_resources.keySet()) {
					Map<String, HAPResourceId> byName = resourceIdSupplement.m_resources.get(type);
					if(byName==null) {
						out = false;
						break;
					}
					else {
						if(!HAPBasicUtility.isEqualMaps(byName, this.m_resources.get(type))) {
							out = false;
							break;
						}
					}
				}
			}
		}
		return out;
	}
	
	@Override
	public int hashCode() {	return this.toStringValue(HAPSerializationFormat.LITERATE).hashCode();	}
	
	@Override
	public HAPResourceIdSupplement clone(){	return this;	}
}
