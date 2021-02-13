package com.nosliw.data.core.component.attachment;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceId;

//
@HAPEntityWithAttribute
public class HAPAttachmentReferenceExternal extends HAPAttachmentReference{

	@HAPAttribute
	public static String ADAPTOR = "adaptor";
	
	//adapter to fit the external entity to component
	private Object m_adaptor;
	
	public HAPAttachmentReferenceExternal() {	}
	
	public HAPAttachmentReferenceExternal(String resourceType) {
		super(resourceType);
	}
	
	public HAPAttachmentReferenceExternal(HAPResourceId resourceId) {
		super(resourceId);
	}

	@Override
	public String getType() {  return HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_adaptor!=null)		jsonMap.put(ADAPTOR, HAPSerializeManager.getInstance().toStringValue(this.m_adaptor, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		this.m_adaptor = jsonObj.opt(ADAPTOR);
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentReferenceExternal) {
			HAPAttachmentReferenceExternal ele = (HAPAttachmentReferenceExternal)obj;
			if(super.equals(ele)) {
				if(HAPBasicUtility.isEquals(this.m_adaptor, ele.m_adaptor))  out = true;
			}
		}
		return out;
	}
	
	@Override
	public HAPAttachmentReferenceExternal cloneAttachment() {
		HAPAttachmentReferenceExternal out = new HAPAttachmentReferenceExternal();
		super.cloneToReferenceAttachment(out);
		out.m_adaptor = this.m_adaptor;
		return out;
	}
}
