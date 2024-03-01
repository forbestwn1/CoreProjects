package com.nosliw.data.core.resource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

/**
 * Resource Id to identify resource 
 * unchangeable object
 */
public class HAPResourceIdSimple extends HAPResourceId{

	@HAPAttribute
	public static String ID = "id";

	protected String m_id;

	public HAPResourceIdSimple(){
		super(null, null);
	}

	protected HAPResourceIdSimple(String type){
		super(type, null);
	}

	public HAPResourceIdSimple(String type, String version, String id){
		super(type, version);
		this.m_id = id;
	}

	public HAPResourceIdSimple(HAPResourceIdSimple resourceId){
		this(resourceId.getResourceType());
		this.cloneFrom(resourceId);
	}

	public String getId() {   return this.m_id;    }
	protected void setId(String id) {  this.m_id = id;   }

	@Override
	public String getStructure() {  return HAPConstantShared.RESOURCEID_TYPE_SIMPLE;	}

	protected void init(String id, HAPSupplementResourceId supplement){
		if(id!=null) {
			this.setId(id);
		}
		if(supplement!=null) {
			this.setSupplement(supplement);
		}
	}

	@Override
	public String getCoreIdLiterate() {	return this.m_id;	}

	@Override
	protected void buildCoreIdJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
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
	public int hashCode() {
		return this.toStringValue(HAPSerializationFormat.LITERATE).hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(super.equals(o)) {
			if(o instanceof HAPResourceIdSimple){
				HAPResourceIdSimple resourceId = (HAPResourceIdSimple)o;
				return HAPUtilityBasic.isEquals(this.getId(), resourceId.getId());
			}
		}
		return out;
	}
	
	@Override
	public HAPResourceIdSimple clone(){
		HAPResourceIdSimple out = new HAPResourceIdSimple(this.getResourceType());
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResourceIdSimple resourceId){
		super.cloneFrom(resourceId);
		this.setId(resourceId.m_id);
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		String[] segs = HAPUtilityNamingConversion.parseLevel2(literateValue);
		if(segs.length==2) {
			this.setResourceType(segs[0]);
			buildCoreIdByLiterate(segs[1]);
		}
		else if(segs.length==1) {
			if(this.getResourceType()!=null) {
				buildCoreIdByLiterate(literateValue);
			} else {
				this.setResourceType(literateValue);
			}
		}
		return true;  
	}

}
