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
	
	
	private HAPDataSourceDefinitionManager m_dataSourceDefinitionManager;
	
	private Map<String, HAPDataSource> m_dataSources;
	
	public HAPDataSourceManager(HAPDataSourceDefinitionManager dataSourceDefinitionManager){
		this.m_dataSourceDefinitionManager = dataSourceDefinitionManager;
		this.m_dataSources = new LinkedHashMap<String, HAPDataSource>();
	}
	
	public void registerDataSource(String name, HAPDataSource dataSource){
		this.m_dataSources.put(name, dataSource);
	}
	
	public HAPData getData(String dataSource, Map<String, HAPData> parms){
		HAPData out = this.m_dataSources.get(dataSource).getData(parms);
		return out;
	}
	
}
