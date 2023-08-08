package com.nosliw.data.core.domain.entity.attachment;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public abstract class HAPAttachmentImp extends HAPEntityInfoWritableImp implements HAPAttachment{

	private String m_valueType;
	
	private Object m_rawValue;
	
	private HAPIdEntityInDomain m_entityId;

	public HAPAttachmentImp() {}

	public HAPAttachmentImp(String valueType, Object rawValue, HAPIdEntityInDomain entityId, HAPEntityInfo entityInfo) {
		this.m_valueType  = valueType;
		this.m_rawValue = rawValue;
		this.m_entityId = entityId;
		entityInfo.cloneToEntityInfo(this);
	}
	
	@Override
	public String getValueType() {   return this.m_valueType;   }
	
	@Override
	public Object getRawValue() {    return this.m_rawValue;    }
	
	@Override
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, this.getValueType());
		jsonMap.put(RAWVALUE, HAPSerializeManager.getInstance().toStringValue(this.m_rawValue, HAPSerializationFormat.JSON));
		jsonMap.put(ENTITYID, HAPSerializeManager.getInstance().toStringValue(this.m_entityId, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		if(this.m_valueType==null)   this.m_valueType = jsonObj.getString(VALUETYPE);
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentImp) {
			HAPAttachmentImp ele = (HAPAttachmentImp)obj;
			if(super.equals(ele)) {
				if(HAPUtilityBasic.isEquals(ele.getValueType(), this.getValueType())) {
					out = true;
				}
			}
		}
		return out;
	}

	public void cloneToAttachment(HAPAttachmentImp obj) {
		this.cloneToEntityInfo(obj);
		obj.m_valueType = this.m_valueType;
		obj.m_entityId = this.m_entityId;
		obj.m_rawValue = this.m_rawValue;
	}
}
