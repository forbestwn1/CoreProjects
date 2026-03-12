package com.nosliw.data.core.datasource;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public interface HAPExecutableDataSource {

	HAPData getData(Map<String, HAPData> parms);
	
}
