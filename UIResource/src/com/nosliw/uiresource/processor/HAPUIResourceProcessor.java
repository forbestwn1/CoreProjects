package com.nosliw.uiresource.processor;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.parser.HAPUIResourceParser;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPUIResourceProcessor {

	public static void processUIResource(
			HAPUIDefinitionUnitResource uiResource, 
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPUITagManager uiTagMan, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan, 
			HAPUIResourceParser uiResourceParser,
			HAPIdGenerator idGengerator) {
		//process include tags
		//process included ui resource and convert it into standard include tag
		HAPUIResourceIncludeTagProcessor.process(uiResource, uiResourceMan, dataTypeHelper, uiTagMan, runtime, expressionMan, uiResourceParser, idGengerator);
		
		//build expression context
		HAPUIResourceContextProcessor.process(null, uiResource, dataTypeHelper, uiTagMan, runtime, expressionMan);

		HAPUIResourceContextEntityProcessor.process(null, uiResource, dataTypeHelper, uiTagMan, runtime, expressionMan);
		
		//process expression definition
		HAPUIResourceExpressionProcessor.process(uiResource, runtime, resourceMan);
		
		//discovery resources required
		HAPResourceDependencyProcessor.process(uiResource, resourceMan);
		uiResource.processed();
	}
}
