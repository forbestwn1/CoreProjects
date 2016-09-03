package com.nosliw.entity.query;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

public class HAPQueryComponentSimple extends HAPQueryComponent{

	public HAPQueryComponentSimple(String queryId, HAPQueryDefinition queryDef,
			Map<String, HAPData> queryParms, HAPQueryManager queryManager,
			HAPDataTypeManager dataTypeMan) {
		super(queryId, queryDef, queryParms, queryManager, dataTypeMan);
	}

	/*
	 * handle new entity
	 */
	public HAPEntityOperationInfo newEntity(HAPEntityWraper entity){
		HAPEntityOperationInfo operation = null;
		if(m_revalidation==true){
			HAPQueryEntityWraper queryEntityWraper = this.processEntity(entity);
			if(queryEntityWraper!=null){
				operation = this.addQueryEntityOperation(queryEntityWraper, HAPConstant.ENTITYOPERATION_SCOPE_GLOBAL);
			}
		}
		return operation;
	}

	public HAPEntityOperationInfo deleteEntity(HAPEntityID ID){
		return this.removeQueryEntityOperation(ID.toString(), HAPConstant.ENTITYOPERATION_SCOPE_GLOBAL);
	}

	public HAPEntityOperationInfo changeEntity(HAPEntityWraper entity){
		HAPQueryEntityWraper queryEntityWraper = this.processEntity(entity);
		if(queryEntityWraper==null){
			return this.deleteEntity(entity.getID());
		}
		else{
			return this.addOrUpdateQueryEntityOperation(queryEntityWraper, HAPConstant.ENTITYOPERATION_SCOPE_GLOBAL);
		}
	}	
}
