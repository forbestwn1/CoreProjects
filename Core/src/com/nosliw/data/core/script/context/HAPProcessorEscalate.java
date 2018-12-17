package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPBasicUtility;

public class HAPProcessorEscalate {

	public static void process(HAPContextGroup contextGroup, Set<String> categarys, Map<String, String> cm) {
		for(String categary : categarys) {
			HAPContext context = contextGroup.getContext(categary);

			Map<String, String> contextMapping = new LinkedHashMap<String, String>();
			contextMapping.putAll(cm);
			for(String key : context.getElementNames()) {
				if(HAPBasicUtility.isStringEmpty(contextMapping.get(key)))			contextMapping.put(key, key);
			}
			
			for(String key : context.getElementNames()) {
				process(contextGroup, key, contextMapping.get(key), categary);
			}
		}
	}
	
	
	//escalte context node to parent context group, only absolute variable
	public static void process(HAPContextGroup contextGroup, String contextEleName, String mappedPath, String categaryType) {
		HAPContextDefinitionRoot rootNode = contextGroup.getElement(categaryType, contextEleName);
		if(rootNode.isAbsolute()) {
			HAPComplexPath complexPath = new HAPComplexPath(mappedPath);
			Triple<Boolean, HAPContextDefinitionRoot, String> a = escalate(complexPath, categaryType, contextGroup.getParent(), rootNode);
			if(a.getLeft())		contextGroup.addElement(contextEleName, a.getMiddle(), a.getRight());
		}
	}
	
	private static Triple<Boolean, HAPContextDefinitionRoot, String> escalate(HAPComplexPath path, String categaryType, HAPContextGroup parentContextGroup, HAPContextDefinitionRoot original) {
		
		Triple<Boolean, HAPContextDefinitionRoot, String> out = null;
		HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(path.getFullName()), parentContextGroup, null, HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_FIRST);
		if(resolveInfo!=null) {
			//find matched one
			out = Triple.of(true, HAPUtilityContext.createRelativeContextDefinitionRoot(resolveInfo.rootNode, resolveInfo.path.getRootElementId().getCategary(), resolveInfo.path.getPath()), categaryType);
			return out;
		}
		else {
			HAPContextGroup grandParent = parentContextGroup.getParent();
			boolean isEnd = false;
			if(grandParent==null)   isEnd = true;
			else  isEnd = !HAPUtilityContext.getContextGroupPopupMode(grandParent);

			//not find
			if(isEnd){
				//only root name is valid, mappedPath with path is not valid
				if(HAPBasicUtility.isStringEmpty(path.getPath())) {
					out = Triple.of(false, original.cloneContextDefinitionRoot(), categaryType);
					parentContextGroup.addElement(path.getRootName(), out.getMiddle(), categaryType);
				}
				else HAPErrorUtility.invalid("");
				return out;
			}
			else {
				Triple<Boolean, HAPContextDefinitionRoot, String> a = escalate(path, categaryType, grandParent, original);
				if(a.getLeft()) {
					parentContextGroup.addElement(path.getRootName(), a.getMiddle(), a.getRight());
				}
				else {
				}

				out = Triple.of(true, HAPUtilityContext.createRelativeContextDefinitionRoot(parentContextGroup, a.getRight(), path.getRootName()), a.getRight());

//				parentContextGroup.addElement(path.getRootName(), a.getLeft(), a.getRight());
//				out = Pair.of(HAPUtilityContext.createRelativeContextDefinitionRoot(parentContextGroup, a.getRight(), path.getRootName()), a.getRight());
				return out;
			}
		}
	}

	/*
	private static Object[] escalate1(String name, String categaryType, HAPContextGroup parentContextGroup, HAPContextDefinitionRoot original) {
		
		Object[] out = new Object[2];
		HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(name), parentContextGroup, null, HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_FIRST);
		if(resolveInfo!=null) {
			//find matched one
			out[0] = HAPUtilityContext.createInheritedElement(resolveInfo.rootNode, resolveInfo.path.getRootElementId().getCategary(), resolveInfo.path.getRootElementId().getName());
			out[1] = categaryType;
//					resolveInfo.path.getRootElementId().getCategary();
			return out;
		}
		else {
			HAPContextGroup grandParent = parentContextGroup.getParent();
			boolean isEnd = false;
			if(grandParent==null)   isEnd = true;
			else  isEnd = !HAPUtilityContext.getContextGroupPopupMode(grandParent);

			//not find
			if(isEnd){
				HAPContextDefinitionRoot newRootNode = original.cloneContextDefinitionRoot();
				parentContextGroup.addElement(name, newRootNode, categaryType);
				return out;
			}
			else {
				Object[] a = escalate(name, categaryType, grandParent, original);
				
				String categary;
				HAPContextGroup group;
				if(a[0]!=null) {
					parentContextGroup.addElement(name, (HAPContextDefinitionRoot)a[0], (String)a[1]);
					group = parentContextGroup;
					categary = (String)a[1];
				}
				else {
					parentContextGroup.addElement(name, HAPUtilityContext.createInheritedElement(grandParent, categaryType, name), categaryType);
					group = parentContextGroup;
					categary = categaryType;
				}
				
				out[0] = HAPUtilityContext.createInheritedElement(group, categary, name);
				out[1] = categary;
				return out;
			}
		}
	}
*/
}
