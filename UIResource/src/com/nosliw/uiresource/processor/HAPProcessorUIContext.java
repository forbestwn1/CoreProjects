package com.nosliw.uiresource.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextNodeRoot;
import com.nosliw.data.core.script.context.HAPContextNodeRootAbsolute;
import com.nosliw.data.core.script.context.HAPContextNodeRootConstant;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextRootNodeId;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPInfoRelativeContextResolve;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContext;
import com.nosliw.uiresource.tag.HAPUITagId;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPProcessorUIContext {

	//process context information
	public static void process(HAPExecutableUIUnit uiExe, HAPContextGroup parentContext, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		
		HAPContextGroup contextDef = uiExe.getUIUnitDefinition().getContextDefinition();
		
		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			
			String tagName = ((HAPDefinitionUIUnitTag)uiTagExe.getUIUnitDefinition()).getTagName();
			if(HAPConstant.UITAG_NAME_INCLUDE.equals(tagName)) {
				//process include tag
				String includeContextMapping = uiExe.getAttributes().get(HAPConstant.UITAG_NAME_INCLUDE_PARM_CONTEXT);
				Map<String, String> contextMapping = HAPNamingConversionUtility.parsePropertyValuePairs(includeContextMapping);
				
				contextDef = contextDef.clone();
				HAPContext context = contextDef.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);

				for(String key : context.getElementNames()) {
					if(HAPBasicUtility.isStringEmpty(contextMapping.get(key)))			contextMapping.put(key, key);
				}
				
				for(String key : context.getElementNames()) {
					HAPContextNodeRoot rootNode = context.getElement(key);
					if(rootNode.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE)) {
						HAPContextNodeRootAbsolute node = (HAPContextNodeRootAbsolute)rootNode;
						String mappedName = contextMapping.get(key);
						HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(mappedName), parentContext, null, HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_FIRST);
						if(resolveInfo!=null) {
							//find matched one
							HAPContextNodeRoot newRootNode = HAPUtilityContext.createInheritedElement(resolveInfo.rootNode, resolveInfo.path.getRootElementId().getCategary(), resolveInfo.path.getRootElementId().getName());
							context.addElement(key, newRootNode);
						}
						else {
							//not find
							
						}
					}
				}
			}
			
			//for custom tag, build context for tag first : merge parent context with context definition in tag definition first
			parentContext = buildUITagContext(uiTagExe, parentContext, uiTagMan, contextProcessorEnv);
			uiTagExe.setTagContext(parentContext);
			//flat it
			uiTagExe.setFlatTagContext(HAPUtilityContext.buildFlatContext(uiTagExe.getTagContext()));
		}
		
		//merge with context defined in tag unit
		HAPContextGroup extContextGroup = HAPProcessorContext.process(contextDef, parentContext, new HAPConfigureContextProcessor(), contextProcessorEnv);
		uiExe.setContext(extContextGroup);

		//build flat context
		HAPContextFlat flatContext = HAPUtilityContext.buildFlatContext(uiExe.getContext());
		uiExe.setFlatContext(flatContext);

		Map<String, Object> constantsValue = flatContext.getConstantValue();
		for(String name : constantsValue.keySet()) {
			Object constantValue = constantsValue.get(name);
			uiExe.addConstantValue(name, constantValue);
		}
		
		//build variables
		Map<String, HAPVariableInfo> varsInfo = HAPUtilityContext.discoverDataVariablesInContext(flatContext.getContext());
		for(String varName : varsInfo.keySet()) {
			uiExe.getExpressionContext().addVariable(varName, varsInfo.get(varName));
		}
	}
	

	
	//build context for ui Tag
	private static HAPContextGroup buildUITagContext(HAPExecutableUIUnitTag uiTag, HAPContextGroup parentContext, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		//get contextDef 
		HAPUITagDefinitionContext tagDefinitionContext = uiTagMan.getUITagDefinition(new HAPUITagId(uiTag.getUIUnitTagDefinition().getTagName())).getContext();

		//add attribute constant as part of tagContext
		Map<String, String> constants = uiTag.getAttributes();
		HAPContextGroup tagContext = tagDefinitionContext.clone();
		for(String cstName : constants.keySet()) {
			HAPContextNodeRootConstant cstRootNode = new HAPContextNodeRootConstant(constants.get(cstName));
			tagContext.addElement(cstName, cstRootNode, HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL);
		}
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = tagDefinitionContext.getInheritMode();
		return HAPProcessorContext.process(tagContext, parentContext, configure, contextProcessorEnv);
	}
}
