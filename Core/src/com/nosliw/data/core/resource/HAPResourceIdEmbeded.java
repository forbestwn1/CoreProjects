package com.nosliw.data.core.resource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

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
	
	public HAPResourceIdEmbeded(String type, HAPResourceId parentId, String path) {
		this(type);
		this.m_parentId = parentId;
		this.m_path = path;
	}
	
	@Override
	public String getStructure() {  return HAPConstantShared.RESOURCEID_TYPE_EMBEDED;  }

	public HAPResourceId getParentResourceId() {    return this.m_parentId;    }
	public void setParentResourceId(HAPResourceId parentResourceId) {   this.m_parentId = parentResourceId;   }
	
	public String getPath() {    return this.m_path;    }
	public void setPath(String path) {    this.m_path = path;    }
	
	@Override
	public String getCoreIdLiterate() {
		return HAPUtilityNamingConversion.cascadeLevel3(this.m_path, this.m_parentId.toStringValue(HAPSerializationFormat.LITERATE));
		
//		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
//		this.buildCoreJsonMap(jsonMap, null);
//		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	@Override
	protected void buildCoreIdByLiterate(String idLiterate) {
		String[] idSegs = HAPUtilityNamingConversion.parseLevel3(idLiterate);
		this.m_path = idSegs[0];
		this.m_parentId = HAPFactoryResourceId.newInstance(idSegs[1]);
		
//		JSONObject jsonObj = new JSONObject(idLiterate);
//		this.buildCoreIdByJSON(jsonObj);
	}

	@Override
	protected void buildCoreIdJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(PATH, this.m_path);
		jsonMap.put(PARENT, this.m_parentId.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildCoreIdByJSON(JSONObject jsonObj) {
		this.m_path = jsonObj.optString(PATH);
		this.m_parentId =  HAPFactoryResourceId.newInstance(jsonObj.opt(PARENT));
	}

	@Override
	public HAPResourceId clone() {
		HAPResourceIdEmbeded out = new HAPResourceIdEmbeded(this.getResourceType());
		out.m_path = this.m_path;
		out.m_parentId = this.m_parentId.clone();
		return out;
	}
}
