package com.nosliw.data.core.component;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoUtility;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPContextStructure;

public abstract class HAPComponentContainerElement extends HAPSerializableImp implements HAPComponent{

	@HAPAttribute
	public static String CONTAINER = "container";

	@HAPAttribute
	public static String ELEMENT = "element";

	@HAPAttribute
	public static String ELEMENTNAME = "elementName";

	private HAPResourceId m_resourceId;

	private HAPResourceDefinitionContainer m_componentContainer;
	
	private String m_elementName;
	
	private HAPComponent m_element;
	
	protected HAPComponentContainerElement() {}
	
	public HAPComponentContainerElement(HAPResourceDefinitionContainer componentContainer, String elementName) {
		this.m_componentContainer = componentContainer;
		this.m_elementName = elementName;
		this.setElement(((HAPComponent)this.getContainer().getElement(this.getElementName())).cloneComponent());
		HAPUtilityComponent.mergeWithParentAttachment(this.m_element, this.m_componentContainer.getAttachmentContainer());
		HAPInfoUtility.softMerge(this.m_element.getInfo(), this.m_componentContainer.getInfo());
	}

	public HAPResourceDefinitionContainer getContainer() {   return this.m_componentContainer;    }
	public void setResourceContainer(HAPResourceDefinitionContainer container) {   this.m_componentContainer = container;     }
	
	public String getElementName() {   return this.m_elementName;   }
	public void setElementName(String name) {   this.m_elementName = name;    }
	
	public HAPComponent getElement() {   return this.m_element;   }
	public void setElement(HAPComponent element) {   this.m_element = element;   }
	

	@Override
	public HAPResourceId getResourceId() {   return this.m_resourceId;   }
	@Override
	public void setResourceId(HAPResourceId resourceId) {   this.m_resourceId = resourceId;    }
	
	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {	return this.m_element.getChildrenComponentId();	}

	@Override
	public HAPInfo getInfo() {		return this.m_element.getInfo();	}
	
	@Override
	public HAPAttachmentContainer getAttachmentContainer() {  return this.getElement().getAttachmentContainer(); }

	protected void cloneToComponentContainerElement(HAPComponentContainerElement componentContainerElement) {
		componentContainerElement.m_elementName = this.m_elementName;
		componentContainerElement.m_resourceId = this.m_resourceId.clone();
		componentContainerElement.m_componentContainer = this.m_componentContainer.cloneResourceDefinitionContainer();
		componentContainerElement.m_element = this.m_element.cloneComponent();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.getInfo(), HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTNAME, this.m_elementName);
		jsonMap.put(CONTAINER, HAPJsonUtility.buildJson(this.m_componentContainer, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPContextStructure getContextStructure() {
		return null;
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
	public Set<HAPHandlerEvent> getEventHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEventHandler(HAPHandlerEvent eventHandler) {
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
	public void setContextStructure(HAPContextStructure context) {
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
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInfo(HAPInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildEntityInfoByJson(Object json) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void cloneToDataContext(HAPWithDataContext dataContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttachmentContainer(HAPAttachmentContainer attachmentContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cloneToAttachment(HAPWithAttachment withAttachment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cloneToComplexResourceDefinition(HAPResourceDefinitionComplex complexEntity) {
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


}
