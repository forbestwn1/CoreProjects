package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.a.HAPManualWithBrick;
import com.nosliw.core.application.division.manual.core.b.HAPHandlerDownwardImpTreeNode;
import com.nosliw.core.application.division.manual.core.b.HAPManualUtilityBrickTraverse;
import com.nosliw.core.application.division.manual.core.b.HAPTreeNodeBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperBrickRoot;

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
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueWrapper()).getBrickTypeId();
						process = HAPManualUtilityProcess.isAttributeAutoProcess(attrDef, processContext.getBrickManager());
					}
					if(process) {
						HAPManualPluginProcessorBlockComplex plugin = (HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(entityTypeId);
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
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueWrapper()).getBrickTypeId();
						process = HAPManualUtilityProcess.isAttributeAutoProcess(attrDef, processContext.getBrickManager());
					}
					if(process) {
						HAPManualPluginProcessorBlockComplex plugin = (HAPManualPluginProcessorBlockComplex)manualBrickMan.getBlockProcessPlugin(entityTypeId);
						plugin.postProcessBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
					}
				}

			},
			processContext);
	}

}
