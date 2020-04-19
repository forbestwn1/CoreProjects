package com.nosliw.uiresource.page.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPIdGenerator;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPProcessorUIPage {

	public static HAPExecutableUIUnitPage processUIResource(
			HAPDefinitionUIPage uiPageDef,
			String id,
			HAPContextGroup context,
			HAPContextGroup parentContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerResourceDefinition resourceDefMan,
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPUITagManager uiTagMan,  
			HAPRuntime runtime, 
			HAPManagerExpression expressionMan, 
			HAPResourceManagerRoot resourceMan, 
			HAPParserPage uiResourceParser,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPIdGenerator idGengerator) {
		
		HAPExecutableUIUnitPage out = new HAPExecutableUIUnitPage(uiPageDef, id);

		HAPRequirementContextProcessor requirementContextProcessor = HAPUtilityCommon.getDefaultContextProcessorRequirement(resourceDefMan, dataTypeHelper, runtime, expressionMan, serviceDefinitionManager);
		
		//compile definition to executable
		HAPProcessorCompile.process(out, null);

		//build page context by parent context override context defined in page
		HAPContextGroup pageContext = uiPageDef.getContextNotFlat().cloneContextGroup();
		if(context!=null) {
			for(String categary : context.getContextTypes()) {
				HAPContext ctx = context.getContext(categary);
				for(String eleName : ctx.getElementNames()) {
					pageContext.addElement(eleName, ctx.getElement(eleName), categary);
				}
			}
		}
			
		if(serviceProviders==null)  serviceProviders = new LinkedHashMap<String, HAPDefinitionServiceProvider>();
		HAPProcessorUIContext.process(out, pageContext, parentContext, serviceProviders, uiTagMan, requirementContextProcessor);

//		HAPPorcessorResolveName.resolve(out);
		
		HAPProcessorUIConstant.resolveConstants(out, runtime);
		
		HAPProcessorUIExpression.processUIExpression(out, runtime, expressionMan, requirementContextProcessor);
		
		HAPProcessorUIEventEscalate.process(out, uiTagMan);
		
		HAPProcessorUICommandEscalate.process(out, uiTagMan);

//		HAPProcessorUIServiceEscalate.process(out, uiTagMan);
		
		return out;
	}

}
