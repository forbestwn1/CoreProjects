package com.nosliw.data.core.datasource.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.datasource.HAPDataSourceManager;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoRuntimeUtility;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteConverterRhino;
import com.nosliw.data.core.task111.HAPExecutableTask;
import com.nosliw.data.core.task111.HAPExecutorTaskImp;
import com.nosliw.data.core.task111.HAPLogTask;
import com.nosliw.data.core.task111.HAPTaskReferenceCache;

public class HAPExecutorTaskDataSource extends HAPExecutorTaskImp{

	private HAPDataSourceManager m_dataSourceManager;
	private HAPRuntime m_runtime;
	
	public HAPExecutorTaskDataSource(HAPDataSourceManager dataSourceManager, HAPRuntime runtime) {
		this.m_dataSourceManager = dataSourceManager;
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPData executeTask(HAPExecutableTask task, Map<String, HAPData> parms, HAPTaskReferenceCache cache, HAPLogTask logger) {
		
		HAPExecutableTaskDataSource dataSourceExe = (HAPExecutableTaskDataSource)task;
		
		Map<String, HAPData> dataSourceParms = new LinkedHashMap<String, HAPData>();
		dataSourceParms.putAll(parms);
		//prepare data source parms : converter, expression result
		for(String parm : dataSourceExe.getParmsOperand().keySet()) {
			HAPWrapperOperand parmOperand = dataSourceExe.getParmsOperand().get(parm);
			if(parmOperand!=null) {
				dataSourceParms.put(parm, this.calculateParmData(parmOperand, dataSourceExe.getMatchers().get(parm), parms));
			}
		}
		
		//execute data source
		HAPData out = this.m_dataSourceManager.getData(dataSourceExe.getDataSource(), dataSourceParms);
		
		return out;
	}

	private HAPData calculateParmData(HAPWrapperOperand operand, HAPMatchers matchers, Map<String, HAPData> parms) {
		HAPData parmData = HAPRhinoRuntimeUtility.executeOperandSync(operand, parms, null, m_runtime);
		
		HAPRuntimeTaskExecuteConverterRhino converterTask = new HAPRuntimeTaskExecuteConverterRhino(parmData, matchers); 
		HAPData out = (HAPData)this.m_runtime.executeTaskSync(converterTask).getData();
		return out;
	}
}
