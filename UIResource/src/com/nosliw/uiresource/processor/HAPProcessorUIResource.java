package com.nosliw.uiresource.processor;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPParserUIResource;
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
			HAPParserUIResource uiResourceParser,
			HAPIdGenerator idGengerator) {
		
		HAPExecutableUIUnitResource out = new HAPExecutableUIUnitResource(uiResourceDef);

		//compile definition to executable
		HAPProcessorCompile.process(out, null);
		
		Set<String> inheritanceExcludedInfo = new HashSet<String>();
		inheritanceExcludedInfo.add(HAPConstant.UIRESOURCE_CONTEXTINFO_INSTANTIATE);
		HAPEnvContextProcessor contextProcessorEnv = new HAPEnvContextProcessor(dataTypeHelper, runtime, expressionMan, inheritanceExcludedInfo);
		HAPProcessorUIContext.process(out, null, uiTagMan, contextProcessorEnv);

//		HAPPorcessorResolveName.resolve(out);
		
		HAPProcessorUIConstant.resolveConstants(out, runtime);
		
		HAPProcessorUIExpression.processUIExpression(out, runtime, expressionMan);
		
		HAPProcessorUIEventEscalate.process(out, uiTagMan);
		
		HAPProcessorUICommandEscalate.process(out, uiTagMan);

		HAPProcessorUIServiceEscalate.process(out, uiTagMan);
		
		HAPProcessorResourceDependency.process(out, resourceMan);
		
		return out;
	}

}
