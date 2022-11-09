package com.nosliw.data.core.resource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPResourceIdLocal  extends HAPResourceId{

	@HAPAttribute
	public static String BASEPATH = "basePath";

	@HAPAttribute
	public static String NAME = "name";

//	private HAPPathLocationBase m_basePath;
	
	private String m_name;

	public HAPResourceIdLocal(String type) {
		super(type);
	}
	
	@Override
	public String getStructure() {  return HAPConstantShared.RESOURCEID_TYPE_LOCAL;  }

	public String getName() {   return this.m_name;   }
	public void setName(String name) {    this.m_name = name;    }
	
	@Override
	public String getCoreIdLiterate() {
		return this.m_name;
	}

	@Override
	protected void buildCoreIdByLiterate(String idLiterate) {
		this.m_name = idLiterate;
	}

	@Override
	protected void buildCoreIdJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(NAME, this.m_name);
	}

	@Override
	protected void buildCoreIdByJSON(JSONObject jsonObj) {
		this.m_name = jsonObj.optString(NAME);
	}

	@Override
	public HAPResourceId clone() {
		HAPResourceIdLocal out = new HAPResourceIdLocal(this.getResourceType());
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(super.equals(o)) {
			if(o instanceof HAPResourceIdLocal){
				HAPResourceIdLocal resourceId = (HAPResourceIdLocal)o;
				return HAPUtilityBasic.isEquals(this.getName(), resourceId.getName());
			}
		}
		return out;
	}

	protected void cloneFrom(HAPResourceIdLocal resourceId){
		super.cloneFrom(resourceId);
		this.m_name = resourceId.m_name;
	}
}
