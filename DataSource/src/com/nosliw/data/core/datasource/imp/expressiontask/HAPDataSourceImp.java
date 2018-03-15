package com.nosliw.data.core.datasource.imp.expressiontask;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.datasource.HAPDataSource;
import com.nosliw.data.core.datasource.HAPDefinition;
import com.nosliw.data.core.datasource.HAPDefinitionParm;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPDataSourceImp implements HAPDataSource{

	private HAPDefinition m_dataSourceDefinition;
	
	private HAPManagerTask m_taskManager;
	
	private String m_taskName;
	
	private HAPDefinitionTaskSuite m_suite;
	
	public HAPDataSourceImp(HAPDefinition dataSourceDefinition, String taskName, HAPDefinitionTaskSuite suite, HAPManagerTask taskManager) {
		this.m_dataSourceDefinition = dataSourceDefinition;
		this.m_suite = suite;
		this.m_taskName = taskName;
		this.m_taskManager = taskManager;
	}
	
	@Override
	public HAPData getData(Map<String, HAPData> parms) {
		//prepare parms
		Map<String, HAPData> realParms = new LinkedHashMap<String, HAPData>();
		for(String parmName : this.m_dataSourceDefinition.getParms().keySet()) {
			HAPDefinitionParm parmDef = this.m_dataSourceDefinition.getParms().get(parmName);
			HAPData parmValue = parms.get(parmName);
			if(parmValue==null) {
				//use default
				parmValue = parmDef.getDefault();
			}
		}
		
		return this.m_taskManager.executeTask(m_taskName, m_suite, realParms);
	}

}
