package com.nosliw.entity.persistent.mango;

import java.util.List;
import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.query.HAPPageInfo;
import com.nosliw.entity.query.HAPQueryComponent;
import com.nosliw.entity.query.HAPQueryComponentFactory;
import com.nosliw.entity.query.HAPQueryDefinition;
import com.nosliw.entity.query.HAPQueryEntityWraper;
import com.nosliw.entity.query.HAPQueryExecutor;
import com.nosliw.entity.query.HAPQueryManager;

public class HAPPersistentQueryManager extends HAPQueryManager{

	public HAPPersistentQueryManager(HAPEntityDataAccess dataAccess) {
		super(dataAccess);
	}

	@Override
	public HAPQueryComponent query(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo,	boolean isRequest) {
		HAPQueryComponent out = HAPQueryComponentFactory.createQueryComponent(query, queryParms, this, this.getDataTypeManager());
		
		HAPQueryExecutor executor = query.getExecutor();
		if(executor!=null){
			//executor defined, then use it to run query
			List<HAPQueryEntityWraper> entitys = executor.queryRequest(query, queryParms, pageInfo);
			out.addQueryEntityWrapers(entitys);
		}
		else{
			//
			throw new RuntimeException();
		}
		
		this.addQueryComponent(out);
		return out;
	}

	@Override
	public HAPQueryComponent query(String queryId, HAPPageInfo pageInfo, boolean isRequest) {
		return this.getQuery(queryId);
	}

}
