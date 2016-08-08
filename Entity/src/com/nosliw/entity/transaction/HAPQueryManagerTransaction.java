package com.nosliw.entity.transaction;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.query.HAPPageInfo;
import com.nosliw.entity.query.HAPQueryComponent;
import com.nosliw.entity.query.HAPQueryDefinition;
import com.nosliw.entity.query.HAPQueryManager;

public class HAPQueryManagerTransaction extends HAPQueryManager{

	
	public HAPQueryManagerTransaction(HAPEntityDataAccess dataAccess) {
		super(dataAccess);
	}

	@Override
	public HAPQueryComponent query(String queryId, HAPPageInfo pageInfo, boolean isRequest){
		HAPQueryComponent out = this.getQuery(queryId);
		
		return out;
	}
	
	/*
	 * query request for new query
	 */
	@Override
	public HAPQueryComponent query(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo, boolean isRequest){
		HAPQueryManager underQueryMan = this.getUnderQueryManager();
		//run query on under query manager
		HAPQueryComponent queryComponent = underQueryMan.query(query, queryParms, pageInfo, false);
		//change data access in query component to current data access
		queryComponent.setQueryManager(this);

		//update query component based on transit entity within current data access
		for(HAPEntityWraper wraper : this.m_dataAccess.getTransitEntitysByStatus(HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_CHANGED)){
			queryComponent.changeEntity(wraper);
		}

		for(HAPEntityWraper wraper : this.m_dataAccess.getTransitEntitysByStatus(HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_NEW)){
			queryComponent.newEntity(wraper);
		}
		
		for(HAPEntityWraper wraper : this.m_dataAccess.getTransitEntitysByStatus(HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_DEAD)){
			queryComponent.deleteEntity(wraper.getID());
		}
		
		this.m_querys.put(queryComponent.getQueryId(), queryComponent);
		return queryComponent;
	}
}
