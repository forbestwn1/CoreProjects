package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPBasicUtility;

public class HAPProcessorEscalate {

	public static void process(HAPContextGroup contextGroup, Set<String> categarys, Map<String, String> cm, Set<String> inheritanceExcludedInfo) {
		for(String categary : categarys) {
			HAPContext context = contextGroup.getContext(categary);

			Map<String, String> contextMapping = new LinkedHashMap<String, String>();
			contextMapping.putAll(cm);
			for(String key : context.getElementNames()) {
				if(HAPBasicUtility.isStringEmpty(contextMapping.get(key)))			contextMapping.put(key, key);
			}
			
			for(String key : context.getElementNames()) {
				process(contextGroup, key, contextMapping.get(key), categary, inheritanceExcludedInfo);
			}
		}
	}
	
	
	//escalte context node to parent context group, only absolute variable
	public static void process(HAPContextGroup contextGroup, String contextEleName, String mappedPath, String categaryType, Set<String> inheritanceExcludedInfo) {
		HAPContextDefinitionRoot rootNode = contextGroup.getElement(categaryType, contextEleName);
		if(rootNode.isAbsolute()) {
			HAPComplexPath complexPath = new HAPComplexPath(mappedPath);
			Triple<Boolean, HAPContextDefinitionRoot, String> a = escalate(complexPath, categaryType, contextGroup.getParent(), rootNode, inheritanceExcludedInfo);
			if(a.getLeft())		contextGroup.addElement(contextEleName, a.getMiddle(), a.getRight());
			else {
				contextGroup.addElement(contextEleName, HAPUtilityContext.createRelativeContextDefinitionRoot(a.getMiddle(), a.getRight(), complexPath.getRootName(), inheritanceExcludedInfo), a.getRight());
			}
		}
	}
	
	private static Triple<Boolean, HAPContextDefinitionRoot, String> escalate(HAPComplexPath path, String categaryType, HAPContextGroup parentContextGroup, HAPContextDefinitionRoot original, Set<String> inheritanceExcludedInfo) {
		
		Triple<Boolean, HAPContextDefinitionRoot, String> out = null;
		HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(path.getFullName()), parentContextGroup, null, HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_FIRST);
		if(resolveInfo!=null) {
			//find matched one
			out = Triple.of(true, HAPUtilityContext.createRelativeContextDefinitionRoot(resolveInfo.rootNode, resolveInfo.path.getRootElementId().getCategary(), resolveInfo.path.getPath(), inheritanceExcludedInfo), categaryType);
			return out;
		}
		else {
			HAPContextGroup grandParent = parentContextGroup.getParent();
			boolean isEnd = false;
			if(grandParent==null)   isEnd = true;
			else  isEnd = !HAPUtilityContext.getContextGroupPopupMode(parentContextGroup);

			//not find
			if(isEnd){
				//only root name is valid, mappedPath with path is not valid
				if(HAPBasicUtility.isStringEmpty(path.getPath())) {
					HAPContextDefinitionRoot rootNode = original.cloneContextDefinitionRoot();
					parentContextGroup.addElement(path.getRootName(), rootNode, categaryType);
					out = Triple.of(false, rootNode, categaryType);
				}
				else HAPErrorUtility.invalid("");
				return out;
			}
			else {
				Triple<Boolean, HAPContextDefinitionRoot, String> a = escalate(path, categaryType, grandParent, original, inheritanceExcludedInfo);
				HAPContextDefinitionRoot b;
				if(a.getLeft()) {
					b = a.getMiddle();
					parentContextGroup.addElement(path.getRootName(), b, a.getRight());
				}
				else {
					b = HAPUtilityContext.createRelativeContextDefinitionRoot(a.getMiddle(), a.getRight(), path.getRootName(), inheritanceExcludedInfo);
					parentContextGroup.addElement(path.getRootName(), b, a.getRight());
				}

				out = Triple.of(false, b, a.getRight());
				return out;
			}
		}
	}
}
