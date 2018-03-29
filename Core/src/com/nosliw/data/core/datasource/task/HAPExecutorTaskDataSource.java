package com.nosliw.data.core.datasource.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.datasource.HAPDataSourceManager;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecuteExpression;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoRuntimeUtility;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskExecuteConverterRhino;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPExecutorTask;
import com.nosliw.data.core.task.HAPTaskReferenceCache;

public class HAPExecutorTaskDataSource implements HAPExecutorTask{

	private HAPDataSourceManager m_dataSourceManager;
	private HAPRuntime m_runtime;
	
	public HAPExecutorTaskDataSource(HAPDataSourceManager dataSourceManager, HAPRuntime runtime) {
		this.m_dataSourceManager = dataSourceManager;
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPData execute(HAPExecutableTask task, Map<String, HAPData> parms, HAPTaskReferenceCache cache) {
		
		HAPExecutableTaskDataSource dataSourceExe = (HAPExecutableTaskDataSource)task;
		
		Map<String, HAPData> dataSourceParms = new LinkedHashMap<String, HAPData>();
		//prepare data source parms : converter, expression result
		for(String parm : dataSourceExe.getParmsOperand().keySet()) {
			this.calculateParmData(dataSourceExe.getParmsOperand().get(parm), dataSourceExe.getMatchers().get(parm), parms);
		}
		
		//execute data source
		HAPData out = this.m_dataSourceManager.getData(dataSourceExe.getDataSource(), dataSourceParms);
		
		return out;
	}

	private HAPData calculateParmData(HAPOperandWrapper operand, HAPMatchers matchers, Map<String, HAPData> parms) {
		HAPData parmData = HAPRhinoRuntimeUtility.executeOperandSync(operand, parms, m_runtime);
		
		HAPRuntimeTaskExecuteConverterRhino converterTask = new HAPRuntimeTaskExecuteConverterRhino(parmData, matchers); 
		HAPData out = (HAPData)this.m_runtime.executeTaskSync(converterTask).getData();
		return out;
	}
}
