package com.nosliw.data.core.cronjob;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.service.HAPManagerServiceDefinition;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.dataable.HAPManagerDataable;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.domain.entity.expression.data1.HAPManagerExpression;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.process1.HAPProcessorProcess;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPProcessorCronJob {

	public static HAPExecutableCronJob process(
			HAPDefinitionCronJob cronJobDefinition,
			String id,
			HAPValueStructureDefinitionGroup parentContext, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPManagerProcess processMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPManagerExpression expressionManager,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPManagerResourceDefinition resourceDefMan,
			HAPManagerDataable dataableMan,
			HAPManagerScheduleDefinition scheduleMan) {

		HAPExecutableCronJob out = new HAPExecutableCronJob(cronJobDefinition, id);

		HAPConfigureProcessorValueStructure contextProcessConfg = HAPUtilityConfiguration.getContextProcessConfigurationForCronJob();
		HAPProcessTracker processTracker = new HAPProcessTracker(); 

		HAPUtilityComponent.processComponentExecutable(out, parentContext, runtimeEnv, contextProcessConfg, processMan.getPluginManager());

		//process task
		HAPResourceDefinitionProcess processDef = out.getProcessDefinition(cronJobDefinition.getTask().getProcess());
		HAPExecutableProcess processExe = HAPProcessorProcess.process(processDef, null, out.getServiceProviders(), processMan, runtimeEnv, processTracker);
		HAPExecutableWrapperTask<HAPExecutableProcess> processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(cronJobDefinition.getTask().getTask(), processExe, HAPContainerStructure.createDefault(out.getContextStructure()), null, runtimeEnv);			
		out.setTask(processExeWrapper);
 
		//process end
//		HAPDefinitionEnd endDef = cronJobDefinition.getEnd();
//		HAPDefinitionDataable resourceDef = (HAPDefinitionDataable)HAPAttachmentUtility.getResourceDefinition(cronJobDefinition.getAttachmentContainer(), endDef.getAttachmentReference().getType(), endDef.getAttachmentReference().getName(), resourceDefMan);
//		HAPExecutableDataable exetable = dataableMan.process(resourceDef);
//		
//		HAPExecutableWrapperTask endExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(endDef.getEmbeded(), exetable, HAPParentContext.createDefault(out.getContext()), null, contextProcessRequirement);			
//		out.setEnd(endExeWrapper);
	
		//process schedule
		HAPExecutablePollSchedule schedule = scheduleMan.parsePollSchedule(cronJobDefinition.getSchedule().getDefinition());
		out.setSchedule(schedule);
		
		return out;
	}
}
