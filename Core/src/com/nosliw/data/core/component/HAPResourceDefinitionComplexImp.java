package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructure;

public abstract class HAPResourceDefinitionComplexImp extends HAPWithAttachmentImp implements HAPResourceDefinitionComplex{

	private HAPResourceId m_resourceId;
	
	//context definition within this component
	private HAPContextStructure m_context;
	
	public HAPResourceDefinitionComplexImp() {
	}

	@Override
	public String getResourceType() {   return this.getResourceId().getType();  }

	@Override
	public void setResourceId(HAPResourceId resourceId) {  this.m_resourceId = resourceId;   }
	@Override
	public HAPResourceId getResourceId() {   return this.m_resourceId;   }
	
	@Override
	public HAPContextStructure getContextStructure() {  return this.m_context;   }
	@Override
	public void setContextStructure(HAPContextStructure context) {  
		this.m_context = context;
		if(this.m_context ==null)  this.m_context = new HAPContextGroup();
	}
	
	public HAPContextGroup getContextNotFlat() {   return (HAPContextGroup)this.getContextStructure();    }
	public HAPContext getContextFlat() {    return (HAPContext)this.getContextStructure();    }
	
	@Override
	public void cloneToDataContext(HAPWithDataContext withDataContext) {
		if(this.m_context!=null)	withDataContext.setContextStructure(this.m_context.cloneContextStructure());
	}

	@Override
	public void cloneToResourceDefinition(HAPResourceDefinition resourceDef) {
		resourceDef.setResourceId(this.getResourceId()==null?null:this.getResourceId().clone());
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
		jsonMap.put(HAPWithAttachment.ATTACHMENT, this.getAttachmentContainer().toStringValue(HAPSerializationFormat.JSON));
	}

}
