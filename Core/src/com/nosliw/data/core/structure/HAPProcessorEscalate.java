package com.nosliw.data.core.structure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPProcessorEscalate {

	public static void process(HAPValueStructure contextGroup, Set<String> categarys, Map<String, String> cm, Set<String> inheritanceExcludedInfo) {

		//normalize mapping first
		Map<String, String> contextMapping = new LinkedHashMap<String, String>();
		contextMapping.putAll(cm);

		for(HAPRoot root : contextGroup.getAllRoots()) {
			if(contextGroup.isExternalVisible(root.getLocalId())) {
				//only external visible root
				List<HAPInfoAlias> rootAliases = contextGroup.discoverRootAliasById(root.getLocalId());
				String targetPath = null;
				for(HAPInfoAlias aliasInfo : rootAliases) {
					targetPath = contextMapping.get(aliasInfo.getName());
					if(!HAPBasicUtility.isStringEmpty(targetPath)) break;
				}
				if(HAPBasicUtility.isStringEmpty(targetPath)) {
					targetPath = rootAliases.get(0).getName();
				}

				process(contextGroup, root.getLocalId(), targetPath, inheritanceExcludedInfo);
			}
		}
	}
	
	//escalte context node to parent context group, only absolute variable
	public static void process(HAPValueStructure sourceContextGroup, String sourceRootId, String escalateTargetPath, Set<String> inheritanceExcludedInfo) {
		HAPRoot sourceRootNode = sourceContextGroup.getRoot(sourceRootId);
		if(sourceRootNode.isAbsolute()) {
			HAPComplexPath complexPath = new HAPComplexPath(escalateTargetPath);
			Pair<Boolean, HAPRoot> a = escalate(sourceContextGroup, sourceRootId, sourceContextGroup.getParent(), complexPath, inheritanceExcludedInfo);
			
			HAPRoot b = getEscalateStepRootNode(a, complexPath, inheritanceExcludedInfo);
			sourceContextGroup.addRoot(sourceContextGroup.getRootReferenceById(sourceRootId), b);
		}
	}
	
	//out.left: true--escalate to existing root node    false--escalate to new root node
	private static Pair<Boolean, HAPRoot> escalate(HAPValueStructure originalStrucutre, String originalRootId, HAPValueStructure parentContextGroup, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		
		Pair<Boolean, HAPRoot> out = null;
		HAPInfoReferenceResolve resolveInfo = HAPUtilityStructure.analyzeElementReference(new HAPReferenceElement(path.getFullName()), parentContextGroup, HAPConstant.RESOLVEPARENTMODE_FIRST, null);
		if(HAPUtilityStructure.isLogicallySolved(resolveInfo)) {
			//find matched one
			out = Pair.of(true, HAPUtilityStructure.createRootWithRelativeElement(resolveInfo.referredRoot, null, resolveInfo.path.getPathStr(), inheritanceExcludedInfo));
		}
		else {
			//not find
			HAPValueStructure grandParent = parentContextGroup.getParent();
			boolean isEnd = false;
			if(grandParent==null)   isEnd = true;
			else  isEnd = !HAPUtilityValueStructure.getContextGroupPopupMode(parentContextGroup.getInfo());

			if(isEnd){
				//at the end of escalate
				//only root name is valid, mappedPath with path is not valid
				if(HAPBasicUtility.isStringEmpty(path.getPathStr())) {
					//clone original root node to parent context
					HAPRoot newRoot = HAPUtilityStructure.addRoot(parentContextGroup, path.getRootName(), originalStrucutre.getRoot(originalRootId).cloneRoot());
					out = Pair.of(false, newRoot);
				}
				else HAPErrorUtility.invalid("");
			}
			else {
				//keep escalate to grand parent
				Pair<Boolean, HAPRoot> a = escalate(originalStrucutre, originalRootId, grandParent, path, inheritanceExcludedInfo);
				HAPRoot b = getEscalateStepRootNode(a, path, inheritanceExcludedInfo);
				HAPRoot newRoot = HAPUtilityStructure.addRoot(parentContextGroup, path.getRootName(), b);
				out = Pair.of(false, newRoot);
			}
		}
		return out;
	}

	
	private static HAPRoot getEscalateStepRootNode(Pair<Boolean, HAPRoot> stepResult, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		HAPRoot out;
		if(stepResult.getLeft()) {
			out = stepResult.getRight();
		}
		else {
			out = HAPUtilityStructure.createRootWithRelativeElement(stepResult.getRight(), null, path.getRootName(), inheritanceExcludedInfo);
			//kkk should set original to relative node
		}
		return out;
	}

}
