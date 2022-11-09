package com.nosliw.uiresource.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContainerChildReferenceResource;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.component.HAPDefinitionEntityComponentImp;
import com.nosliw.data.core.component.HAPInfoChildResource;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentReference;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.uiresource.common.HAPInfoDecoration;

/**
Module is a independent entity that is runnable within a container
It is a simple application which contains pages and transition between pages
Also be aware that module can be run in different container (pc browser, mobile browser, ...), try to make it platform independent
for instance, for a module that shows a school information, it contains two pages: list of school, school info
    when in mobile phone, the school info should overlap on top of list page
    when in pc, the list page and the school page should diplay sid by side

Don't need to define service information here, as service information will be gathered from all the mdoule ui definition
 */
@HAPEntityWithAttribute
public class HAPDefinitionModule extends HAPDefinitionEntityComponentImp{
	
	@HAPAttribute
	public static String UI = "ui";
	
	@HAPAttribute
	public static String UIDECORATION = "uiDecoration";
	
	// all the module uis (name -- definition)
	private List<HAPDefinitionModuleUI> m_uis;

	private List<HAPInfoDecoration> m_uiDecoration;
	
	public HAPDefinitionModule(String id) {
		this();
	}

	public HAPDefinitionModule() {
		this.m_uis = new ArrayList<HAPDefinitionModuleUI>();
		this.m_uiDecoration = new ArrayList<HAPInfoDecoration>();
	}
	
	public List<HAPDefinitionModuleUI> getUIs(){  return this.m_uis;  }
	public void addUI(HAPDefinitionModuleUI ui) {   this.m_uis.add(ui);   }
	
	public void setUIDecoration(List<HAPInfoDecoration> decs) {  this.m_uiDecoration = decs;    }
	public List<HAPInfoDecoration> getUIDecoration(){   return this.m_uiDecoration;    }
	
	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {
		HAPContainerChildReferenceResource out = new HAPContainerChildReferenceResource();
		//ui part
		for(HAPDefinitionModuleUI ui : this.getUIs()) {
			if(!HAPUtilityEntityInfo.isEnabled(ui)) {
				HAPDefinitionEntityContainerAttachment mappedParentAttachment = HAPUtilityComponent.buildNameMappedAttachment(this.getAttachmentContainer(), ui);
				HAPAttachmentReference pageAttachment = (HAPAttachmentReference)this.getAttachmentContainer().getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, ui.getPage());
				out.addChildCompoentId(new HAPInfoChildResource(ui.getName(), pageAttachment.getReferenceId(), ui.getInfo()), mappedParentAttachment);
			}
		}
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UI, HAPUtilityJson.buildJson(this.m_uis, HAPSerializationFormat.JSON));
		jsonMap.put(UIDECORATION, HAPUtilityJson.buildJson(this.m_uiDecoration, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDefinitionEntityComponent cloneComponent() {
		HAPDefinitionModule out = new HAPDefinitionModule();
		this.cloneToComponent(out, true);
		for(HAPDefinitionModuleUI ui : this.m_uis) {
			out.m_uis.add(ui.cloneModuleUIDef());
		}
		for(HAPInfoDecoration dec : this.m_uiDecoration) {
			out.m_uiDecoration.add(dec.cloneDecorationInfo());
		}
		return out;
	}

	@Override
	public String getValueStructureTypeIfNotDefined() {  return HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP;  }
}
