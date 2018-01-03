package com.nosliw.data.core.datasource.imp.secondhand;

import com.nosliw.data.core.datasource.HAPDataSource;
import com.nosliw.data.core.datasource.HAPDataSourceDefinition;
import com.nosliw.data.core.datasource.HAPDataSourceFactory;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPDataSourceFactorySecondHand implements HAPDataSourceFactory{

	public static final String DATASOURCEFACTORY_NAME = "secondHand"; 
	
	private HAPExpressionManager m_expressionManager;
	
	private HAPRuntime m_runtime;
	
	public HAPDataSourceFactorySecondHand(
			HAPExpressionManager expressionManager,
			HAPRuntime runtime){
		this.m_runtime = runtime;
		this.m_expressionManager = expressionManager;
	}
	
	@Override
	public HAPDataSource newDataSource(HAPDataSourceDefinition dataSourceDefinition){
		return new HAPDataSourceImpSecondHand(dataSourceDefinition, m_expressionManager, m_runtime);		
	}
	
	
}
