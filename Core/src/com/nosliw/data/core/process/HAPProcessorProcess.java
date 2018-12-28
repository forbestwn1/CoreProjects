package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;

public class HAPProcessorProcess{

	public static HAPExecutableProcess process(
			HAPDefinitionProcess processDefinition, 
			String id, 
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionProcess> processDefinitionsContext,
			HAPProcessContext processContext,
			HAPManagerProcess processMan,
			HAPEnvContextProcessor envContextProcessor) {
		
		HAPExecutableProcess out = null;

		//merge context with parent
		HAPContextGroup context = HAPProcessorContext.process(processDefinition.getContext(), parentContext, null, envContextProcessor);
		
		HAPContextGroup oldContext;
		do {
			out = new HAPExecutableProcess(processDefinition, id);
			oldContext = context.cloneContextGroup();
			Map<String, HAPDefinitionDataAssociationGroup> results = new LinkedHashMap<String, HAPDefinitionDataAssociationGroup>();
			Map<String, HAPDefinitionActivity> activities = processDefinition.getActivities();
			for(String activityId : activities.keySet()) {
				HAPDefinitionActivity activity = activities.get(activityId);
				
				//start activity
				if(activity.getType().equals(HAPConstant.ACTIVITY_TYPE_START))    out.setStartActivityId(activityId);    
				
				//process activity
				HAPExecutableActivity activityExe = processMan.getActivityPlugin(activity.getType()).getActivityProcessor().process(activity, activityId, out, context, processDefinitionsContext, processMan, envContextProcessor, processContext);
				out.addActivity(activityId, activityExe);
			}
		}while(!context.equals(oldContext));
		
		return out;
	}
	
}
