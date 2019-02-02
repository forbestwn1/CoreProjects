package com.nosliw.uiresource.page.processor;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.HAPUtilityUIResource;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserUIResource;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPProcessorUIPage {

	public static HAPExecutableUIUnitPage processUIResource(
			HAPDefinitionUIPage uiPageDef,
			String id,
			HAPContextGroup context,
			HAPContextGroup parentContext, 
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPUITagManager uiTagMan, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan, 
			HAPParserUIResource uiResourceParser,
			HAPIdGenerator idGengerator) {
		
		HAPExecutableUIUnitPage out = new HAPExecutableUIUnitPage(uiPageDef, id);

		//compile definition to executable
		HAPProcessorCompile.process(out, null);
		
		//build page context by parent context override context defined in page
		HAPContextGroup pageContext = uiPageDef.getContextDefinition().cloneContextGroup();
		if(context!=null) {
			for(String categary : context.getContextTypes()) {
				HAPContext ctx = context.getContext(categary);
				for(String eleName : ctx.getElementNames()) {
					pageContext.addElement(eleName, ctx.getElement(eleName), categary);
				}
			}
		}
			
		HAPProcessorUIContext.process(out, pageContext, parentContext, uiTagMan, HAPUtilityUIResource.getDefaultContextProcessorRequirement(dataTypeHelper, runtime, expressionMan));

//		HAPPorcessorResolveName.resolve(out);
		
		HAPProcessorUIConstant.resolveConstants(out, runtime);
		
		HAPProcessorUIExpression.processUIExpression(out, runtime, expressionMan);
		
		HAPProcessorUIEventEscalate.process(out, uiTagMan);
		
		HAPProcessorUICommandEscalate.process(out, uiTagMan);

		HAPProcessorUIServiceEscalate.process(out, uiTagMan);
		
		return out;
	}

}
