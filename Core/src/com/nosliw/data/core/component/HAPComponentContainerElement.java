package com.nosliw.data.core.component;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoUtility;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPUtilityWithValueStructure;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.component.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.handler.HAPHandler;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinitionOrId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public abstract class HAPComponentContainerElement extends HAPSerializableImp implements HAPComponent{

	@HAPAttribute
	public static String CONTAINER = "container";

	@HAPAttribute
	public static String COMPONENTENTITY = "componentEntity";
 
	@HAPAttribute
	public static String ELEMENTID = "elementId";

	private HAPResourceId m_resourceId;

	private HAPResourceDefinitionContainer m_componentContainer;
	
	private String m_elementId;
	
	//calculate out
	private HAPComponent m_componentEntity;
	
	//calculate out
	private HAPWrapperValueStructure m_valueStructureWrapper;
	
	protected HAPComponentContainerElement() {}
	
	public HAPComponentContainerElement(HAPResourceDefinitionContainer componentContainer, String elementId) {
		this.m_componentContainer = componentContainer;
		this.m_elementId = elementId;
		this.m_componentEntity = ((HAPComponent)this.getResourceContainer().getContainerElement(this.getElementId())).cloneComponent();
		HAPUtilityComponent.mergeWithParentAttachment(this.m_componentEntity, this.m_componentContainer.getAttachmentContainer());    //build attachment
		//build value structure
		
		this.m_valueStructureWrapper = new HAPWrapperValueStructure(HAPUtilityValueStructure.hardMerge(HAPUtilityWithValueStructure.getValueStructure(m_componentEntity), HAPUtilityWithValueStructure.getValueStructure(m_componentContainer)));   
		HAPInfoUtility.softMerge(this.m_componentEntity.getInfo(), this.m_componentContainer.getInfo());
	}

	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	
	@Override
	public HAPWrapperValueStructure getValueStructureWrapper() {	return this.m_valueStructureWrapper;	}

	@Override
	public void setValueStructureWrapper(HAPWrapperValueStructure valueStructureWrapper) {   this.m_valueStructureWrapper = valueStructureWrapper;	}

	public HAPResourceDefinitionContainer getResourceContainer() {   return this.m_componentContainer;    }
	public void setResourceContainer(HAPResourceDefinitionContainer container) {   this.m_componentContainer = container;     }
	
	public String getElementId() {   return this.m_elementId;   }
	public void setElementId(String id) {   this.m_elementId = id;    }
	
	public HAPComponent getComponentEntity() {   return this.m_componentEntity;   }

	@Override
	public HAPResourceId getResourceId() {   return this.m_resourceId;   }
	@Override
	public void setResourceId(HAPResourceId resourceId) {   this.m_resourceId = resourceId;    }
	
	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {	return this.m_componentEntity.getChildrenReferencedResource();	}

	@Override
	public HAPInfo getInfo() {		return this.m_componentEntity.getInfo();	}
	
	@Override
	public HAPContainerAttachment getAttachmentContainer() {  return this.getComponentEntity().getAttachmentContainer(); }

	//path base for local resource reference
	@Override
	public HAPLocalReferenceBase getLocalReferenceBase() {   return this.m_componentContainer.getLocalReferenceBase();	}

	@Override
	public void setLocalReferenceBase(HAPLocalReferenceBase localRefBase) {     }
	
	@Override
	public HAPAttachment getAttachment(String type, String name) {  return this.getAttachmentContainer().getElement(type, name);  }

	@Override
	public HAPAttachment getAttachment(HAPReferenceAttachment idAttachment) {   return this.getAttachmentContainer().getElement(idAttachment);  }

	protected void cloneToComponentContainerElement(HAPComponentContainerElement componentContainerElement) {
		componentContainerElement.m_elementId = this.m_elementId;
		componentContainerElement.m_resourceId = this.m_resourceId.clone();
		componentContainerElement.m_componentContainer = this.m_componentContainer.cloneResourceDefinitionContainer();
		componentContainerElement.m_componentEntity = this.m_componentEntity.cloneComponent();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.getInfo(), HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTID, this.m_elementId);
		jsonMap.put(CONTAINER, HAPJsonUtility.buildJson(this.m_componentContainer, HAPSerializationFormat.JSON));
	}
	
	@Override
	public void cloneToComplexResourceDefinition(HAPDefinitionResourceComplex complexEntity,
			boolean cloneValueStructure) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, HAPAttachment> getAttachmentsByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mergeBy(HAPWithAttachment parent, String mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<HAPHandlerLifecycle> getLifecycleAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLifecycleAction(HAPHandlerLifecycle lifecycleAction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<HAPHandler> getEventHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEventHandler(HAPHandler eventHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getAllServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addService(HAPDefinitionServiceUse service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStatus(String status) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInfo(HAPInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cloneToEntityInfo(HAPEntityInfo entityInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildEntityInfoByJson(Object json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttachmentContainer(HAPContainerAttachment attachmentContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cloneToAttachment(HAPWithAttachment withAttachment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cloneToResourceDefinition(HAPResourceDefinition resourceDef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPEntityInfo cloneEntityInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPDefinitionServiceUse getService(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPContextReference> getContextReferences() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public HAPResourceDefinitionOrId getChild(String path) {
		// TODO Auto-generated method stub
		return null;
	}


}
