package com.nosliw.data.core.datasource;

public interface HAPDataSourceFactory {

	HAPDataSource newDataSource(HAPDataSourceDefinition dataSourceDefinition);
	
}
