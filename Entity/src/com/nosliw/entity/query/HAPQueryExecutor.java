package com.nosliw.entity.query;

import java.util.List;
import java.util.Map;

import com.nosliw.data.HAPData;

public interface HAPQueryExecutor {

	public List<HAPQueryEntityWraper> queryRequest(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo);

}
