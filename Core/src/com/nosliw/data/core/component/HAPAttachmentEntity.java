package com.nosliw.data.core.component;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPAttachmentEntity extends HAPAttachmentImp{

	@HAPAttribute
	public static String ENTITY = "entity";

	private JSONObject m_entity;
	
	private HAPAttachmentEntity() {}
	
	public HAPAttachmentEntity(String resourceType) {
		this.setResourceType(resourceType);
	}
	
	@Override
	public String getType() {
		return HAPConstant.ATTACHMENT_TYPE_ENTITY;
	}

	public JSONObject getEntity() {    return this.m_entity;    }
	public void setEntity(JSONObject entityObj) {    this.m_entity = entityObj;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_entity!=null)		jsonMap.put(ENTITY, HAPJsonUtility.buildJson(this.m_entity, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_entity = jsonObj.optJSONObject(ENTITY);
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentEntity) {
			HAPAttachmentEntity ele = (HAPAttachmentEntity)obj;
			if(super.equals(ele)) {
				if(HAPBasicUtility.isEquals(ele.m_entity, this.m_entity)) {
					out = true;
				}
			}
		}
		return out;
	}
	
	@Override
	public HAPAttachmentEntity clone() {
		HAPAttachmentEntity out = new HAPAttachmentEntity();
		this.cloneToObject(out);
		out.m_entity = this.m_entity;
		return out;
	}
}