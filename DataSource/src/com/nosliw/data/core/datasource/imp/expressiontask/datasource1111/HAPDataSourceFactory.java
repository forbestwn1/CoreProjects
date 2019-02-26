package com.nosliw.data.core.datasource;

public interface HAPDataSourceFactory {

	HAPExecutableDataSource newDataSource(HAPDefinition dataSourceDefinition);
	
}
