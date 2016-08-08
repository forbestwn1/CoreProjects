package com.nosliw.entity.query;

import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;

public class HAPQueryComponentComplex extends HAPQueryComponent{

	public HAPQueryComponentComplex(String queryId,
			HAPQueryDefinition queryDef, Map<String, HAPData> queryParms,
			HAPQueryManager queryManager, HAPDataTypeManager dataTypeMan) {
		super(queryId, queryDef, queryParms, queryManager, dataTypeMan);
	}

}
