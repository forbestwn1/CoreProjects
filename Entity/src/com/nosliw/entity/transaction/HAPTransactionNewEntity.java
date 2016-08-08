package com.nosliw.entity.transaction;

import java.util.Set;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.dataaccess.HAPDataContext;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

public class HAPTransactionNewEntity extends HAPTransaction{

	public HAPTransactionNewEntity(HAPConfigure configure, HAPEntityDataAccess access, HAPDataContext dataContext) {
		super(configure, access, dataContext);
		this.setOperationScope(HAPConstant.CONS_ENTITYOPERATION_SCOPE_ENTITY);
	}

	private HAPEntityWraper getNewEntityWraper(){
		Set<HAPEntityWraper> entitys = this.getTransitEntitysByStatus(HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_NEW); 
		if(entitys.size()==0)  return null;
		else return entitys.toArray(new HAPEntityWraper[0])[0];
	}
	
//	@Override
	public HAPServiceData isValidOperationTransaction(HAPEntityOperationInfo operation) {
		HAPEntityID entityID = operation.getEntityID();
		if(entityID.equals(this.getNewEntityWraper().getID())){
			return HAPServiceData.createSuccessData();
		}
		return HAPServiceData.createFailureData();
	}

	@Override
	public HAPServiceData isValidOperation(HAPEntityOperationInfo operation) {
		return HAPServiceData.createSuccessData();
	}

}
