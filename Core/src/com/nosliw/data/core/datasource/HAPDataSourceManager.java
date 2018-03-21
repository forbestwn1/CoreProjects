package com.nosliw.data.core.datasource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPData;

@HAPEntityWithAttribute
public class HAPDataSourceManager {

	@HAPAttribute
	public static final String GATEWAY_DATASOURCE = "dataSource";
	
	private Map<String, HAPDataSourceFactory> m_dataSourceFactorys;
	
	private HAPDataSourceDefinitionManager m_dataSourceDefinitionManager;
	
	private Map<String, HAPDataSource> m_dataSources;
	
	public HAPDataSourceManager(){
		this.m_dataSourceDefinitionManager = new HAPDataSourceDefinitionManager();
		this.m_dataSources = new LinkedHashMap<String, HAPDataSource>();
		this.m_dataSourceFactorys = new LinkedHashMap<String, HAPDataSourceFactory>();
	}
	
	public void registerDataSource(String name, HAPDataSource dataSource){
		this.m_dataSources.put(name, dataSource);
	}

	public void registerDataSourceFactory(String name, HAPDataSourceFactory dataSourceFactory){
		this.m_dataSourceFactorys.put(name, dataSourceFactory);
	}
	
	public HAPData getData(String dataSourceName, Map<String, HAPData> parms){
		HAPDataSource dataSource = this.m_dataSources.get(dataSourceName);
		if(dataSource==null){
			try{
				//not exists, then create one using factory
				HAPDefinition def = this.m_dataSourceDefinitionManager.getDataSourceDefinition(dataSourceName);
				String imp = def.getImplementation();
				if(imp.contains(".")){
					//it is class name
					dataSource = (HAPDataSource)Class.forName(imp).newInstance();
				}
				else{
					//it is factory name
					dataSource = this.m_dataSourceFactorys.get(imp).newDataSource(def);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(dataSource!=null)   this.registerDataSource(dataSourceName, dataSource);
		}
		
		HAPData out = null;
		if(dataSource!=null)  out = dataSource.getData(parms);
		return out;
	}
	
	protected HAPDataSourceDefinitionManager getDataSourceDefinitionManager(){
		return this.m_dataSourceDefinitionManager;
	}
}
