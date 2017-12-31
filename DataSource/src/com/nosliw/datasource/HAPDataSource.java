package com.nosliw.datasource;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public interface HAPDataSource {

	HAPDataSourceDefinition getDefinition();
	
	HAPData getData(Map<String, HAPData> parms);

}
