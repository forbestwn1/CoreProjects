package com.nosliw.datasource.imp.secondhand;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.datasource.HAPDataSource;
import com.nosliw.datasource.HAPDataSourceDefinition;
import com.nosliw.datasource.HAPDataSourceDefinitionManager;
import com.nosliw.datasource.HAPDataSourceParm;

public class HAPDataSourceImpSecondHand implements HAPDataSource{

	private HAPDataSourceDefinitionManager m_dataSourceDefinitionManager;
	
	private HAPExpressionManager m_expressionManager;
	
	private HAPRuntime m_runtime;
	
	private HAPDataSourceDefinition m_dataSourceDefinition;
	
	private HAPConfigure m_configure;
	
	public HAPDataSourceImpSecondHand(HAPDataSourceDefinition dataSourceDefinition){
		this.m_dataSourceDefinition = dataSourceDefinition;
	}
	
	private void init(){
		
		
		HAPExpressionDefinitionSuite expressionSuite = this.m_configure.getExpressionSuite();
		
		Map<String, HAPDataSourceParm> dataSourceParmsDef =  this.m_dataSourceDefinition.getParms();
		
		
		//replace operation 
		
		
		
		
		Map<String, HAPDependentDataSource> dependencys = this.m_configure.getDependentDataSources();
		for(String depName : dependencys.keySet()){
			//process dependency data source
			HAPDependentDataSource dependency = dependencys.get(depName);
			String dataSourceName = dependency.getName();
			HAPDataSourceDefinition depDataSourceDef = this.m_dataSourceDefinitionManager.getDataSourceDefinition(dataSourceName);
			for(String dataSourceParmName : dataSourceParmsDef.keySet()){
				HAPDataSourceParm depDataSourceParm = dataSourceParmsDef.get(dataSourceParmName);
				//each parm in dependent data source
				String parmName = depDataSourceParm.getName();
				HAPDependentDataSourceParm parmMapping = dependency.getParmMapping(parmName);
				if(parmMapping==null){
					//no mapping for parm, merge to parent
					dataSourceParmsDef.get()
				}
				else{
					//found mapping for parm, 
				}
			}
			
		}
		
		
		
	}
	
	
	@Override
	public HAPData getData(Map<String, HAPData> parms) {
		
		HAPExpression expression = m_expressionManager.processExpression(null, "main", m_configure.getExpressionSuite(), null);
		
		//execute expression
		HAPRuntimeTask task1 = new HAPRuntimeTaskExecuteExpressionRhino(expression, parms);
		task1.registerListener(new HAPRunTaskEventListener(){
			@Override
			public void finish(HAPRuntimeTask task){
				try{
					HAPServiceData serviceData = task.getResult();
					processResult(suite, serviceData);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		m_runtime.executeTask(task1);
		
		
		return null;
	}

	@Override
	public HAPDataSourceDefinition getDefinition() {
		return this.m_dataSourceDefinition;
	}

}
