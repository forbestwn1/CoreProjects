package com.nosliw.core.application.division.manual.core.process;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.application.division.manual.core.HAPManualAdapter;
import com.nosliw.core.application.division.manual.core.HAPManualAttributeInBrick;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPTreeNodeBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAdapter;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueBrick;

public class HAPManualUtilityProcessorAdapter {

	public static void processAdapterInAttribute(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPUtilityBrickTraverse.traverseTree(
				processContext.getCurrentBundle(),
				processContext.getRootBrickName(),
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualDefinitionUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					if(treeNodeDef instanceof HAPManualDefinitionAttributeInBrick) {
						HAPManualDefinitionAttributeInBrick attrDef = (HAPManualDefinitionAttributeInBrick)treeNodeDef;
						HAPManualAttributeInBrick attrExe = (HAPManualAttributeInBrick)treeNode;
						
						if(HAPManualDefinitionUtilityBrick.isAdapterAutoProcess(attrDef, processContext.getBrickManager())) {
							Set<HAPManualDefinitionAdapter> adapterDefs = attrDef.getAdapters();
							Map<String, HAPManualAdapter> adapterExeByName = new LinkedHashMap<String, HAPManualAdapter>();
							for(HAPManualAdapter adapter : attrExe.getManualAdapters()) {
								adapterExeByName.put(adapter.getName(), adapter);
							}
							
							for(HAPManualDefinitionAdapter adapterDef : adapterDefs) {
								HAPManualAdapter adapterExe = adapterExeByName.get(adapterDef.getName()); 
								HAPManualDefinitionWrapperValueBrick adapterWrapperDef = (HAPManualDefinitionWrapperValueBrick)adapterDef.getValueWrapper();
								HAPManualPluginProcessorAdapter adapterProcessPlugin = manualBrickMan.getAdapterProcessPlugin(adapterWrapperDef.getBrick().getBrickTypeId());
								
								HAPManualBrick brick = (HAPManualBrick)((HAPWrapperValueOfBrick)adapterExe.getValueWrapper()).getBrick();
								adapterProcessPlugin.process(brick, adapterWrapperDef.getBrick(), new HAPManualContextProcessAdapter(processContext.getCurrentBundle(), processContext.getRootBrickName(), treeNode.getTreeNodeInfo().getPathFromRoot(), manualBrickMan, processContext.getBrickManager(), processContext.getDataTypeHelper(), processContext.getResourceManager(), processContext.getRuntimeInfo()));
							}
							
							return true;
						}
						else {
							return false;
						}
					}
					else {
						return true;
					}
				}
			},
			processContext.getBrickManager(),
			processContext);
	}
	
}
