package com.nosliw.uiresource.processor;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPParserUIResource;
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
			HAPParserUIResource uiResourceParser,
			HAPIdGenerator idGengerator) {
		
		HAPExecutableUIUnitResource out = new HAPExecutableUIUnitResource(uiResourceDef);

//		compile(out, null);
		HAPProcessorCompile.process(out, null);
		
//		processContext(out, null, uiResourceMan, dataTypeHelper, uiTagMan, runtime, expressionMan);
		HAPEnvContextProcessor contextProcessorEnv = new HAPEnvContextProcessor(dataTypeHelper, runtime, expressionMan);
		HAPProcessorUIContext.process(out, null, uiTagMan, contextProcessorEnv);

//		resolveName(out);
		HAPPorcessorResolveName.resolve(out);
		
//		resolveConstant(out, runtime);
		HAPProcessorUIConstant.resolveConstants(out, runtime);
		
//		processUIExpression(out, runtime, expressionMan);
		HAPProcessorUIExpression.processUIExpression(out, runtime, expressionMan);
		
//		processUIUnitExe(out, null, uiResourceMan, dataTypeHelper, uiTagMan, runtime, expressionMan, resourceMan, uiResourceParser, idGengerator);			
		
		HAPProcessorResourceDependency.process(out, resourceMan);
		
		return out;
	}

	private static void compile(HAPExecutableUIUnit uiUnitExe, HAPExecutableUIUnit parentUIUnitExe) {
		//turn definition to executable
		HAPProcessorCompile.process(uiUnitExe, parentUIUnitExe);
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiUnitExe.getUITags()) {
			compile(childTag, uiUnitExe);			
		}
	}

/*	
	private static void processContext(
			HAPExecutableUIUnit uiUnitExe, 
			HAPExecutableUIUnit parentUIUnitExe, 
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPUITagManager uiTagMan, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionMan) {

		HAPEnvContextProcessor contextProcessorEnv = new HAPEnvContextProcessor(dataTypeHelper, runtime, expressionMan);
		HAPProcessorUIContext.process(uiUnitExe, parentUIUnitExe==null?null:parentUIUnitExe.getContext(), uiTagMan, contextProcessorEnv);
		
		//child tag
//		for(HAPExecutableUIUnitTag childTag : uiUnitExe.getUITags()) {
//			processContext(childTag, uiUnitExe, uiResourceMan, dataTypeHelper, uiTagMan, runtime, expressionMan);			
//		}
	}
*/
	private static void resolveName(HAPExecutableUIUnit uiUnitExe) {
		HAPPorcessorResolveName.resolve(uiUnitExe);
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiUnitExe.getUITags()) {
			resolveName(childTag);			
		}
	}


	private static void resolveConstant(HAPExecutableUIUnit uiUnitExe, HAPRuntime runtime) {
		HAPProcessorUIConstant.resolveConstants(uiUnitExe, runtime);
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiUnitExe.getUITags()) {
			resolveConstant(childTag, runtime);			
		}
	}

	private static void processUIExpression(HAPExecutableUIUnit uiUnitExe, HAPRuntime runtime, HAPExpressionSuiteManager expressionMan) {
		HAPProcessorUIExpression.processUIExpression(uiUnitExe, runtime, expressionMan);
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiUnitExe.getUITags()) {
			processUIExpression(childTag, runtime, expressionMan);			
		}
	}
	
	/*
	private static void processUIUnitExe(
			HAPExecutableUIUnit uiUnitExe, 
			HAPExecutableUIUnit parentUIUnitExe, 
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPUITagManager uiTagMan, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan, 
			HAPParserUIResource uiResourceParser,
			HAPIdGenerator idGengerator) {

		//turn definition to executable
		HAPProcessorCompile.process(uiUnitExe, parentUIUnitExe);
		
		HAPEnvContextProcessor contextProcessorEnv = new HAPEnvContextProcessor(dataTypeHelper, runtime, expressionMan);
		HAPProcessorUIContext.process(uiUnitExe, parentUIUnitExe==null?null:parentUIUnitExe.getContext(), uiTagMan, contextProcessorEnv);
		
		HAPPorcessorResolveName.resolve(uiUnitExe);
		
		HAPProcessorUIConstant.resolveConstants(uiUnitExe, runtime);
		
		HAPProcessorUIExpression.processUIExpression(uiUnitExe, runtime, expressionMan);
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiUnitExe.getUITags()) {
			processUIUnitExe(childTag, uiUnitExe, uiResourceMan, dataTypeHelper, uiTagMan, runtime, expressionMan, resourceMan, uiResourceParser, idGengerator);			
		}
	}
	*/
}
