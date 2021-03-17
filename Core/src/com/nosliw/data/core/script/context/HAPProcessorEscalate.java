package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPProcessorEscalate {

	public static void process(HAPContextGroup contextGroup, Set<String> categarys, Map<String, String> cm, Set<String> inheritanceExcludedInfo) {
		for(String categary : categarys) {
			HAPContext context = contextGroup.getContext(categary);

			Map<String, String> contextMapping = new LinkedHashMap<String, String>();
			contextMapping.putAll(cm);
			for(String eleName : context.getElementNames()) {
				if(HAPBasicUtility.isStringEmpty(contextMapping.get(eleName)))			contextMapping.put(eleName, eleName);
			}
			
			for(String eleName : context.getElementNames()) {
				process(contextGroup, categary, eleName, contextMapping.get(eleName), inheritanceExcludedInfo);
			}
		}
	}
	
	//escalte context node to parent context group, only absolute variable
	public static void process(HAPContextGroup sourceContextGroup, String sourceCategaryType, String contextEleName, String escalateTargetPath, Set<String> inheritanceExcludedInfo) {
		HAPContextDefinitionRoot sourceRootNode = sourceContextGroup.getElement(sourceCategaryType, contextEleName);
		if(sourceRootNode.isAbsolute()) {
			HAPComplexPath complexPath = new HAPComplexPath(escalateTargetPath);
			Pair<Boolean, HAPContextDefinitionRoot> a = escalate(sourceRootNode, sourceCategaryType, sourceContextGroup.getParent(), complexPath, inheritanceExcludedInfo);
			
			HAPContextDefinitionRoot b = getEscalateStepRootNode(a, sourceCategaryType, complexPath, inheritanceExcludedInfo);
			sourceContextGroup.addElement(contextEleName, b, sourceCategaryType);
		}
	}
	
	//out.left: true--escalate to existing root node    false--escalate to new root node
	private static Pair<Boolean, HAPContextDefinitionRoot> escalate(HAPContextDefinitionRoot original, String categaryType, HAPContextGroup parentContextGroup, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		
		Pair<Boolean, HAPContextDefinitionRoot> out = null;
		HAPInfoContextElementReferenceResolve resolveInfo = HAPUtilityContext.resolveReferencedContextElement(new HAPContextPath(path.getFullName()), parentContextGroup, null, HAPConstant.RESOLVEPARENTMODE_FIRST);
		if(HAPUtilityContext.isLogicallySolved(resolveInfo)) {
			//find matched one
			out = Pair.of(true, HAPUtilityContext.createRelativeContextDefinitionRoot(resolveInfo.rootNode, resolveInfo.path.getRootElementId().getCategary(), resolveInfo.path.getPath(), inheritanceExcludedInfo));
		}
		else {
			//not find
			HAPContextGroup grandParent = parentContextGroup.getParent();
			boolean isEnd = false;
			if(grandParent==null)   isEnd = true;
			else  isEnd = !HAPUtilityContext.getContextGroupPopupMode(parentContextGroup.getInfo());

			if(isEnd){
				//at the end of escalate
				//only root name is valid, mappedPath with path is not valid
				if(HAPBasicUtility.isStringEmpty(path.getPath())) {
					//clone original root node to parent context
					HAPContextDefinitionRoot rootNode = original.cloneContextDefinitionRoot();
					parentContextGroup.addElement(path.getRootName(), rootNode, categaryType);
					out = Pair.of(false, rootNode);
				}
				else HAPErrorUtility.invalid("");
			}
			else {
				//keep escalate to grand parent
				Pair<Boolean, HAPContextDefinitionRoot> a = escalate(original, categaryType, grandParent, path, inheritanceExcludedInfo);
				HAPContextDefinitionRoot b = getEscalateStepRootNode(a, categaryType, path, inheritanceExcludedInfo);
				parentContextGroup.addElement(path.getRootName(), b, categaryType);
				out = Pair.of(false, b);
			}
		}
		return out;
	}

	
	private static HAPContextDefinitionRoot getEscalateStepRootNode(Pair<Boolean, HAPContextDefinitionRoot> stepResult, String categaryType, HAPComplexPath path, Set<String> inheritanceExcludedInfo) {
		HAPContextDefinitionRoot out;
		if(stepResult.getLeft()) {
			out = stepResult.getRight();
		}
		else {
			out = HAPUtilityContext.createRelativeContextDefinitionRoot(stepResult.getRight(), categaryType, path.getRootName(), inheritanceExcludedInfo);
			//kkk should set original to relative node
		}
		return out;
	}

}
