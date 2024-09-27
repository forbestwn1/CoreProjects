package com.nosliw.core.application.division.manual.common.attachment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrickRoot;

public class HAPManualDefinitionAttachment extends HAPSerializableImp{

	public static String ITEM = "item"; 
	
	private Map<String, Map<String, Map<String, HAPManualDefinitionWrapperBrickRoot>>> m_items;
	
	public HAPManualDefinitionAttachment() {
		this.m_items = new LinkedHashMap<String, Map<String, Map<String, HAPManualDefinitionWrapperBrickRoot>>>();
	}

	public void addItem(HAPManualDefinitionWrapperBrickRoot item) {
		String brickType = item.getBrickTypeId().getBrickType();
		String brickVersion = item.getBrickTypeId().getVersion();
		Map<String, Map<String, Map<String, HAPManualDefinitionWrapperBrickRoot>>> def = this.getItems();
		Map<String, Map<String, HAPManualDefinitionWrapperBrickRoot>> byVersion = def.get(brickType);
		if(byVersion==null) {
			byVersion = new LinkedHashMap<String, Map<String, HAPManualDefinitionWrapperBrickRoot>>();
			def.put(brickType, byVersion);
		}
		
		Map<String, HAPManualDefinitionWrapperBrickRoot> items = byVersion.get(brickVersion);
		if(items==null) {
			items = new LinkedHashMap<String, HAPManualDefinitionWrapperBrickRoot>();
			byVersion.put(brickVersion, items);
		}
		items.put(item.getName(), item);
	}
	
	public Map<String, HAPManualDefinitionWrapperBrickRoot> getItemsByBrickType(String brickType, String brickVersion){
		Map<String, HAPManualDefinitionWrapperBrickRoot> out = null;
		Map<String, Map<String, HAPManualDefinitionWrapperBrickRoot>> byVersion = this.getItems().get(brickType);
		if(byVersion!=null) {
			out = byVersion.get(brickVersion);
		}
		return out;
	}

	public HAPManualDefinitionWrapperBrickRoot getItem(HAPIdBrickType brickTypeId, String name) {
		return getItem(brickTypeId.getBrickType(), brickTypeId.getVersion(), name);
	}

	
	public HAPManualDefinitionWrapperBrickRoot getItem(String brickType, String brickVersion, String name) {
		HAPManualDefinitionWrapperBrickRoot out = null;
		Map<String, HAPManualDefinitionWrapperBrickRoot> items = getItemsByBrickType(brickType, brickVersion);
		if(items!=null) {
			out = items.get(name);
		}
		return out;
	}
	
	public void mergeWith(HAPManualDefinitionAttachment parent, String mode) {
		if(parent==null) {
			return;
		}
		if(mode==null) {
			mode = HAPConstant.INHERITMODE_CHILD;
		}
		if(mode.equals(HAPConstant.INHERITMODE_NONE)) {
			return;
		}
		
		Map<String, Map<String, Map<String, HAPManualDefinitionWrapperBrickRoot>>> parentItems = parent.getItems();
		for(String brickType : parentItems.keySet()) {
			Map<String, Map<String, HAPManualDefinitionWrapperBrickRoot>> byVersion = parentItems.get(brickType);
			for(String version : byVersion.keySet()) {
				Map<String, HAPManualDefinitionWrapperBrickRoot> items = byVersion.get(version);
				for(String name : items.keySet()) {
					boolean override = false;
					if(mode.equals(HAPConstant.INHERITMODE_PARENT)) {
						override = true;
					}
					if(mode.equals(HAPConstant.INHERITMODE_CHILD)) {
						HAPManualDefinitionWrapperBrickRoot childItem = this.getItem(brickType, version, name);
						if(childItem==null) {
							override = true;
						}
					} 

					if(override) {
						//if configurable, then parent override child
						HAPManualDefinitionWrapperBrickRoot newAttachment = items.get(name).cloneBrickWrapper();
						HAPManualUtilityAttachment.setOverridenByParent(newAttachment);
						this.addItem(newAttachment);
					}
				}
			}
		}
	}
	
	private Map<String, Map<String, Map<String, HAPManualDefinitionWrapperBrickRoot>>> getItems(){   return this.m_items;   }
	
}
