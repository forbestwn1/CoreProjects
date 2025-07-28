package com.nosliw.data.core.structure.temp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPProcessorEscalate {

	public static void process(HAPValueStructureDefinitionGroup contextGroup, Set<String> categarys, Map<String, String> cm, Set<String> inheritanceExcludedInfo) {
		for(String categary : categarys) {
			HAPValueStructureDefinitionFlat context = contextGroup.getFlat(categary);

			Map<String, String> contextMapping = new LinkedHashMap<String, String>();
			contextMapping.putAll(cm);
			for(String eleName : context.getRootNames()) {
				if(HAPUtilityBasic.isStringEmpty(contextMapping.get(eleName)))			contextMapping.put(eleName, eleName);
			}
			
			for(String eleName : context.getRootNames()) {
				process(contextGroup, categary, eleName, contextMapping.get(eleName), inheritanceExcludedInfo);
			}
		}
	}
	
	//escalte context node to parent context group, only absolute variable
	public static void process(HAPValueStructureDefinitionGroup sourceContextGroup, String sourceCategaryType, String contextEleName, String escalateTargetPath, Set<String> inheritanceExcludedInfo) {
		HAPRootStructure sourceRootNode = sourceContextGroup.getElement(sourceCategaryType, contextEleName);
		if(sourceRootNode.isAbsolute()) {
			HAPComplexPath complexPath = new HAPComplexPath(escalateTargetPath);
			Pair<Boolean, HAPRootStructure> a = escalate(sourceRootNode, sourceCategaryType, sourceContextGroup.getParentValueContextName(), complexPath, inheritanceExcludedInfo);
			
			HAPRootStructure b = getEscalateStepRootNode(a, sourceCategaryType, complexPath, inheritanceExcludedInfo);
			sourceContextGroup.addRoot(contextEleName, b, sourceCategaryType);
		}
	}
	
	//out.left: true--escalate to existing root node    false--escalate to new root node
	private static Pair<Boolean, HAPRootStructure> escalate(HAPRootStructure original, String categaryType, HAPValueStructureDefinitionGroup parentContextGroup, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		
		Pair<Boolean, HAPRootStructure> out = null;
		HAPResultReferenceResolve resolveInfo = HAPUtilityContext.analyzeElementReference(new HAPReferenceElementInStructure(path.getFullName()), parentContextGroup, null, HAPConstant.RESOLVEPARENTMODE_FIRST);
		if(HAPUtilityStructure.isLogicallySolved(resolveInfo)) {
			//find matched one
			out = Pair.of(true, HAPUtilityContext.createRootWithRelativeElement(resolveInfo.referredRoot, resolveInfo.path.getRootReference().getCategary(), resolveInfo.path.getPathStr(), inheritanceExcludedInfo));
		}
		else {
			//not find
			HAPValueStructureDefinitionGroup grandParent = parentContextGroup.getParentValueContextName();
			boolean isEnd = false;
			if(grandParent==null)   isEnd = true;
			else  isEnd = !HAPUtilityContext.getContextGroupPopupMode(parentContextGroup.getInfo());

			if(isEnd){
				//at the end of escalate
				//only root name is valid, mappedPath with path is not valid
				if(HAPUtilityBasic.isStringEmpty(path.getPathStr())) {
					//clone original root node to parent context
					HAPRootStructure rootNode = original.cloneRoot();
					parentContextGroup.addRoot(path.getRoot(), rootNode, categaryType);
					out = Pair.of(false, rootNode);
				}
				else HAPErrorUtility.invalid("");
			}
			else {
				//keep escalate to grand parent
				Pair<Boolean, HAPRootStructure> a = escalate(original, categaryType, grandParent, path, inheritanceExcludedInfo);
				HAPRootStructure b = getEscalateStepRootNode(a, categaryType, path, inheritanceExcludedInfo);
				parentContextGroup.addRoot(path.getRoot(), b, categaryType);
				out = Pair.of(false, b);
			}
		}
		return out;
	}

	
	private static HAPRootStructure getEscalateStepRootNode(Pair<Boolean, HAPRootStructure> stepResult, String categaryType, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		HAPRootStructure out;
		if(stepResult.getLeft()) {
			out = stepResult.getRight();
		}
		else {
			out = HAPUtilityStructure.createRootWithRelativeElement(stepResult.getRight(), categaryType, path.getRoot(), inheritanceExcludedInfo);
			//kkk should set original to relative node
		}
		return out;
	}

}
