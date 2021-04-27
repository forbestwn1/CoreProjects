package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinitionOrId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionGroup;

public abstract class HAPResourceDefinitionComplexImp extends HAPWithAttachmentImp implements HAPDefinitionResourceComplex{

	private HAPResourceId m_resourceId;
	
	//context definition within this component
	private HAPContextStructureValueDefinition m_context;
	
	private List<HAPContextReference> m_contextRefs;
	
	public HAPResourceDefinitionComplexImp() {
		this.m_contextRefs = new ArrayList<HAPContextReference>();
	}

	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	@Override
	public String getResourceType() {   return this.getResourceId().getType();  }

	@Override
	public void setResourceId(HAPResourceId resourceId) {  this.m_resourceId = resourceId;   }
	@Override
	public HAPResourceId getResourceId() {   return this.m_resourceId;   }
	
	@Override
	public HAPContextStructureValueDefinition getValueContext() {  return this.m_context;   }
	@Override
	public void setValueContext(HAPContextStructureValueDefinition context) {  
		this.m_context = context;
		if(this.m_context ==null)  this.m_context = new HAPContextStructureValueDefinitionGroup();
	}
	
	@Override
	public List<HAPContextReference> getContextReferences(){   return this.m_contextRefs; 	}
	public void addContextReference(HAPContextReference contextRef) {   this.m_contextRefs.add(contextRef);    }
	
	public HAPContextStructureValueDefinitionGroup getContextNotFlat() {   return (HAPContextStructureValueDefinitionGroup)this.getValueContext();    }
	public HAPContextStructureValueDefinitionFlat getContextFlat() {    return (HAPContextStructureValueDefinitionFlat)this.getValueContext();    }
	
	@Override
	public HAPResourceDefinitionOrId getChild(String path) {   return null;    }

	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cloneToValueContext(HAPWithValueContext withDataContext) {
		if(this.m_context!=null)	withDataContext.setValueContext(this.m_context.cloneContextStructure());
	}

	@Override
	public void cloneToResourceDefinition(HAPResourceDefinition resourceDef) {
		resourceDef.setResourceId(this.getResourceId()==null?null:this.getResourceId().clone());
	}

	@Override
	public void cloneToComplexResourceDefinition(HAPDefinitionResourceComplex complexEntity) {
		this.cloneToEntityInfo(complexEntity);
		this.cloneToResourceDefinition(complexEntity);
		this.cloneToAttachment(complexEntity);
		this.cloneToValueContext(complexEntity);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(HAPWithAttachment.ATTACHMENT, this.getAttachmentContainer().toStringValue(HAPSerializationFormat.JSON));
	}

}
