package com.nosliw.core.runtime.js.rhino;

import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.script.HAPJSScriptInfo;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.HAPOperationParm;
import com.nosliw.core.data.HAPRuntimeTaskExecuteDataOperation;
import com.nosliw.core.resource.HAPResourceInfo;
import com.nosliw.core.runtime.HAPRunTaskEventListener;
import com.nosliw.core.runtime.HAPRuntime;
import com.nosliw.core.runtime.HAPRuntimeTask;
import com.nosliw.core.runtime.js.rhino.task.HAPRuntimeTaskLoadResourcesRhino;

public class HAPRuntimeTaskExecuteDataOperationRhino extends HAPRuntimeTaskExecuteDataOperation{

	public HAPRuntimeTaskExecuteDataOperationRhino(HAPDataTypeId dataTypeId, String operation, List<HAPOperationParm> parms) {
		super(dataTypeId, operation, parms);
	}

	@Override
	public Class getResultDataType() {	return HAPData.class;	}
	
	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime){
		try{
			HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
			
			//prepare resources for data operation in the runtime (resource and dependency)
			//execute expression after load required resources
			List<HAPResourceInfo> resourcesId =	HAPUtilityExpressionResource.discoverResourceRequirement(this.getDataTypeId(), this.getOperation(),rhinoRuntime.getRuntimeEnvironment().getResourceManager(), runtime.getRuntimeInfo());
			
			HAPRuntimeTask loadResourcesTask = new HAPRuntimeTaskLoadResourcesRhino(resourcesId);
			loadResourcesTask.registerListener(new HAPRunTaskEventListenerInner(this, rhinoRuntime));
			return loadResourcesTask;
		}
		catch(Exception e){
			this.finish(HAPServiceData.createFailureData(e, ""));
			e.printStackTrace();
		}
		return null;
	}
	
	class HAPRunTaskEventListenerInner implements HAPRunTaskEventListener{
		private HAPRuntimeTaskExecuteDataOperationRhino m_parent;
		private HAPRuntimeImpRhino m_runtime;
		
		public HAPRunTaskEventListenerInner(HAPRuntimeTaskExecuteDataOperationRhino parent, HAPRuntimeImpRhino runtime){
			this.m_parent = parent;
			this.m_runtime = runtime;
		}
		
		@Override
		public void finish(HAPRuntimeTask task) {
			HAPServiceData resourceTaskResult = task.getResult();
			if(resourceTaskResult.isSuccess()){
				//after resource loaded, execute expression
				try{
					HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeRhinoScript.buildRequestScriptForExecuteDataOperationTask(this.m_parent, this.m_runtime);
					this.m_runtime.loadTaskScript(scriptInfo, m_parent.getTaskId());
				}
				catch(Exception e){
					this.m_parent.finish(HAPServiceData.createFailureData(e, ""));
				}
			}
			else{
				this.m_parent.finish(resourceTaskResult);
			}
		}
	}
}
