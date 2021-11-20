package com.nosliw.data.core.component;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoUtility;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPDefinitionEntityContainer;
import com.nosliw.data.core.complex.attachment.HAPAttachment;
import com.nosliw.data.core.complex.attachment.HAPContainerAttachment;
import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;
import com.nosliw.data.core.component.command.HAPDefinitionCommand;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;

//component that defined as element of another container component
public abstract class HAPDefinitionEntityElementInContainerComponent extends HAPDefinitionEntityElementInContainerComplex implements HAPDefinitionEntityComponent{

	@HAPAttribute
	public static String CONTAINER = "container";

	@HAPAttribute
	public static String COMPONENTENTITY = "componentEntity";
 
	@HAPAttribute
	public static String ELEMENTID = "elementId";

	private HAPDefinitionEntityContainer m_componentContainer;
	
	private String m_elementId;
	
	//calculate out
	private HAPDefinitionEntityComponent m_componentEntity;
	
	protected HAPDefinitionEntityElementInContainerComponent() {}
	
	public HAPDefinitionEntityElementInContainerComponent(HAPDefinitionEntityContainer componentContainer, String elementId) {
		this.m_componentContainer = componentContainer;
		this.m_elementId = elementId;
		this.m_componentEntity = ((HAPDefinitionEntityComponent)this.getResourceContainer().getContainerElement(this.getElementId())).cloneComponent();
		HAPUtilityComponent.mergeWithParentAttachment(this.m_componentEntity, this.m_componentContainer.getAttachmentContainer());    //build attachment
		//build value structure
//		HAPUtilityComplexValueStructure.setParentPart(m_componentEntity, componentContainer);
		
		HAPInfoUtility.softMerge(this.m_componentEntity.getInfo(), this.m_componentContainer.getInfo());
	}

	@Override
	public HAPComplexValueStructure getValueStructureComplex() {	return this.m_componentEntity.getValueStructureComplex();	}

	@Override
	public HAPComplexValueStructure getContainerValueStructureComplex() {	return this.m_componentContainer.getValueStructureComplex();	}
	
	@Override
	public HAPDefinitionEntityContainer getResourceContainer() {   return this.m_componentContainer;    }
	@Override
	public void setResourceContainer(HAPDefinitionEntityContainer container) {   this.m_componentContainer = container;     }
	
	@Override
	public String getElementId() {   return this.m_elementId;   }
	@Override
	public void setElementId(String id) {   this.m_elementId = id;    }
	
	@Override
	public HAPDefinitionEntityComponent getComponentEntity() {   return this.m_componentEntity;   }

	@Override
	public HAPInfo getInfo() {		return this.m_componentEntity.getInfo();	}
	
	protected void cloneToComponentContainerElement(HAPDefinitionEntityElementInContainerComponent componentContainerElement) {
		componentContainerElement.m_elementId = this.m_elementId;
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
	public HAPDefinitionTaskSuite getTaskSuite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTask(HAPDefinitionTask task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTaskSuite(HAPDefinitionTaskSuite suite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<HAPDefinitionCommand> getCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCommand(HAPDefinitionCommand command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<HAPDefinitionEvent> getEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDefinitionEvent getEvent(String eventName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEvent(HAPDefinitionEvent event) {
		// TODO Auto-generated method stub
		
	}


}
