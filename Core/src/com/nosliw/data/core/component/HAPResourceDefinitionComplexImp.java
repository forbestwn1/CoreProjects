package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinitionOrId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public abstract class HAPResourceDefinitionComplexImp extends HAPWithAttachmentImp implements HAPDefinitionResourceComplex{

	private HAPResourceId m_resourceId;
	
	//value structure definition within this component
	private HAPValueStructure m_valueStructure;
	
	private List<HAPContextReference> m_contextRefs;
	
	private HAPLocalReferenceBase m_localReferenceBase;

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
	public HAPValueStructure getValueStructure() {  return this.m_valueStructure;   }
	@Override
	public void setValueStructure(HAPValueStructure valueStructure) {
		this.m_valueStructure = valueStructure;
		if(this.m_valueStructure ==null)  this.m_valueStructure = new HAPValueStructureDefinitionGroup();
	}
	
	@Override
	public List<HAPContextReference> getContextReferences(){   return this.m_contextRefs; 	}
	public void addContextReference(HAPContextReference contextRef) {   this.m_contextRefs.add(contextRef);    }
	
	public HAPValueStructureDefinitionGroup getContextNotFlat() {   return (HAPValueStructureDefinitionGroup)this.getValueStructure();    }
	public HAPValueStructureDefinitionFlat getContextFlat() {    return (HAPValueStructureDefinitionFlat)this.getValueStructure();    }
	
	@Override
	public HAPResourceDefinitionOrId getChild(String path) {   return null;    }

	//path base for local resource reference
	@Override
	public HAPLocalReferenceBase getLocalReferenceBase() {   return this.m_localReferenceBase;	}

	@Override
	public void setLocalReferenceBase(HAPLocalReferenceBase localRefBase) {   this.m_localReferenceBase = localRefBase;  }
	
	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cloneToWithValueStructure(HAPWithValueStructure withDataContext) {
		if(this.m_valueStructure!=null)	withDataContext.setValueStructure((HAPValueStructure)this.m_valueStructure.cloneStructure());
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
		this.cloneToWithValueStructure(complexEntity);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildJson(this.m_valueStructure, HAPSerializationFormat.JSON));
		jsonMap.put(HAPWithAttachment.ATTACHMENT, this.getAttachmentContainer().toStringValue(HAPSerializationFormat.JSON));
	}

}
