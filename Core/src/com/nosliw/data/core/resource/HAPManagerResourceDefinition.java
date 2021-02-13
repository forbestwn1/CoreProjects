package com.nosliw.data.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.component.HAPDefinitionResourceComplex;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentUtility;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;

public class HAPManagerResourceDefinition {

	private Map<String, HAPPluginResourceDefinition> m_plugins;
	private HAPManagerDynamicResource m_dynamicResourceManager;
	
	public HAPManagerResourceDefinition(HAPManagerDynamicResource dynamicResourceMan) {
		this.m_plugins = new LinkedHashMap<String, HAPPluginResourceDefinition>();
		this.m_dynamicResourceManager = dynamicResourceMan;
	}
	
	public HAPResourceDefinition getResourceDefinition(HAPResourceId resourceId) {
		HAPResourceDefinition out = null;
		String structure = resourceId.getStructure();
		if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
			HAPResourceIdSimple simpleId = (HAPResourceIdSimple)resourceId;
			String type = simpleId.getType();
			out = this.m_plugins.get(type).getResource(simpleId);
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_DYNAMIC)) {
			HAPResourceIdDynamic dynamicResourceId = (HAPResourceIdDynamic)resourceId;
			out = this.m_dynamicResourceManager.buildResource(dynamicResourceId.getBuilderId(), dynamicResourceId.getParms());
		} 
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
			HAPResourceIdLocal localResourceId = (HAPResourceIdLocal)resourceId;
			String path = localResourceId.getBasePath().getPath() + localResourceId.getType() + "/" + localResourceId.getName() + ".res";
			out = this.parseResourceDefinition(localResourceId.getType(), HAPFileUtility.readFile(path));
			if(out instanceof HAPWithAttachment) {
				((HAPWithAttachment)out).setLocalReferenceBase(localResourceId.getBasePath());
			}
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
			
		}
		
		if(out instanceof HAPWithAttachment) {
			//merge attachment with supplment in resource id
			HAPAttachmentUtility.mergeAttachmentInResourceIdSupplementToContainer(resourceId, ((HAPWithAttachment)out).getAttachmentContainer(), HAPConstant.INHERITMODE_PARENT);
		}
		
		//set resource id
		out.setResourceId(resourceId);
		return out;
	}

	public HAPDefinitionResourceComplex getAdjustedComplextResourceDefinition(HAPResourceId resourceId, HAPContainerAttachment parentAttachment) {
		HAPDefinitionResourceComplex out = (HAPDefinitionResourceComplex)this.getResourceDefinition(resourceId);
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
