package com.nosliw.uiresource.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPChildrenComponentId;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.uiresource.module.HAPDefinitionModuleUI;

@HAPEntityWithAttribute
public class HAPDefinitionAppElementUI  extends HAPComponentImp implements HAPDefinitionAppElement{

	@HAPAttribute
	public static final String MODULE = "module";

	//all modules in this entry
	private List<HAPDefinitionAppModule> m_modules;

	private HAPDefinitionAppElementUI() {}
	
	public HAPDefinitionAppElementUI(String id) {
		super(id);
		this.m_modules = new ArrayList<HAPDefinitionAppModule>();
	}
	
	public List<HAPDefinitionAppModule> getModules(){  return this.m_modules;  }
	public void addModules(List<HAPDefinitionAppModule> modules) {   if(modules!=null)   this.m_modules.addAll(modules);   }
	public void addModule(HAPDefinitionAppModule module) {  this.m_modules.add(module);  }
	
	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		HAPChildrenComponentIdContainer out = new HAPChildrenComponentIdContainer();

		//module part
		for(HAPDefinitionAppModule module : this.getModules()) {
			if(!HAPDefinitionModuleUI.STATUS_DISABLED.equals(module.getStatus())) {
				HAPAttachmentContainer mappedParentAttachment = HAPUtilityComponent.buildNameMappedAttachment(this.getAttachmentContainer(), module);
				HAPAttachmentReference moduleAttachment = (HAPAttachmentReference)this.getAttachmentContainer().getElement(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE, module.getModule());
				out.addChildCompoentId(new HAPChildrenComponentId(module.getName(), moduleAttachment.getId(), module.getInfo()), mappedParentAttachment);
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
	public HAPComponent cloneComponent() {  return cloneAppUIElementDefinition();	}
}
