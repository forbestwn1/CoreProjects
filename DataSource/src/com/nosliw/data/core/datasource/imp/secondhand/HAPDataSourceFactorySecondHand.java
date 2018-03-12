package com.nosliw.data.core.datasource.imp.secondhand;

import com.nosliw.data.core.datasource.HAPDataSource;
import com.nosliw.data.core.datasource.HAPDefinition;
import com.nosliw.data.core.datasource.HAPDataSourceDefinitionManager;
import com.nosliw.data.core.datasource.HAPDataSourceFactory;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPDataSourceFactorySecondHand implements HAPDataSourceFactory{

	public static final String DATASOURCEFACTORY_NAME = "secondHand"; 
	
	private HAPDataSourceDefinitionManager m_dataSourceDefinitionManager;
	
	private HAPManagerTask m_expressionManager;
	
	private HAPRuntime m_runtime;
	
	public HAPDataSourceFactorySecondHand(
			HAPDataSourceDefinitionManager dataSourceDefinitionManager,
			HAPManagerTask expressionManager,
			HAPRuntime runtime){
		this.m_dataSourceDefinitionManager = dataSourceDefinitionManager;
		this.m_runtime = runtime;
		this.m_expressionManager = expressionManager;
	}
	
	@Override
	public HAPDataSource newDataSource(HAPDefinition dataSourceDefinition){
		return new HAPDataSourceImpSecondHand(dataSourceDefinition, this.m_dataSourceDefinitionManager, m_expressionManager, m_runtime);		
	}
	
	
}
