package com.nosliw.data.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;

public class HAPResourceIdTemplate extends HAPResourceId{

	@HAPAttribute
	public static String TEMPLATE = "template";

	@HAPAttribute
	public static String PARMS = "parms";

	private String m_templateId;
	
	private Map<String, HAPData> m_parms;
	
	public HAPResourceIdTemplate(String type) {
		super(type);
	}

	@Override
	public String getStructure() {  return HAPConstant.RESOURCEID_TYPE_TEMPLATE; };

	@Override
	public String getIdLiterate() {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		this.buildCoreJsonMap(jsonMap, null);
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	@Override
	protected void buildCoreJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(TEMPLATE, this.m_templateId);
		jsonMap.put(PARMS, HAPJsonUtility.buildJson(m_parms, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildCoreIdByLiterate(String idLiterate) {
		JSONObject jsonObj = new JSONObject(idLiterate);
		this.buildCoreIdByJSON(jsonObj);
	}

	@Override
	protected void buildCoreIdByJSON(JSONObject jsonObj) {
		this.m_templateId = jsonObj.getString(TEMPLATE);
		this.m_parms = HAPDataUtility.buildDataWrapperMapFromJson(jsonObj);
	}

	@Override
	public HAPResourceId clone() {
		HAPResourceIdTemplate out = new HAPResourceIdTemplate(this.getType());
		out.m_templateId = this.m_templateId;
		out.m_parms = this.m_parms;
		return null;
	}
}
