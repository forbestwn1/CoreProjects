package com.nosliw.core.xxx.application.common.structure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.valueport.HAPResultReferenceResolve;
import com.nosliw.core.xxx.application.valueport.HAPUtilityStructureElementReference;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.valuestructure1.HAPTreeNodeValueStructure;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;

public class HAPProcessorEscalate {

	public static void process(HAPTreeNodeValueStructure sourceNode, Set<String> categarys, Map<String, String> cm, Set<String> inheritanceExcludedInfo) {
		
		HAPValueStructureInValuePort11111 structure = sourceNode.getValueStructureWrapper().getValueStructureBlock();

		//normalize mapping first
		Map<String, String> contextMapping = new LinkedHashMap<String, String>();
		contextMapping.putAll(cm);

		for(HAPRootStructure root : structure.getAllRoots()) {
			if(structure.isExternalVisible(root.getLocalId())) {
				//only external visible root
				List<HAPInfoAlias> rootAliases = structure.discoverRootAliasById(root.getLocalId());
				String targetPath = null;
				for(HAPInfoAlias aliasInfo : rootAliases) {
					targetPath = contextMapping.get(aliasInfo.getName());
					if(!HAPUtilityBasic.isStringEmpty(targetPath)) break;
				}
				if(HAPUtilityBasic.isStringEmpty(targetPath)) {
					targetPath = rootAliases.get(0).getName();
				}

				process(sourceNode, root.getLocalId(), targetPath, inheritanceExcludedInfo);
			}
		}
	}
	
	//escalte context node to parent context group, only absolute variable
	public static void process(HAPTreeNodeValueStructure sourceNode, String sourceRootId, String escalateTargetPath, Set<String> inheritanceExcludedInfo) {
		HAPValueStructureInValuePort11111 sourceStructure = sourceNode.getValueStructureWrapper().getValueStructureBlock();
		HAPRootStructure sourceRootNode = sourceStructure.getRoot(sourceRootId);
		if(sourceRootNode.isAbsolute()) {
			HAPComplexPath complexPath = new HAPComplexPath(escalateTargetPath);
			Pair<Boolean, HAPRootStructure> a = escalate(sourceNode, sourceRootId, sourceNode.getParent(), complexPath, inheritanceExcludedInfo);
			
			HAPRootStructure b = getEscalateStepRootNode(a, complexPath, inheritanceExcludedInfo);
			sourceStructure.addRoot(sourceStructure.getRootReferenceById(sourceRootId), b);
		}
	}
	
	//out.left: true--escalate to existing root node    false--escalate to new root node
	private static Pair<Boolean, HAPRootStructure> escalate(HAPTreeNodeValueStructure originalNode, String originalRootId, HAPTreeNodeValueStructure parentNode, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		
		HAPValueStructureInValuePort11111 originalStrucutre = originalNode.getValueStructureWrapper().getValueStructureBlock();
		HAPValueStructureInValuePort11111 parentStructure = parentNode.getValueStructureWrapper().getValueStructureBlock();
		
		Pair<Boolean, HAPRootStructure> out = null;
		HAPResultReferenceResolve resolveInfo = HAPUtilityStructureElementReference.analyzeElementReference(new HAPReferenceElementInStructure(path.getFullName()), parentStructure, HAPConstant.RESOLVEPARENTMODE_FIRST, null);
		if(HAPUtilityStructureElementReference.isLogicallySolved(resolveInfo)) {
			//find matched one
			out = Pair.of(true, HAPUtilityStructure.createRootWithRelativeElement(resolveInfo.referredRoot, null, resolveInfo.path.getPathStr(), inheritanceExcludedInfo));
		}
		else {
			//not find
			HAPTreeNodeValueStructure grandParentNode = parentNode.getParent();
			boolean isEnd = false;
			if(grandParentNode==null)   isEnd = true;
			else  isEnd = !HAPUtilityValueStructure.getContextGroupPopupMode(parentStructure.getInfo());

			if(isEnd){
				//at the end of escalate
				//only root name is valid, mappedPath with path is not valid
				if(HAPUtilityBasic.isStringEmpty(path.getPathStr())) {
					//clone original root node to parent context
					HAPRootStructure newRoot = HAPUtilityStructure.addRoot(parentStructure, path.getRoot(), originalStrucutre.getRoot(originalRootId).cloneRoot());
					out = Pair.of(false, newRoot);
				}
				else HAPErrorUtility.invalid("");
			}
			else {
				//keep escalate to grand parent
				Pair<Boolean, HAPRootStructure> a = escalate(originalNode, originalRootId, grandParentNode, path, inheritanceExcludedInfo);
				HAPRootStructure b = getEscalateStepRootNode(a, path, inheritanceExcludedInfo);
				HAPRootStructure newRoot = HAPUtilityStructure.addRoot(parentStructure, path.getRoot(), b);
				out = Pair.of(false, newRoot);
			}
		}
		return out;
	}

	
	private static HAPRootStructure getEscalateStepRootNode(Pair<Boolean, HAPRootStructure> stepResult, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		HAPRootStructure out;
		if(stepResult.getLeft()) {
			out = stepResult.getRight();
		}
		else {
			out = HAPUtilityStructure.createRootWithRelativeElement(stepResult.getRight(), null, path.getRoot(), inheritanceExcludedInfo);
			//kkk should set original to relative node
		}
		return out;
	}

}
