package com.nosliw.data.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPResourceIdEmbeded  extends HAPResourceId{

	@HAPAttribute
	public static String PARENT = "parent";

	@HAPAttribute
	public static String PATH = "path";

	private HAPResourceId m_parentId;
	
	private String m_path;

	public HAPResourceIdEmbeded(String type) {
		super(type);
	}
	
	@Override
	public String getStructure() {  return HAPConstant.RESOURCEID_TYPE_EMBEDED;  }

	@Override
	public String getIdLiterate() {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		this.buildCoreJsonMap(jsonMap, null);
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	@Override
	protected void buildCoreIdByLiterate(String idLiterate) {
		JSONObject jsonObj = new JSONObject(idLiterate);
		this.buildCoreIdByJSON(jsonObj);
	}

	@Override
	protected void buildCoreJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(PATH, this.m_path);
		jsonMap.put(PARENT, this.m_parentId.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildCoreIdByJSON(JSONObject jsonObj) {
		this.m_path = jsonObj.optString(PATH);
		this.m_parentId =  HAPResourceIdFactory.newInstance(jsonObj.opt(PARENT));
	}

	@Override
	public HAPResourceId clone() {
		HAPResourceIdEmbeded out = new HAPResourceIdEmbeded(this.getType());
		out.m_path = this.m_path;
		out.m_parentId = this.m_parentId.clone();
		return null;
	}
}
