package com.nosliw.uiresource.page.processor;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.component.HAPProcessorComponent;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.ui.tag.HAPManagerUITag;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUITag;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit1;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPProcessorUIPage {

	public static HAPExecutableUIUnitPage processUIResource(
			HAPDefinitionUIUnitPage uiPageDef,
			String id,
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan,
			HAPManagerUITag uiTagMan,  
			HAPParserPage uiResourceParser,
			HAPGeneratorId idGengerator) {
		
		//----------------------  Normalize definition
		//expand include and build mapping for include tag
		HAPProcessorInclude.expandInclude(uiPageDef, uiResourceParser, uiResourceMan, runtimeEnv.getResourceDefinitionManager());
	
		//process attachment
		HAPProcessorAttachment.mergeAttachment(uiPageDef, null);

		//enhance value structure (error)
		HAPProcessorUIValueStructure.enhanceValueStructure(uiPageDef);
		
		
		
		
		HAPContextProcessor processContext = new HAPContextProcessor(uiPageDef, runtimeEnv);
		normalize(uiPageDef, processContext);

		//----------------------  Build executable
		HAPExecutableUIUnitPage out = new HAPExecutableUIUnitPage(uiPageDef, id);

		//process value structure
		HAPProcessorUIValueStructure.process(out, null, uiTagMan, runtimeEnv);

		//process component elements (event, command, service)
		processComponent(out, runtimeEnv);
		
		//process service
		HAPProcessorUIService.escalate(out, uiTagMan);
		
		//process expression
		HAPProcessorUIExpressionScript.buildExpressionScriptProcessContext(out, runtimeEnv);
		HAPProcessorUIExpressionScript.processUIScriptExpression(out, runtimeEnv);
		
		//process handler
		
		//process command
		HAPProcessorUICommand.escalateCommand(out, uiTagMan);

		//process event
		HAPProcessorUIEvent.escalateEvent(out, uiTagMan);
		
		//process style
		HAPProcessorStyle.process(out);
		
		//build executable value structure
		HAPProcessorUIValueStructure.buildExecutable(out);
		
		
		return out;
	}

	public static void normalize(HAPDefinitionUIUnit uiUnitDefinition, HAPContextProcessor processContext) {
		HAPProcessorComponent.normalize(uiUnitDefinition, processContext);
		//child tag
		for(HAPDefinitionUITag uiTag : uiUnitDefinition.getUITags()) {
			normalize(uiTag, processContext);
		}
	}
	
	public static void processComponent(HAPExecutableUIUnit1 uiExe, HAPRuntimeEnvironment runtimeEnv) {
		HAPProcessorComponent.process(uiExe.getUIUnitDefinition(), uiExe.getBody(), runtimeEnv);
		
		//child tag
		for(HAPExecutableUITag childTag : uiExe.getBody().getUITags()) {
			processComponent(childTag, runtimeEnv);			
		}
	}
}
