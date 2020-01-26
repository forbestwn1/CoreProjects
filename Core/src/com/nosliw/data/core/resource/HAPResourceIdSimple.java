package com.nosliw.data.core.resource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

/**
 * Resource Id to identify resource 
 * unchangeable object
 */
public class HAPResourceIdSimple extends HAPResourceId{

	@HAPAttribute
	public static String ID = "id";

	protected String m_id;
	
	protected HAPResourceIdSimple(String type){
		super(type);
	}

	public String getId() {   return this.m_id;    }
	protected void setId(String id) {}

	@Override
	public String getStructure() {  return HAPConstant.RESOURCEID_TYPE_SIMPLE;	}

	protected void init(String id, HAPResourceIdSupplement supplement){
		if(id!=null)		this.setId(id);
		if(supplement!=null)   this.setSupplement(supplement);
	}

	@Override
	public String getIdLiterate() {	return this.m_id;	}

	@Override
	protected void buildCoreJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(ID, this.getId());
	}

	@Override
	protected void buildCoreIdByLiterate(String idLiterate) {
		this.m_id = idLiterate;
	}

	@Override
	protected void buildCoreIdByJSON(JSONObject jsonObj) {
		this.m_id = jsonObj.getString(ID);
	}

	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(super.equals(o)) {
			if(o instanceof HAPResourceIdSimple){
				HAPResourceIdSimple resourceId = (HAPResourceIdSimple)o;
				return HAPBasicUtility.isEquals(this.getId(), resourceId.getId());
			}
		}
		return out;
	}
	
	@Override
	public int hashCode() {
		return this.toStringValue(HAPSerializationFormat.LITERATE).hashCode();
	}
	
	@Override
	public HAPResourceIdSimple clone(){
		HAPResourceIdSimple out = new HAPResourceIdSimple(this.getType());
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResourceIdSimple resourceId){
		super.cloneFrom(resourceId);
		this.setId(resourceId.m_id);
	}
}
