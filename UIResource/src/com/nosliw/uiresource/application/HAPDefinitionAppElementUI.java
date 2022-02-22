package com.nosliw.uiresource.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPInfoChildResource;
import com.nosliw.data.core.component.HAPContainerChildReferenceResource;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentReference;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;

@HAPEntityWithAttribute
public class HAPDefinitionAppElementUI  extends HAPDefinitionAppElement{

	@HAPAttribute
	public static final String MODULE = "module";

	//all modules in this entry
	private List<HAPDefinitionAppModule> m_modules;

	private HAPDefinitionAppElementUI() {
		this(null);
	}
	
	public HAPDefinitionAppElementUI(String id) {
		super(id);
		this.m_modules = new ArrayList<HAPDefinitionAppModule>();
	}
	
	public List<HAPDefinitionAppModule> getModules(){  return this.m_modules;  }
	public void addModules(List<HAPDefinitionAppModule> modules) {   if(modules!=null)   this.m_modules.addAll(modules);   }
	public void addModule(HAPDefinitionAppModule module) {  this.m_modules.add(module);  }
	
	@Override
	public HAPContainerChildReferenceResource getChildrenComponentId() {
		HAPContainerChildReferenceResource out = new HAPContainerChildReferenceResource();

		//module part
		for(HAPDefinitionAppModule module : this.getModules()) {
			if(HAPUtilityEntityInfo.isEnabled(module)) {
				HAPDefinitionEntityContainerAttachment mappedParentAttachment = HAPUtilityComponent.buildNameMappedAttachment(this.getAttachmentContainer(), module);
				HAPAttachmentReference moduleAttachment = (HAPAttachmentReference)this.getAttachmentContainer().getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULE, module.getModule());
				out.addChildCompoentId(new HAPInfoChildResource(module.getName(), moduleAttachment.getReferenceId(), module.getInfo()), mappedParentAttachment);
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULE, HAPJsonUtility.buildJson(this.m_modules, HAPSerializationFormat.JSON));
	}

	public HAPDefinitionAppElementUI cloneAppUIElementDefinition() {
		HAPDefinitionAppElementUI out = new HAPDefinitionAppElementUI();
		this.cloneToComponent(out);
		for(HAPDefinitionAppModule module : this.m_modules) {
			out.addModule(module.cloneAppModuleDef());
		}
		return out;
	}
	
	@Override
	public HAPDefinitionEntityComponent cloneComponent() {  return cloneAppUIElementDefinition();	}
}
