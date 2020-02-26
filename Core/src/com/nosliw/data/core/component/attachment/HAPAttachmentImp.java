package com.nosliw.data.core.component.attachment;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPBasicUtility;

public abstract class HAPAttachmentImp extends HAPEntityInfoWritableImp implements HAPAttachment{

	@HAPAttribute
	public static String RESOURCETYPE = "resourceType";
	
	private String m_resourceType;

	@Override
	public String getResourceType() {   return this.m_resourceType;   }
	public void setResourceType(String resourceType) {    this.m_resourceType = resourceType;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCETYPE, this.getResourceType());
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		if(this.m_resourceType==null)   this.m_resourceType = jsonObj.getString(RESOURCETYPE);
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentImp) {
			HAPAttachmentImp ele = (HAPAttachmentImp)obj;
			if(super.equals(ele)) {
				if(HAPBasicUtility.isEquals(ele.getResourceType(), this.getResourceType())) {
						out = true;
				}
			}
		}
		return out;
	}

	public void cloneToObject(HAPAttachmentImp obj) {
		this.cloneToEntityInfo(obj);
		this.setResourceType(obj.getResourceType());
	}
	
	@Override
	public HAPAttachmentImp clone() {
		return null;
	}

}
