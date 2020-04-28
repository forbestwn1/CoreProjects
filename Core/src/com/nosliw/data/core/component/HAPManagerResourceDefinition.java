package com.nosliw.data.core.component;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceIdTemplate;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.template.HAPManagerTemplate;

public class HAPManagerResourceDefinition {

	private Map<String, HAPPluginResourceDefinition> m_plugins;
	private HAPManagerTemplate m_templateMan;
	
	public HAPManagerResourceDefinition(HAPManagerTemplate templateMan) {
		this.m_plugins = new LinkedHashMap<String, HAPPluginResourceDefinition>();
		this.m_templateMan = templateMan;
	}
	
	public HAPResourceDefinition getResourceDefinition(HAPResourceId resourceId) {
		HAPResourceDefinition out = null;
		String structure = resourceId.getStructure();
		if(structure.equals(HAPConstant.RESOURCEID_TYPE_SIMPLE)) {
			HAPResourceIdSimple simpleId = (HAPResourceIdSimple)resourceId;
			String type = simpleId.getType();
			out = this.m_plugins.get(type).getResource(simpleId);
		}
		else if(structure.equals(HAPConstant.RESOURCEID_TYPE_TEMPLATE)) {
			HAPResourceIdTemplate templateResourceId = (HAPResourceIdTemplate)resourceId;
//			this.m_templateMan.build(templateResourceId.getTemplateId(), templateResourceId.getParms());
		}
		
		if(out instanceof HAPWithAttachment) {
			//merge attachment with supplment in resource id
			((HAPWithAttachment)out).getAttachmentContainer().merge(new HAPAttachmentContainer(resourceId.getSupplement()), HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
		}
		
		//set resource id
		out.setResourceId(resourceId);
		return out;
	}

	public HAPResourceDefinitionComplex getAdjustedComplextResourceDefinition(HAPResourceId resourceId, HAPAttachmentContainer parentAttachment) {
		HAPResourceDefinitionComplex out = (HAPResourceDefinitionComplex)this.getResourceDefinition(resourceId);
		HAPUtilityComponent.mergeWithParentAttachment(out, parentAttachment);
		return out;
	}
	
	public HAPResourceDefinition parseResourceDefinition(String type, Object content) {
		return this.m_plugins.get(type).parseResourceDefinition(content);
	}
	
	public void registerPlugin(HAPPluginResourceDefinition plugin) {
		this.m_plugins.put(plugin.getResourceType(), plugin);
	}
}
