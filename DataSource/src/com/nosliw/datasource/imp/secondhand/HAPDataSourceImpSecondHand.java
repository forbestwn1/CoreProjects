package com.nosliw.datasource.imp.secondhand;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.datasource.HAPDataSource;
import com.nosliw.datasource.HAPDataSourceDefinition;
import com.nosliw.datasource.HAPDataSourceDefinitionManager;
import com.nosliw.datasource.HAPDataSourceParm;

public class HAPDataSourceImpSecondHand implements HAPDataSource{

	private HAPDataSourceDefinitionManager m_dataSourceDefinitionManager;
	
	private HAPDataSourceDefinition m_dataSourceDefinition;
	
	private HAPConfigure m_configure;
	
	public HAPDataSourceImpSecondHand(HAPDataSourceDefinition dataSourceDefinition){
		this.m_dataSourceDefinition = dataSourceDefinition;
	}
	
	@Override
	public HAPData getData(Map<String, HAPData> parms) {
		
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
		
		
		
		return null;
	}

	@Override
	public HAPDataSourceDefinition getDefinition() {
		return this.m_dataSourceDefinition;
	}

}
