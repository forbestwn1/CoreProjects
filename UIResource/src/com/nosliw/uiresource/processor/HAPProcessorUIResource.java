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
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;
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
		
		HAPEnvContextProcessor contextProcessorEnv = new HAPEnvContextProcessor(dataTypeHelper, runtime, expressionMan);
		
		HAPExecutableUIUnitResource out = new HAPExecutableUIUnitResource(uiResourceDef);
		
		HAPProcessorCompile.compile(out);
		
		HAPProcessorUIContext.processUIUnitContext(out, null, uiTagMan, contextProcessorEnv);
		
		
		
		
		
		
		return out;
		
		
		
		//process include tags
		//process included ui resource and convert it into standard include tag
		HAPUIResourceIncludeTagProcessor.process(uiResource, uiResourceMan, dataTypeHelper, uiTagMan, runtime, expressionMan, uiResourceParser, idGengerator);
		
		//build expression context
		HAPProcessorUIContext.process(null, uiResource, uiTagMan, new HAPEnvContextProcessor(dataTypeHelper, runtime, expressionMan));

		HAPUIResourceContextEntityProcessor.process(null, uiResource, uiTagMan, new HAPEnvContextProcessor(dataTypeHelper, runtime, expressionMan));
		
		//process expression definition
		HAPUIResourceExpressionProcessor.process(uiResource, runtime, resourceMan);
		
		//discovery resources required
		HAPResourceDependencyProcessor.process(uiResource, resourceMan);
		uiResource.processed();
	}
}
