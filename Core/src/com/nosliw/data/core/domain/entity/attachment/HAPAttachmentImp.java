package com.nosliw.data.core.domain.entity.attachment;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;

public abstract class HAPAttachmentImp extends HAPEntityInfoWritableImp implements HAPAttachment{

	private String m_valueType;
	
	private Object m_adaptor;

	public HAPAttachmentImp() {}

	public HAPAttachmentImp(String valueType) {
		this.m_valueType  = valueType;
	}
	
	@Override
	public String getValueType() {   return this.m_valueType;   }
	
	@Override
	public void setValueType(String valueType) {    this.m_valueType = valueType;    }

	@Override
	public Object getAdaptor() {   return this.m_adaptor;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, this.getValueType());
		jsonMap.put(ADAPTOR, HAPSerializeManager.getInstance().toStringValue(this.m_adaptor, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		if(this.m_valueType==null)   this.m_valueType = jsonObj.getString(VALUETYPE);
		this.m_adaptor = jsonObj.opt(ADAPTOR);
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentImp) {
			HAPAttachmentImp ele = (HAPAttachmentImp)obj;
			if(super.equals(ele)) {
				if(HAPBasicUtility.isEquals(ele.getValueType(), this.getValueType())) {
					out = true;
				}
			}
		}
		return out;
	}

	public void cloneToAttachment(HAPAttachmentImp obj) {
		this.cloneToEntityInfo(obj);
		obj.setValueType(this.getValueType());
		obj.m_adaptor = this.m_adaptor;
	}
}
