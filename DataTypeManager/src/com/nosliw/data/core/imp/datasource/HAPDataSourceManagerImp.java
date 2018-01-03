package com.nosliw.data.core.imp.datasource;

import com.nosliw.data.core.datasource.HAPDataSourceManager;
import com.nosliw.data.core.datasource.imp.secondhand.HAPDataSourceFactorySecondHand;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPDataSourceManagerImp extends HAPDataSourceManager{

	private HAPExpressionManager m_expressionManager;
	
	private HAPRuntime m_runtime;
	
	public HAPDataSourceManagerImp(			
			HAPExpressionManager expressionManager,
			HAPRuntime runtime) {
		super(new HAPDataSourceDefinitionManagerImp());
		this.m_expressionManager = expressionManager;
		this.m_runtime = runtime;
		
		this.registerDataSourceFactory(HAPDataSourceFactorySecondHand.DATASOURCEFACTORY_NAME, new HAPDataSourceFactorySecondHand(this.m_expressionManager, this.m_runtime));
	}

}
