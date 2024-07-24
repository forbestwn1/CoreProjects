package com.nosliw.core.application.division.manual.common.attachment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrick;

public class HAPManualDefinitionAttachment extends HAPSerializableImp{

	public static String ITEM = "item"; 
	
	private Map<String, Map<String, Map<String, HAPManualDefinitionWrapperBrick>>> m_items;
	
	public HAPManualDefinitionAttachment() {
		this.m_items = new LinkedHashMap<String, Map<String, Map<String, HAPManualDefinitionWrapperBrick>>>();
	}

	public void addItem(HAPManualDefinitionWrapperBrick item) {
		String brickType = item.getBrickTypeId().getBrickType();
		String brickVersion = item.getBrickTypeId().getVersion();
		Map<String, Map<String, Map<String, HAPManualDefinitionWrapperBrick>>> def = this.getItems();
		Map<String, Map<String, HAPManualDefinitionWrapperBrick>> byVersion = def.get(brickType);
		if(byVersion==null) {
			byVersion = new LinkedHashMap<String, Map<String, HAPManualDefinitionWrapperBrick>>();
			def.put(brickType, byVersion);
		}
		
		Map<String, HAPManualDefinitionWrapperBrick> items = byVersion.get(brickVersion);
		if(items==null) {
			items = new LinkedHashMap<String, HAPManualDefinitionWrapperBrick>();
			byVersion.put(brickVersion, items);
		}
		items.put(item.getName(), item);
	}
	
	public Map<String, HAPManualDefinitionWrapperBrick> getItemsByBrickType(String brickType, String brickVersion){
		Map<String, HAPManualDefinitionWrapperBrick> out = null;
		Map<String, Map<String, HAPManualDefinitionWrapperBrick>> byVersion = this.getItems().get(brickType);
		if(byVersion!=null) {
			out = byVersion.get(brickVersion);
		}
		return out;
	}

	public HAPManualDefinitionWrapperBrick getItem(HAPIdBrickType brickTypeId, String name) {
		return getItem(brickTypeId.getBrickType(), brickTypeId.getVersion(), name);
	}

	
	public HAPManualDefinitionWrapperBrick getItem(String brickType, String brickVersion, String name) {
		HAPManualDefinitionWrapperBrick out = null;
		Map<String, HAPManualDefinitionWrapperBrick> items = getItemsByBrickType(brickType, brickVersion);
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
		
		Map<String, Map<String, Map<String, HAPManualDefinitionWrapperBrick>>> parentItems = parent.getItems();
		for(String brickType : parentItems.keySet()) {
			Map<String, Map<String, HAPManualDefinitionWrapperBrick>> byVersion = parentItems.get(brickType);
			for(String version : byVersion.keySet()) {
				Map<String, HAPManualDefinitionWrapperBrick> items = byVersion.get(version);
				for(String name : items.keySet()) {
					boolean override = false;
					if(mode.equals(HAPConstant.INHERITMODE_PARENT)) {
						override = true;
					}
					if(mode.equals(HAPConstant.INHERITMODE_CHILD)) {
						HAPManualDefinitionWrapperBrick childItem = this.getItem(brickType, version, name);
						if(childItem==null) {
							override = true;
						}
					} 

					if(override) {
						//if configurable, then parent override child
						HAPManualDefinitionWrapperBrick newAttachment = items.get(name).cloneBrickWrapper();
						HAPManualUtilityAttachment.setOverridenByParent(newAttachment);
						this.addItem(newAttachment);
					}
				}
			}
		}
	}
	
	private Map<String, Map<String, Map<String, HAPManualDefinitionWrapperBrick>>> getItems(){   return this.m_items;   }
	
}
