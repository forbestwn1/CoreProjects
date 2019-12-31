package com.nosliw.data.core.component;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;

//
@HAPEntityWithAttribute
public class HAPAttachmentReference extends HAPEntityInfoWritableImp implements HAPAttachment{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String RESOURCETYPE = "resourceType";
	
	private String m_resourceType;
	
	private HAPResourceId m_id;
	
	public HAPAttachmentReference(String resourceType) {
		this.m_resourceType = resourceType;
	}

	@Override
	public String getType() {  return HAPConstant.ATTACHMENT_TYPE_REFERENCE;  }
	
	public HAPResourceId getId() {	return this.m_id;  }
	public void setId(HAPResourceId id) {    this.m_id = id;    }
	
	private String getResourceType() {
		if(this.m_resourceType==null && this.m_id!=null) {
			this.m_resourceType = this.m_id.getType();
		}
		return this.m_resourceType;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCETYPE, this.getResourceType());
		if(this.m_id!=null)		jsonMap.put(ID, this.m_id.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		if(this.m_resourceType==null)   this.m_resourceType = jsonObj.getString(RESOURCETYPE);
		Object id = jsonObj.opt(ID);
		if(id!=null) {
			this.m_id = HAPResourceId.newInstance(this.m_resourceType, id);
		}
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentReference) {
			HAPAttachmentReference ele = (HAPAttachmentReference)obj;
			if(super.equals(ele)) {
				if(HAPBasicUtility.isEquals(ele.getResourceType(), this.getResourceType())) {
					if(HAPBasicUtility.isEquals(ele.m_id, this.m_id)) {
						out = true;
					}
				}
			}
		}
		return out;
	}
	
	@Override
	public HAPAttachmentReference clone() {
		HAPAttachmentReference out = new HAPAttachmentReference(this.getResourceType());
		this.cloneToEntityInfo(out);
		out.m_id = this.m_id.clone();
		return out;
	}

}
