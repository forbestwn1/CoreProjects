package com.nosliw.data.core.component;

import com.nosliw.data.core.component.event.HAPDefinitionHandlerEvent;
import com.nosliw.data.core.component.event.HAPExecutableHandlerEvent;
import com.nosliw.data.core.component.event.HAPProcessEvent;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;

public class HAPProcessorEmbededComponent {

	public static void process(HAPDefinitionEmbededComponent embededComponentDef, HAPExecutableEmbededComponent embededComponentExe, HAPExecutableComponent childComponent, HAPExecutableComponent parentComponent, HAPRuntimeEnvironment runtimeEnv) {
		embededComponentExe.setComponent(childComponent);
		
		//input data mapping between module and page
		for(HAPDefinitionDataAssociation dataAssociation : embededComponentDef.getInDataAssociations().getDataAssociations()) {
			HAPExecutableDataAssociation inputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(
					HAPContainerStructure.createDefault(parentComponent.getValueStructure()), 
					dataAssociation, 
					HAPContainerStructure.createDefault(childComponent.getValueStructure()), 
					null, 
					runtimeEnv);
			embededComponentExe.addInDataAssociation(inputDataAssocation);
		}
		
		//output data mapping
		for(HAPDefinitionDataAssociation dataAssociation : embededComponentDef.getOutDataAssociations().getDataAssociations()) {
			HAPExecutableDataAssociation outputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(
					HAPContainerStructure.createDefault(childComponent.getValueStructure()), 
					dataAssociation,
					HAPContainerStructure.createDefault(parentComponent.getValueStructure()), 
					null, 
					runtimeEnv);
			embededComponentExe.addOutDataAssociation(outputDataAssocation);
		}

		//process event handler
		for(HAPDefinitionHandlerEvent eventHandler : embededComponentDef.getEventHandlers()) {
			HAPExecutableHandlerEvent eventHandlerExe = HAPProcessEvent.processEventHandler(eventHandler, childComponent.getEvent(eventHandler.getEventName()), parentComponent.getValueStructure(), runtimeEnv);
			embededComponentExe.addEventHandler(eventHandlerExe);
		}
		
	}
}
