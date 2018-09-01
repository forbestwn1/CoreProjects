package com.nosliw.uiresource.processor;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPUIResourceParser;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPProcessorUIResource {

	public static HAPExecutableUIUnitResource processUIResource(
			HAPDefinitionUIUnitResource uiResourceDef, 
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPUITagManager uiTagMan, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan, 
			HAPUIResourceParser uiResourceParser,
			HAPIdGenerator idGengerator) {
		
		HAPExecutableUIUnitResource out = new HAPExecutableUIUnitResource(uiResourceDef);

		processUIUnitExe(out, null, uiResourceMan, dataTypeHelper, uiTagMan, runtime, expressionMan, resourceMan, uiResourceParser, idGengerator);			
		
		return out;
	}


	private static void processUIUnitExe(
			HAPExecutableUIUnit uiUnitExe, 
			HAPExecutableUIUnit parentUIUnitExe, 
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPUITagManager uiTagMan, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan, 
			HAPUIResourceParser uiResourceParser,
			HAPIdGenerator idGengerator) {

		HAPProcessorCompile.compile(uiUnitExe, parentUIUnitExe);
		
		HAPEnvContextProcessor contextProcessorEnv = new HAPEnvContextProcessor(dataTypeHelper, runtime, expressionMan);
		HAPProcessorUIContext.processUIUnitContext(uiUnitExe, parentUIUnitExe==null?null:parentUIUnitExe.getContext(), uiTagMan, contextProcessorEnv);
		
		HAPProcessorUIConstant.resolve(uiUnitExe);
		
		HAPProcessorUIExpression.processUIExpression(uiUnitExe, runtime, expressionMan);
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiUnitExe.getUITags()) {
			processUIUnitExe(childTag, uiUnitExe, uiResourceMan, dataTypeHelper, uiTagMan, runtime, expressionMan, resourceMan, uiResourceParser, idGengerator);			
		}
	}
}
