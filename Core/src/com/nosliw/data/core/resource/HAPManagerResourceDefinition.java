package com.nosliw.data.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.data.core.component.HAPDefinitionResourceComplex;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;

public class HAPManagerResourceDefinition {

	private Map<String, HAPPluginResourceDefinition> m_plugins;
	private HAPManagerDynamicResource m_dynamicResourceManager;
	
	public HAPManagerResourceDefinition(HAPManagerDynamicResource dynamicResourceMan) {
		this.m_plugins = new LinkedHashMap<String, HAPPluginResourceDefinition>();
		this.m_dynamicResourceManager = dynamicResourceMan;
	}

	
	public HAPInfoResourceIdNormalize normalizeResourceId(HAPResourceId resourceId) {
		HAPInfoResourceIdNormalize out = null;
		String resourceType = resourceId.getResourceType();
		HAPPluginResourceDefinition resourcePlugin = this.m_plugins.get(resourceType);
		String resourceStructure = resourceId.getStructure();
		if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
			HAPResourceIdSimple simpleId = (HAPResourceIdSimple)resourceId;
			out = resourcePlugin.normalizeSimpleResourceId(simpleId);
		}
		else if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
			HAPResourceIdLocal localResourceId = (HAPResourceIdLocal)resourceId;
			out = resourcePlugin.normalizeLocalResourceId(localResourceId);
		}
		else if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
			HAPResourceIdEmbeded embededId = (HAPResourceIdEmbeded)resourceId;
			HAPInfoResourceIdNormalize normalizeParent = this.normalizeResourceId(embededId.getParentResourceId());
			out = new HAPInfoResourceIdNormalize(normalizeParent.getRootResourceId(), normalizeParent.getPath().appendPath(new HAPPath(embededId.getPath())).getPath(), embededId.getResourceType());
		}
		
		return out;
	}
	
	public HAPResourceDefinition getResourceDefinition(HAPResourceId resourceId, HAPDomainEntityDefinitionGlobal globalDomain) {
		return getResourceDefinition(resourceId, globalDomain, null);
	}

	public HAPResourceDefinition getResourceDefinition(HAPResourceId resourceId, HAPDomainEntityDefinitionGlobal globalDomain, String currentDomainResourceId) {
		HAPResourceDefinition out = null;
		out = globalDomain.getResourceDefinitionByResourceId(resourceId);
		if(out==null) {
			out = new HAPResourceDefinition(resourceId);
			String resourceType = resourceId.getResourceType();
			String resourceStructure = resourceId.getStructure();
			if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
				HAPResourceIdSimple simpleId = (HAPResourceIdSimple)resourceId;
				HAPIdEntityInDomain resourceEntityId = this.m_plugins.get(resourceType).getResourceEntityBySimpleResourceId(simpleId, globalDomain);
				out.setEntityId(resourceEntityId);
				globalDomain.addResourceDefinition(out);
			}
			else if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
				HAPResourceIdLocal localResourceId = (HAPResourceIdLocal)resourceId;
				HAPIdEntityInDomain entityId =  this.m_plugins.get(resourceType).getResourceEntityByLocalResourceId(localResourceId, globalDomain, currentDomainResourceId);
				out.setEntityId(entityId);
				//local resource to local domain
				globalDomain.getLocalDomainById(currentDomainResourceId).addLocalResourceDefinition(out);
			}
			else if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
				HAPResourceIdEmbeded embededId = (HAPResourceIdEmbeded)resourceId;
				//get parent resource def first
				HAPResourceDefinition parentResourceDef = this.getResourceDefinition(embededId.getParentResourceId(), globalDomain, currentDomainResourceId);
				HAPInfoEntityInDomainDefinition parentEntityInfo = globalDomain.getEntityInfoDefinition(parentResourceDef.getEntityId());
				HAPManualEntity parentEntity = parentEntityInfo.getEntity();
				//get child resource by path
//				HAPIdEntityInDomain entityId = HAPUtilityDomain.getEntityDescent(parentEntityInfo.getEntityId(), embededId.getPath(), entityDomain);
//				out.setEntityId(entityId);
//				globalDomain.setResourceDefinition(out, resourceId);
			}
			else if(resourceStructure.equals(HAPConstantShared.RESOURCEID_TYPE_DYNAMIC)) {
//				HAPResourceIdDynamic dynamicResourceId = (HAPResourceIdDynamic)resourceId;
//				out = this.m_dynamicResourceManager.buildResource(dynamicResourceId.getBuilderId(), dynamicResourceId.getParms());
			}
		}
		
//		if(out instanceof HAPWithAttachment) {
//			//merge attachment with supplment in resource id
//			HAPUtilityAttachment.mergeAttachmentInResourceIdSupplementToContainer(resourceId, ((HAPWithAttachment)out).getAttachmentContainer(), HAPConstant.INHERITMODE_PARENT);
//		}
		
		return out;
	}
	
	public void registerPlugin(HAPPluginResourceDefinition plugin) {
		this.m_plugins.put(plugin.getResourceType(), plugin);
	}
	
	public HAPDefinitionResourceComplex getAdjustedComplextResourceDefinition(HAPResourceId resourceId, HAPDefinitionEntityContainerAttachment parentAttachment) {
		HAPDefinitionResourceComplex out = (HAPDefinitionResourceComplex)this.getLocalResourceDefinition(resourceId);
		HAPUtilityComponent.mergeWithParentAttachment(out, parentAttachment);
		return out;
	}
}
