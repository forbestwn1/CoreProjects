package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinitionOrId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public abstract class HAPResourceDefinitionComplexImp extends HAPWithAttachmentImp implements HAPDefinitionResourceComplex{

	private HAPResourceId m_resourceId;
	
	//value structure definition within this component
	private HAPWrapperValueStructure m_valueStructureWrapper;
	
	private HAPLocalReferenceBase m_localReferenceBase;

	public HAPResourceDefinitionComplexImp() {
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
	public HAPWrapperValueStructure getValueStructureWrapper() {  return this.m_valueStructureWrapper;   }
	@Override
	public void setValueStructureWrapper(HAPWrapperValueStructure valueStructure) {		this.m_valueStructureWrapper = valueStructure;	}
	
	public HAPValueStructure getValueStructure() {
		if(this.m_valueStructureWrapper==null)   return null;
		else return this.m_valueStructureWrapper.getValueStructure();
	}
	public void setValueStructure(HAPValueStructure valueStructure) { 
		if(this.m_valueStructureWrapper==null)    this.m_valueStructureWrapper = new HAPWrapperValueStructure(valueStructure);
		else  this.m_valueStructureWrapper.setValueStructure(valueStructure);
	}
	
	public HAPValueStructureDefinitionGroup getValueStructureGroup() {   return (HAPValueStructureDefinitionGroup)this.getValueStructureWrapper().getValueStructure();    }
	public HAPValueStructureDefinitionFlat getValueStructureFlat() {    return (HAPValueStructureDefinitionFlat)this.getValueStructureWrapper().getValueStructure();    }
	
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
	public void cloneToResourceDefinition(HAPResourceDefinition resourceDef) {
		resourceDef.setResourceId(this.getResourceId()==null?null:this.getResourceId().clone());
	}

	@Override
	public void cloneToComplexResourceDefinition(HAPDefinitionResourceComplex complexEntity, boolean cloneValueStructure) {
		this.cloneToEntityInfo(complexEntity);
		this.cloneToResourceDefinition(complexEntity);
		this.cloneToAttachment(complexEntity);

		if(this.getValueStructureWrapper()!=null) {
			if(cloneValueStructure) complexEntity.setValueStructureWrapper(this.getValueStructureWrapper().cloneValueStructureWrapper());
			else  complexEntity.setValueStructureWrapper(this.getValueStructureWrapper()); 
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildJson(this.m_valueStructureWrapper, HAPSerializationFormat.JSON));
		jsonMap.put(HAPWithAttachment.ATTACHMENT, this.getAttachmentContainer().toStringValue(HAPSerializationFormat.JSON));
	}

}
