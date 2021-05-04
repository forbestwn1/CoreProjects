package com.nosliw.data.core.structure;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPProcessorEscalate {

	public static void process(HAPStructureValueDefinitionGroup contextGroup, Set<String> categarys, Map<String, String> cm, Set<String> inheritanceExcludedInfo) {
		for(String categary : categarys) {
			HAPStructureValueDefinitionFlat context = contextGroup.getFlat(categary);

			Map<String, String> contextMapping = new LinkedHashMap<String, String>();
			contextMapping.putAll(cm);
			for(String eleName : context.getRootNames()) {
				if(HAPBasicUtility.isStringEmpty(contextMapping.get(eleName)))			contextMapping.put(eleName, eleName);
			}
			
			for(String eleName : context.getRootNames()) {
				process(contextGroup, categary, eleName, contextMapping.get(eleName), inheritanceExcludedInfo);
			}
		}
	}
	
	//escalte context node to parent context group, only absolute variable
	public static void process(HAPStructureValueDefinitionGroup sourceContextGroup, String sourceCategaryType, String contextEleName, String escalateTargetPath, Set<String> inheritanceExcludedInfo) {
		HAPRoot sourceRootNode = sourceContextGroup.getElement(sourceCategaryType, contextEleName);
		if(sourceRootNode.isAbsolute()) {
			HAPComplexPath complexPath = new HAPComplexPath(escalateTargetPath);
			Pair<Boolean, HAPRoot> a = escalate(sourceRootNode, sourceCategaryType, sourceContextGroup.getParent(), complexPath, inheritanceExcludedInfo);
			
			HAPRoot b = getEscalateStepRootNode(a, sourceCategaryType, complexPath, inheritanceExcludedInfo);
			sourceContextGroup.addRoot(contextEleName, b, sourceCategaryType);
		}
	}
	
	//out.left: true--escalate to existing root node    false--escalate to new root node
	private static Pair<Boolean, HAPRoot> escalate(HAPRoot original, String categaryType, HAPStructureValueDefinitionGroup parentContextGroup, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		
		Pair<Boolean, HAPRoot> out = null;
		HAPInfoReferenceResolve resolveInfo = HAPUtilityContext.resolveElementReference(new HAPReferenceElement(path.getFullName()), parentContextGroup, null, HAPConstant.RESOLVEPARENTMODE_FIRST);
		if(HAPUtilityContext.isLogicallySolved(resolveInfo)) {
			//find matched one
			out = Pair.of(true, HAPUtilityContext.createRootWithRelativeElement(resolveInfo.referredRoot, resolveInfo.path.getRootReference().getCategary(), resolveInfo.path.getPathStr(), inheritanceExcludedInfo));
		}
		else {
			//not find
			HAPStructureValueDefinitionGroup grandParent = parentContextGroup.getParent();
			boolean isEnd = false;
			if(grandParent==null)   isEnd = true;
			else  isEnd = !HAPUtilityContext.getContextGroupPopupMode(parentContextGroup.getInfo());

			if(isEnd){
				//at the end of escalate
				//only root name is valid, mappedPath with path is not valid
				if(HAPBasicUtility.isStringEmpty(path.getPathStr())) {
					//clone original root node to parent context
					HAPRoot rootNode = original.cloneRoot();
					parentContextGroup.addRoot(path.getRootName(), rootNode, categaryType);
					out = Pair.of(false, rootNode);
				}
				else HAPErrorUtility.invalid("");
			}
			else {
				//keep escalate to grand parent
				Pair<Boolean, HAPRoot> a = escalate(original, categaryType, grandParent, path, inheritanceExcludedInfo);
				HAPRoot b = getEscalateStepRootNode(a, categaryType, path, inheritanceExcludedInfo);
				parentContextGroup.addRoot(path.getRootName(), b, categaryType);
				out = Pair.of(false, b);
			}
		}
		return out;
	}

	
	private static HAPRoot getEscalateStepRootNode(Pair<Boolean, HAPRoot> stepResult, String categaryType, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		HAPRoot out;
		if(stepResult.getLeft()) {
			out = stepResult.getRight();
		}
		else {
			out = HAPUtilityContext.createRootWithRelativeElement(stepResult.getRight(), categaryType, path.getRootName(), inheritanceExcludedInfo);
			//kkk should set original to relative node
		}
		return out;
	}

}
