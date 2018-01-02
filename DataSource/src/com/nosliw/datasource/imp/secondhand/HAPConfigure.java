package com.nosliw.datasource.imp.secondhand;

import java.util.Map;

import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;

public class HAPConfigure {

	private Map<String, HAPDependentDataSource> m_dependentDataSources;
	
	private HAPExpressionDefinitionSuite m_expressionDefinitionSuite;
	

//    HAPExpressionDefinitionSuiteImp expressionDefinitionSuite = (HAPExpressionDefinitionSuiteImp)HAPStringableEntityImporterJSON.parseJsonEntity(inputStream, HAPExpressionDefinitionSuiteImp._VALUEINFO_NAME, HAPValueInfoManager.getInstance());

	public Map<String, HAPDependentDataSource> getDependentDataSources(){
		return this.m_dependentDataSources;
	}

	public HAPExpressionDefinitionSuite getExpressionSuite(){
		return this.m_expressionDefinitionSuite;
	}
	
}
