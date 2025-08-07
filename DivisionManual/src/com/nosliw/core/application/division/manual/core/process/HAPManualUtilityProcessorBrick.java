package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPTreeNodeBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWithBrick;

public class HAPManualUtilityProcessorBrick {

	public static void process(HAPManualContextProcessBrick processContext) {
		HAPManualManagerBrick manualBrickMan = processContext.getManualBrickManager();
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(
				processContext, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualDefinitionUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					HAPManualBrick block = null;
					if(treeNodeDef instanceof HAPManualDefinitionWrapperBrickRoot) {
						entityTypeId = ((HAPManualDefinitionWrapperBrickRoot)treeNodeDef).getBrickTypeId();
					}
					else {
						HAPManualDefinitionAttributeInBrick attrDef = (HAPManualDefinitionAttributeInBrick)treeNodeDef;
						entityTypeId = ((HAPManualDefinitionWithBrick)attrDef.getValueWrapper()).getBrickTypeId();
						process = HAPManualUtilityProcess.isAttributeAutoProcess(attrDef, processContext.getBrickManager());
					}
					if(process) {
						HAPManualPluginProcessorBlockImp plugin = (HAPManualPluginProcessorBlockImp)manualBrickMan.getBlockProcessPlugin(entityTypeId);
						plugin.processBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
						return true;
					}
					else {
						return false;
					}
				}

				@Override
				public void postProcessTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualDefinitionUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					HAPManualBrick block = null;
					if(treeNodeDef instanceof HAPManualDefinitionWrapperBrickRoot) {
						entityTypeId = ((HAPManualDefinitionWrapperBrickRoot)treeNodeDef).getBrickTypeId();
					}
					else {
						HAPManualDefinitionAttributeInBrick attrDef = (HAPManualDefinitionAttributeInBrick)treeNodeDef;
						entityTypeId = ((HAPManualDefinitionWithBrick)attrDef.getValueWrapper()).getBrickTypeId();
						process = HAPManualUtilityProcess.isAttributeAutoProcess(attrDef, processContext.getBrickManager());
					}
					if(process) {
						HAPManualPluginProcessorBlockImp plugin = (HAPManualPluginProcessorBlockImp)manualBrickMan.getBlockProcessPlugin(entityTypeId);
						plugin.postProcessBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
					}
				}

			},
			processContext);
	}

}
