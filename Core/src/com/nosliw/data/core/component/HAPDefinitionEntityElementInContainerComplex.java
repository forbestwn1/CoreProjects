package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPUtilityInfo;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueContext;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.domain.complexentity.HAPDefinitionEntityContainer;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.resource.HAPResourceId;

//component that defined as element of another container component
public abstract class HAPDefinitionEntityElementInContainerComplex extends HAPSerializableImp implements HAPManualBrickComplex{

	@HAPAttribute
	public static String CONTAINER = "container";

	@HAPAttribute
	public static String COMPONENTENTITY = "componentEntity";
 
	@HAPAttribute
	public static String ELEMENTID = "elementId";

	private HAPResourceId m_resourceId;

	private HAPDefinitionEntityContainer m_componentContainer;
	
	private String m_elementId;
	
	//calculate out
	private HAPDefinitionEntityComponent m_componentEntity;
	
	protected HAPDefinitionEntityElementInContainerComplex() {}
	
	public HAPDefinitionEntityElementInContainerComplex(HAPDefinitionEntityContainer componentContainer, String elementId) {
		this.m_componentContainer = componentContainer;
		this.m_elementId = elementId;
		this.m_componentEntity = ((HAPDefinitionEntityComponent)this.getResourceContainer().getContainerElement(this.getElementId())).cloneComponent();
		HAPUtilityComponent.mergeWithParentAttachment(this.m_componentEntity, this.m_componentContainer.getAttachmentContainer());    //build attachment
		//build value structure
//		HAPUtilityComplexValueStructure.setParentPart(m_componentEntity, componentContainer);
		
		HAPUtilityInfo.softMerge(this.m_componentEntity.getInfo(), this.m_componentContainer.getInfo());
	}

	@Override
	public HAPManualBrickValueContext getValueStructureComplex() {	return this.m_componentEntity.getValueContext();	}

	public HAPManualBrickValueContext getContainerValueStructureComplex() {	return this.m_componentContainer.getValueContext();	}
	
	
	public HAPDefinitionEntityContainer getResourceContainer() {   return this.m_componentContainer;    }
	public void setResourceContainer(HAPDefinitionEntityContainer container) {   this.m_componentContainer = container;     }
	
	public String getElementId() {   return this.m_elementId;   }
	public void setElementId(String id) {   this.m_elementId = id;    }
	
	public HAPDefinitionEntityComponent getComponentEntity() {   return this.m_componentEntity;   }

	@Override
	public HAPInfo getInfo() {		return this.m_componentEntity.getInfo();	}
	
	@Override
	public HAPDefinitionEntityContainerAttachment getAttachmentContainer() {  return this.getComponentEntity().getAttachmentContainer(); }

	@Override
	public HAPAttachment getAttachment(String type, String name) {  return this.getAttachmentContainer().getElement(type, name);  }

	@Override
	public HAPAttachment getAttachment(HAPReferenceAttachment idAttachment) {   return this.getAttachmentContainer().getElement(idAttachment);  }

	protected void cloneToComponentContainerElement(HAPDefinitionEntityElementInContainerComplex componentContainerElement) {
		componentContainerElement.m_elementId = this.m_elementId;
		componentContainerElement.m_resourceId = this.m_resourceId.clone();
		componentContainerElement.m_componentContainer = this.m_componentContainer.cloneResourceDefinitionContainer();
		componentContainerElement.m_componentEntity = this.m_componentEntity.cloneComponent();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INFO, HAPUtilityJson.buildJson(this.getInfo(), HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTID, this.m_elementId);
		jsonMap.put(CONTAINER, HAPUtilityJson.buildJson(this.m_componentContainer, HAPSerializationFormat.JSON));
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
	public void setAttachmentContainer(HAPDefinitionEntityContainerAttachment attachmentContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cloneToAttachment(HAPWithAttachment withAttachment) {
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

}
