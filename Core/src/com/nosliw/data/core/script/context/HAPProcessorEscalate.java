package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

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
	public static void process(HAPContextGroup contextGroup, String contextEleName, String mappedName, String categaryType) {
		HAPContextDefinitionRoot rootNode = contextGroup.getElement(categaryType, contextEleName);
		if(rootNode.isAbsolute()) {
			Pair<HAPContextDefinitionRoot, String> a = escalate(mappedName, categaryType, contextGroup.getParent(), rootNode);
			contextGroup.addElement(contextEleName, a.getLeft(), a.getRight());
		}
	}
	
	private static Pair<HAPContextDefinitionRoot, String> escalate(String name, String categaryType, HAPContextGroup parentContextGroup, HAPContextDefinitionRoot original) {
		
		Pair<HAPContextDefinitionRoot, String> out = null;
		HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(name), parentContextGroup, null, HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_FIRST);
		if(resolveInfo!=null) {
			//find matched one
			out = Pair.of(HAPUtilityContext.createInheritedElement(resolveInfo.rootNode, resolveInfo.path.getRootElementId().getCategary(), resolveInfo.path.getRootElementId().getName()), categaryType);
			return out;
		}
		else {
			HAPContextGroup grandParent = parentContextGroup.getParent();
			boolean isEnd = false;
			if(grandParent==null)   isEnd = true;
			else  isEnd = !HAPUtilityContext.getContextGroupPopupMode(grandParent);

			//not find
			if(isEnd){
				out = Pair.of(original.cloneContextDefinitionRoot(), categaryType);
				return out;
			}
			else {
				Pair<HAPContextDefinitionRoot, String> a = escalate(name, categaryType, grandParent, original);
				
				parentContextGroup.addElement(name, a.getLeft(), a.getRight());
				out = Pair.of(HAPUtilityContext.createInheritedElement(parentContextGroup, a.getRight(), name), a.getRight());
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
