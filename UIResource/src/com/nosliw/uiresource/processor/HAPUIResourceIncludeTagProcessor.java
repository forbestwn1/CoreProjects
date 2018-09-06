package com.nosliw.uiresource.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextNodeRoot;
import com.nosliw.data.core.script.context.HAPContextParser;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.page.definition.HAPUIDefinitionUnitUtility;
import com.nosliw.uiresource.page.definition.HAPParserUIResource;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPUIResourceIncludeTagProcessor {
/*
	public static void process(HAPDefinitionUIUnitResource uiResource, HAPUIResourceManager uiResourceMan, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager, HAPParserUIResource uiResourceParser, HAPIdGenerator idGengerator){
		Set<HAPDefinitionUIUnitTag> includeTags = new HashSet<HAPDefinitionUIUnitTag>();
		HAPUIDefinitionUnitUtility.getUITagByName(uiResource, HAPConstant.UITAG_NAME_INCLUDE, includeTags);
		for(HAPDefinitionUIUnitTag includeTag : includeTags){
			processIncludeTag(includeTag, uiResource, uiResourceMan, dataTypeHelper, uiTagMan, runtime, expressionManager, uiResourceParser, idGengerator);			
		}
	}	
	
	private static void processIncludeTag(HAPDefinitionUIUnitTag includeTagResource, HAPDefinitionUIUnitResource rootResource, HAPUIResourceManager uiResourceMan, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager, HAPParserUIResource uiResourceParser, HAPIdGenerator idGengerator){
		String includeResourceName = includeTagResource.getAttributes().get(HAPConstant.UITAG_NAME_INCLUDE_PARM_SOURCE);

		//build include tag
		HAPDefinitionUIUnitResource uiResource = uiResourceMan.getUIResourceDefinitionById(includeResourceName);
		uiResourceParser.parseContent(includeTagResource, uiResource.getSource());
		HAPProcessorUIConstant.processConstantDefs(includeTagResource, null, expressionManager, runtime);

		//get context mapping definition
		String contextMapName = includeTagResource.getAttributes().get(HAPConstant.UITAG_NAME_INCLUDE_PARM_CONTEXT);
		HAPContextGroup contextDef = null;
		//context mapping defined as constant
		JSONObject contextMappingJson = (JSONObject)rootResource.getConstantValues().get(contextMapName);
		if(contextMappingJson!=null){
			contextDef = new HAPContextGroup();
			HAPContextParser.parseContextGroup(contextMappingJson, contextDef);
			//then apply it in include context
			includeTagResource.setContextDefinition(contextDef);
		}
		
		//loop through all context elements in included resource
		//if context element cannot find mapping, then it should be escalated to context in root resource
		HAPContextGroup contextGroup = includeTagResource.getContextDefinition();
		for(String contextType : contextGroup.getAllContextTypes()){
			HAPContext context = contextGroup.getContext(contextType);
			Map<String, HAPContextNodeRoot> elements = context.getElements();
			for(String eleName : elements.keySet()){
				HAPContextNodeRoot defEle = null;
				if(contextDef!=null)	defEle = contextDef.getElement(contextType, eleName);
				if(defEle==null){
					//not mapped, add to root context
					HAPContextNodeRoot rootNode = elements.get(eleName);
					rootResource.getContextDefinition().getContext(contextType).addElement(eleName, rootNode);
				}
			}
		}
		contextGroup.empty();
	}
*/
}
