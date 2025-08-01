package com.nosliw.core.resource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.dynamic.HAPParmDefinition;

public class HAPResourceIdDynamic extends HAPResourceId{

	@HAPAttribute
	public static String BUILDER = "builder";

	@HAPAttribute
	public static String PARMS = "parms";

	private String m_builderId;
	
	private Set<HAPParmDefinition> m_parms;
	
	public HAPResourceIdDynamic(String type) {
		super(type);
		this.m_parms = new HashSet<HAPParmDefinition>();
	}

	@Override
	public String getStructure() {  return HAPConstantShared.RESOURCEID_TYPE_DYNAMIC; }

	public String getBuilderId() {   return this.m_builderId;    }
	
	public Set<HAPParmDefinition> getParms(){    return this.m_parms;    }
	
	@Override
	public String getCoreIdLiterate() {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		this.buildCoreIdJsonMap(jsonMap, null);
		return HAPUtilityJson.buildMapJson(jsonMap);
	}

	@Override
	protected void buildCoreIdJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(BUILDER, this.m_builderId);
		jsonMap.put(PARMS, HAPUtilityJson.buildJson(m_parms, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildCoreIdByLiterate(String idLiterate) {
		JSONObject jsonObj = new JSONObject(idLiterate);
		this.buildCoreIdByJSON(jsonObj);
	}

	@Override
	protected void buildCoreIdByJSON(JSONObject jsonObj) {
		this.m_builderId = jsonObj.getString(BUILDER);
		JSONArray parmJsonArray = jsonObj.optJSONArray(PARMS);
		for(int i=0; i<parmJsonArray.length(); i++) {
			HAPParmDefinition parmDef = new HAPParmDefinition();
			parmDef.buildObject(parmJsonArray.getJSONObject(i), HAPSerializationFormat.JSON);
			this.m_parms.add(parmDef);
		}
	}

	@Override
	public HAPResourceId clone() {
		HAPResourceIdDynamic out = new HAPResourceIdDynamic(this.getResourceType());
		out.m_builderId = this.m_builderId;
		out.m_parms = this.m_parms;
		return null;
	}
}
