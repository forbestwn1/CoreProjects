package com.nosliw.data.core.component.attachment;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;

//
@HAPEntityWithAttribute
public class HAPAttachmentReference extends HAPAttachmentImp{

	@HAPAttribute
	public static String ID = "id";
	
	private HAPResourceId m_id;

	private HAPAttachmentReference() {	}
	
	public HAPAttachmentReference(String resourceType) {
		this.setResourceType(resourceType);
	}

	public HAPAttachmentReference(HAPResourceId resourceId) {
		this.m_id = resourceId;
	}

	@Override
	public String getType() {  return HAPConstant.ATTACHMENT_TYPE_REFERENCE;  }
	
	public HAPResourceId getId() {	return this.m_id;  }
	public void setId(HAPResourceId id) {    
		this.m_id = id;
		this.setResourceType(this.m_id.getType());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_id!=null)		jsonMap.put(ID, this.m_id.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		Object id = jsonObj.opt(ID);
		if(id!=null) {
			this.m_id = HAPResourceIdFactory.newInstance(this.getResourceType(), id);
		}
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentReference) {
			HAPAttachmentReference ele = (HAPAttachmentReference)obj;
			if(super.equals(ele)) {
				if(HAPBasicUtility.isEquals(ele.m_id, this.m_id)) {
					out = true;
				}
			}
		}
		return out;
	}
	
	@Override
	public HAPAttachmentReference cloneAttachment() {
		HAPAttachmentReference out = new HAPAttachmentReference();
		super.cloneToObject(out);
		out.m_id = this.m_id.clone();
		return out;
	}
}
