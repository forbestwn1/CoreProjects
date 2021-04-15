package com.nosliw.data.core.cronjob;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.dataable.HAPManagerDataable;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.process.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.definition.HAPManagerServiceDefinition;

public class HAPProcessorCronJob {

	public static HAPExecutableCronJob process(
			HAPDefinitionCronJob cronJobDefinition,
			String id,
			HAPContextGroup parentContext, 
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

		HAPConfigureContextProcessor contextProcessConfg = HAPUtilityConfiguration.getContextProcessConfigurationForCronJob();
		HAPProcessTracker processTracker = new HAPProcessTracker(); 

		HAPUtilityComponent.processComponentExecutable(out, parentContext, runtimeEnv, contextProcessConfg, processMan.getPluginManager());

		//process task
		HAPResourceDefinitionProcess processDef = out.getProcessDefinition(cronJobDefinition.getTask().getProcess());
		HAPExecutableProcess processExe = HAPProcessorProcess.process(processDef, null, out.getServiceProviders(), processMan, runtimeEnv, processTracker);
		HAPExecutableWrapperTask<HAPExecutableProcess> processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(cronJobDefinition.getTask().getTask(), processExe, HAPParentContext.createDefault(out.getContextStructure()), null, runtimeEnv);			
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
