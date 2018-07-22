package com.nosliw.uiresource.tag;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.context.HAPContext;
import com.nosliw.uiresource.context.HAPContextGroup;
import com.nosliw.uiresource.context.HAPContextNodeRoot;
import com.nosliw.uiresource.context.HAPContextNodeRootRelative;
import com.nosliw.uiresource.context.HAPContextParser;
import com.nosliw.uiresource.context.HAPContextUtility;
import com.nosliw.uiresource.page.HAPConstantUtility;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitUtility;
import com.nosliw.uiresource.parser.HAPUIResourceParser;

public class HAPUITagUtility {

	
	//build context for ui Tag
	public static void buildUITagContext(HAPContextGroup parentContext, HAPUIDefinitionUnitTag uiTag, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		//get contextDef 
		HAPUITagDefinitionContext tagContextDefinition = uiTagMan.getUITagDefinition(new HAPUITagId(uiTag.getTagName())).getContext();

		Map<String, String> constants = uiTag.getAttributes();
		
		if(tagContextDefinition.isInherit()){
			//add public context from parent
			for(String rootEleName : parentContext.getPublicContext().getElements().keySet()){
				HAPContextNodeRootRelative relativeEle = new HAPContextNodeRootRelative();
				relativeEle.setPath(rootEleName);
				uiTag.getContext().getPublicContext().addElement(rootEleName, HAPContextUtility.processContextDefinitionElement(rootEleName, relativeEle, parentContext, dataTypeHelper, constants, runtime, expressionManager));
			}
		}

		//element defined in tag definition
		HAPContextUtility.processContextGroupDefinition(parentContext, tagContextDefinition, uiTag.getContext(), constants, dataTypeHelper, runtime, expressionManager);
	}
}
