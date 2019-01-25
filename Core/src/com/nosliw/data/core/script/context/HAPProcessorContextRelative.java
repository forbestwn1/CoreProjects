package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPMatchers;

public class HAPProcessorContextRelative {

	public static HAPContextGroup process(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPContextGroup out = contextGroup.cloneContextGroup();
		for(String categary : HAPContextGroup.getAllContextTypes()){
			Map<String, HAPContextDefinitionRoot> eles = out.getElements(categary);
			for(String eleName : eles.keySet()) {
				HAPContextDefinitionRoot contextRoot = eles.get(eleName);
				contextRoot.setDefinition(processRelativeInContextDefinitionElement(contextRoot.getDefinition(), parentContextGroup, configure, contextProcessRequirement));
			}
		}
		return out;
	}

	public static HAPContext process(HAPContext context, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPContext out = context.cloneContext();
		Map<String, HAPContextDefinitionRoot> eles = out.getElements();
		for(String eleName : eles.keySet()) {
			HAPContextDefinitionRoot contextRoot = eles.get(eleName);
			contextRoot.setDefinition(processRelativeInContextDefinitionElement(contextRoot.getDefinition(), parentContextGroup, configure, contextProcessRequirement));
		}
		return out;
	}
	
	private static HAPContextDefinitionElement processRelativeInContextDefinitionElement(HAPContextDefinitionElement defContextElement, HAPContextGroup parentContext, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPContextDefinitionElement out = defContextElement;
		switch(defContextElement.getType()) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPContextDefinitionLeafRelative relativeContextElement = (HAPContextDefinitionLeafRelative)defContextElement;
			if(!relativeContextElement.isProcessed()){
				List<String> categaryes = new ArrayList<String>();
				if(relativeContextElement.getParentCategary()!=null) categaryes.add(relativeContextElement.getParentCategary());
				else if(configure.parentCategary==null)   categaryes.addAll(Arrays.asList(HAPContextGroup.getVisibleContextTypes()));
				else   categaryes.addAll(Arrays.asList(configure.parentCategary));
				out = processRelativeContextDefinitionElement(relativeContextElement, parentContext, categaryes.toArray(new String[0]), configure.relativeResolveMode, contextProcessRequirement);
			}
			break;
		case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
			Map<String, HAPContextDefinitionElement> processedChildren = new LinkedHashMap<String, HAPContextDefinitionElement>();
			HAPContextDefinitionNode nodeContextElement = (HAPContextDefinitionNode)defContextElement;
			for(String childName : nodeContextElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInContextDefinitionElement(nodeContextElement.getChild(childName), parentContext, configure, contextProcessRequirement));
			}
			break;
		}
		return out;
	}
	
	private static HAPContextDefinitionElement processRelativeContextDefinitionElement(HAPContextDefinitionLeafRelative defContextElementRelative, HAPContextGroup parentContext, String[] categaryes, String mode, HAPRequirementContextProcessor contextProcessRequirement){
		HAPContextDefinitionElement out = defContextElementRelative;
		
		HAPContextPath path = defContextElementRelative.getPath(); 
		if("schoolList___public".equals(path.getRootElementId().getFullName())&&"element".equals(path.getSubPath())) {
			int kkkk = 5555;
			kkkk++;
		}

		if("schoolList___public".equals(path.getRootElementId().getFullName())) {
			int kkkk = 5555;
			kkkk++;
		}

		if("element".equals(path.getSubPath())) {
			int kkkk = 5555;
			kkkk++;
		}

		HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(path, parentContext, categaryes, mode);
		
		if(resolveInfo==null || resolveInfo.rootNode==null)
			//cannot find referred root node
			throw new RuntimeException();

		switch(resolveInfo.rootNode.getDefinition().getType()) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
		{
			//if refer to a constant element
			out = new HAPContextDefinitionLeafConstant();
			Object constantValue = ((HAPContextDefinitionLeafConstant)resolveInfo.rootNode.getDefinition()).getValue();
			((HAPContextDefinitionLeafConstant)out).setValue(constantValue);
			break;
		}
		default:
		{
			path.getRootElementId().setCategary(resolveInfo.path.getRootElementId().getCategary());
			defContextElementRelative.setPath(path);
			
			HAPContextDefinitionElement parentContextEle = resolveInfo.resolvedNode; 
			if(parentContextEle!=null){
				HAPContextDefinitionElement relativeContextEle = defContextElementRelative.getDefinition();
				if(relativeContextEle==null) {
					defContextElementRelative.setDefinition(parentContextEle.cloneContextDefinitionElement());
				}
				else {
					//figure out matchers
					Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
					HAPUtilityContext.mergeContextDefitionElement(parentContextEle, relativeContextEle, false, matchers, null, contextProcessRequirement);
					//remove all the void matchers
					Map<String, HAPMatchers> noVoidMatchers = new LinkedHashMap<String, HAPMatchers>();
					for(String p : matchers.keySet()){
						HAPMatchers match = matchers.get(p);
						if(!match.isVoid()){
							noVoidMatchers.put(p, match);
						}
					}
					defContextElementRelative.setMatchers(noVoidMatchers);
				}
			}
			else{
				//not find parent node, throw exception
				throw new RuntimeException();
			}
		}
		}
		return out;
	}

}
