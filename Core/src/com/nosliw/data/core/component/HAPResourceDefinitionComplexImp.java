package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPContextGroup;

public abstract class HAPResourceDefinitionComplexImp extends HAPEntityInfoWritableImp implements HAPResourceDefinitionComplex{

	private HAPResourceId m_resourceId;
	
	//context definition within this component
	private HAPContextGroup m_context;
	
	private HAPAttachmentContainer m_attachmentContainer;

	public HAPResourceDefinitionComplexImp() {
		this.m_context = new HAPContextGroup();
		this.m_attachmentContainer = new HAPAttachmentContainer();
	}

	@Override
	public String getResourceType() {   return this.getResourceId().getType();  }

	@Override
	public void setResourceId(HAPResourceId resourceId) {  this.m_resourceId = resourceId;   }
	@Override
	public HAPResourceId getResourceId() {   return this.m_resourceId;   }
	
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
	public void setAttachmentContainer(HAPAttachmentContainer attachmentContainer) {  this.m_attachmentContainer = attachmentContainer;}  

	@Override
	public Map<String, HAPAttachment> getAttachmentsByType(String type) {	return this.m_attachmentContainer.getAttachmentByType(type);	}

	@Override
	public void mergeBy(HAPWithAttachment parent, String mode) {	this.m_attachmentContainer.merge(parent.getAttachmentContainer(), mode);	}
 
	@Override
	public void cloneToDataContext(HAPWithDataContext withDataContext) {
		this.m_context.cloneTo(withDataContext.getContext());
	}

	@Override
	public void cloneToAttachment(HAPWithAttachment withAttachment) {
		withAttachment.setAttachmentContainer(this.m_attachmentContainer.cloneAttachmentContainer());
	}

	@Override
	public void cloneToResourceDefinition(HAPResourceDefinition resourceDef) {
		resourceDef.setResourceId(this.getResourceId().clone());
	}

	@Override
	public void cloneToComplexResourceDefinition(HAPResourceDefinitionComplex complexEntity) {
		this.cloneToEntityInfo(complexEntity);
		this.cloneToResourceDefinition(complexEntity);
		this.cloneToAttachment(complexEntity);
		this.cloneToDataContext(complexEntity);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(HAPWithAttachment.ATTACHMENT, m_attachmentContainer.toStringValue(HAPSerializationFormat.JSON));
	}

}
