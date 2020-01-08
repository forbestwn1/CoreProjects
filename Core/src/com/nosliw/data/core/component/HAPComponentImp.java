package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContextGroup;

abstract public class HAPComponentImp extends HAPEntityInfoWritableImp implements HAPComponent{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String CONTEXT = "context";
	
	private String m_id;

	//context definition within this component
	private HAPContextGroup m_context;
	
	private HAPAttachmentContainer m_attachmentContainer;
	
	public HAPComponentImp(String id) {
		this.m_id = id;
		this.m_context = new HAPContextGroup();
		this.m_attachmentContainer = new HAPAttachmentContainer();
	}
	
	@Override
	public String getId() {   return this.m_id;   }
	@Override
	public void setId(String id) {  this.m_id = id;   }
 	 
	@Override
	public HAPContextGroup getContext() {  return this.m_context;   }
	@Override
	public void setContext(HAPContextGroup context) {  
		this.m_context = context;
		if(this.m_context ==null)  this.m_context = new HAPContextGroup();
	}
	
	@Override
	public HAPAttachmentContainer getAttachmentContainer() {		return this.m_attachmentContainer;	}

	@Override
	public Map<String, HAPAttachment> getAttachmentsByType(String type) {
		return this.m_attachmentContainer.getAttachmentByType(type);
	}

	@Override
	public void mergeBy(HAPWithAttachment parent, String mode) {
		this.m_attachmentContainer.merge(parent.getAttachmentContainer(), mode);
	}
 
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
	}
}
