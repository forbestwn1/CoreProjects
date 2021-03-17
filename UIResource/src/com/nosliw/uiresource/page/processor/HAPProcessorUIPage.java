package com.nosliw.uiresource.page.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPIdGenerator;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;

public class HAPProcessorUIPage {

	public static HAPExecutableUIUnitPage processUIResource(
			HAPDefinitionUIPage uiPageDef,
			String id,
			HAPContextGroup externalContext,
			HAPContextGroup parentContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan,
			HAPManagerUITag uiTagMan,  
			HAPParserPage uiResourceParser,
			HAPIdGenerator idGengerator) {
		
		HAPExecutableUIUnitPage out = new HAPExecutableUIUnitPage(uiPageDef, id);

		//expand include and build mapping for include tag
		HAPProcessorInclude.processInclude(uiPageDef, uiResourceParser, uiResourceMan, runtimeEnv.getResourceDefinitionManager());
		
		//process attachment
		HAPProcessorAttachment.processAttachment(uiPageDef, null, uiTagMan);
		
		//expand referred context part
		HAPProcessorUIContext.processContextReference(uiPageDef, runtimeEnv.getResourceDefinitionManager());
		
		//enhance context by service
		
		
		//process context
		
		
		
		//context in page can be replaced with external context
		//build page context by external context override context defined in page
		HAPContextGroup pageContext = uiPageDef.getContextNotFlat().cloneContextGroup();
		if(externalContext!=null) {
			for(String categary : externalContext.getContextTypes()) {
				HAPContext ctx = externalContext.getContext(categary);
				for(String eleName : ctx.getElementNames()) {
					pageContext.addElement(eleName, ctx.getElement(eleName), categary);
				}
			}
		}
			
		if(serviceProviders==null)  serviceProviders = new LinkedHashMap<String, HAPDefinitionServiceProvider>();
		HAPProcessorUIContext.process(out, pageContext, parentContext, serviceProviders, uiTagMan, runtimeEnv);

		//include -- 
		//		attachment,
		//build context
		//include
		//		build mapping for include tag
		//context -- 
		//		enhance context by service
		//		process context
		//		
		
		
		
		//compile definition to executable
		HAPProcessorCompile.process(out, null);

//		HAPPorcessorResolveName.resolve(out);
		
		HAPProcessorUIConstantInContext.resolveConstants(out, runtimeEnv.getRuntime());
		
		HAPProcessorUIExpression.processUIExpression(out, runtimeEnv.getRuntime(), runtimeEnv.getExpressionManager(), runtimeEnv);
		
		HAPProcessorUIEventEscalate.process(out, uiTagMan);
		
		HAPProcessorUICommandEscalate.process(out, uiTagMan);

//		HAPProcessorUIServiceEscalate.process(out, uiTagMan);
		
		HAPProcessorStyle.process(out);
		
		return out;
	}

}
