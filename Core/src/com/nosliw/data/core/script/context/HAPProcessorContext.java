package com.nosliw.data.core.script.context;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorContext {

	public static HAPContextStructure process(HAPContextStructure context, HAPParentContext parent, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextStructure out = null;
		if(context!=null) {
			String type = context.getType();
			if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				out = process((HAPContext)context, parent, new HashSet<String>(), configure, runtimeEnv);
			}
			else if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				out = process((HAPContextGroup)context, parent, new HashSet<String>(), configure, runtimeEnv);
			}
		}
		return out;
	}
	
	public static HAPContextGroup processRelative(HAPContextGroup contextGroup, HAPParentContext parent, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		return processRelative(contextGroup, parent, new HashSet<String>(), configure, runtimeEnv);
	}
	
	public static HAPContext process(HAPContext context, HAPParentContext parent, Set<String>  dependency, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		HAPContextGroup contextGroup = new HAPContextGroup();
		contextGroup.setContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		HAPContextGroup processed = process(contextGroup, parent, dependency, configure, runtimeEnv);
		return processed.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPContextGroup process(HAPContextGroup contextGroup, HAPParentContext parent, Set<String>  dependency, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		HAPContextGroup out = processStatic(contextGroup, parent, configure, runtimeEnv);
		out = processRelative(out, parent, dependency, configure, runtimeEnv);
		out.processed();
		return out;
	}

	//merge child context with parent context
	public static HAPContextGroup processStatic(HAPContextGroup contextGroup, HAPParentContext parent, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		
		//figure out all constant values in context
		contextGroup = HAPProcessorContextConstant.process(contextGroup, parent, configure.inheritMode, runtimeEnv);
		
		//solidate name in context  
		contextGroup = HAPProcessorContextSolidate.process(contextGroup, runtimeEnv);
		
		//process data rule
		HAPProcessorContextRule.process(contextGroup, runtimeEnv);
		
		//process inheritance
		contextGroup = HAPProcessorContextVariableInheritance.process(contextGroup, parent, configure.inheritMode, configure.inheritanceExcludedInfo, runtimeEnv);
		
		return contextGroup;
	}
	
	public static HAPContextGroup processRelative(HAPContextGroup contextGroup, HAPParentContext parent, Set<String>  dependency, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.process(contextGroup, parent, dependency, configure, runtimeEnv);
		
		return contextGroup;
		
	}
}
