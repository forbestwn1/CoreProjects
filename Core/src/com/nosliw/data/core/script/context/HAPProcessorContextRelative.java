package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPMatchers;

public class HAPProcessorContextRelative {

	public static HAPContextGroup process(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextGroup out = contextGroup.cloneContextGroup();
		for(String categary : HAPContextGroup.getAllContextTypes()){
			Map<String, HAPContextDefinitionRoot> eles = out.getElements(categary);
			for(String eleName : eles.keySet()) {
				HAPContextDefinitionRoot contextRoot = eles.get(eleName);
				contextRoot.setDefinition(processRelativeInContextDefinitionElement(contextRoot.getDefinition(), parentContextGroup, configure, contextProcessorEnv));
			}
		}
		return out;
	}

	public static HAPContext process(HAPContext context, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContext out = context.cloneContext();
		Map<String, HAPContextDefinitionRoot> eles = out.getElements();
		for(String eleName : eles.keySet()) {
			HAPContextDefinitionRoot contextRoot = eles.get(eleName);
			contextRoot.setDefinition(processRelativeInContextDefinitionElement(contextRoot.getDefinition(), parentContextGroup, configure, contextProcessorEnv));
		}
		return out;
	}
	
	private static HAPContextDefinitionElement processRelativeInContextDefinitionElement(HAPContextDefinitionElement defContextElement, HAPContextGroup parentContext, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextDefinitionElement out = defContextElement;
		switch(defContextElement.getType()) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPContextDefinitionLeafRelative relativeContextElement = (HAPContextDefinitionLeafRelative)defContextElement;
			if(!relativeContextElement.isProcessed()){
				List<String> categaryes = new ArrayList<String>();
				if(relativeContextElement.getParentCategary()!=null) categaryes.add(relativeContextElement.getParentCategary());
				else if(configure.parentCategary==null)   categaryes.addAll(Arrays.asList(HAPContextGroup.getVisibleContextTypes()));
				else   categaryes.addAll(Arrays.asList(configure.parentCategary));
				out = processRelativeContextDefinitionElement(relativeContextElement, parentContext, categaryes.toArray(new String[0]), configure.relativeResolveMode, contextProcessorEnv);
			}
			break;
		case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
			Map<String, HAPContextDefinitionElement> processedChildren = new LinkedHashMap<String, HAPContextDefinitionElement>();
			HAPContextDefinitionNode nodeContextElement = (HAPContextDefinitionNode)defContextElement;
			for(String childName : nodeContextElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInContextDefinitionElement(nodeContextElement.getChild(childName), parentContext, configure, contextProcessorEnv));
			}
			break;
		}
		return out;
	}
	
	private static HAPContextDefinitionElement processRelativeContextDefinitionElement(HAPContextDefinitionLeafRelative defContextElementRelative, HAPContextGroup parentContext, String[] categaryes, String mode, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextDefinitionElement out = defContextElementRelative;
		
		HAPContextPath path = defContextElementRelative.getPath(); 
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
					merge(parentContextEle, relativeContextEle, matchers, null, contextProcessorEnv);
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
		out.processed();
		return out;
	}

	//merge parent context def with child context def to another context out
	//also generate matchers from parent to child
	private static void merge(HAPContextDefinitionElement parent, HAPContextDefinitionElement def, Map<String, HAPMatchers> matchers, String path, HAPEnvContextProcessor contextProcessorEnv){
		String type = def.getType();
		switch(type) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
			HAPContextDefinitionLeafData dataParent = (HAPContextDefinitionLeafData)parent.getSolidContextDefinitionElement();
			HAPContextDefinitionLeafData dataDef = (HAPContextDefinitionLeafData)def;
			//cal matchers
			HAPMatchers matcher = contextProcessorEnv.dataTypeHelper.convertable(dataParent.getCriteria().getCriteria(), dataDef.getCriteria().getCriteria());
			matchers.put(path, matcher);
			break;
		case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
			HAPContextDefinitionNode nodeParent = (HAPContextDefinitionNode)parent;
			HAPContextDefinitionNode nodeDef = (HAPContextDefinitionNode)def;
			for(String nodeName : nodeDef.getChildren().keySet()) {
				HAPContextDefinitionElement childNodeParent = nodeParent.getChildren().get(nodeName);
				HAPContextDefinitionElement childNodeDef = nodeDef.getChildren().get(nodeName);
				
				switch(childNodeDef.getType()) {
				case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
				{
					merge(childNodeParent, childNodeDef, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), contextProcessorEnv);
					break;
				}
				case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
				{
					merge(childNodeParent, childNodeDef, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), contextProcessorEnv);
					break;
				}
				}
			}
			break;
		default:
			
			break;
		}
	}
}
