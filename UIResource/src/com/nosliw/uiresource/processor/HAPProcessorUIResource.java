package com.nosliw.uiresource.processor;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPParserUIResource;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPProcessorUIResource {

	public static HAPExecutableUIUnitResource processUIResource(
			HAPDefinitionUIUnitResource uiResourceDef,
			String id,
			HAPContext parentContext,
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPUITagManager uiTagMan, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan, 
			HAPParserUIResource uiResourceParser,
			HAPIdGenerator idGengerator) {
		
		HAPExecutableUIUnitResource out = new HAPExecutableUIUnitResource(uiResourceDef, id);

		//compile definition to executable
		HAPProcessorCompile.process(out, null);
		
		Set<String> inheritanceExcludedInfo = new HashSet<String>();
		inheritanceExcludedInfo.add(HAPConstant.UIRESOURCE_CONTEXTINFO_INSTANTIATE);
		HAPRequirementContextProcessor contextProcessRequirement = new HAPRequirementContextProcessor(dataTypeHelper, runtime, expressionMan, inheritanceExcludedInfo);
		HAPContextGroup parentContextGroup = null;
		if(parentContext!=null) {
			parentContextGroup = new HAPContextGroup();
			parentContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, parentContext);
		}
		HAPProcessorUIContext.process(out, parentContextGroup, uiTagMan, contextProcessRequirement);

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
