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

public class HAPTransactionNewElement extends HAPTransaction{
	
	public HAPTransactionNewElement(HAPConfigure configure, HAPEntityDataAccess access, HAPDataContext dataContext) {
		super(configure, access, dataContext);
		this.setOperationScope(HAPConstant.ENTITYOPERATION_SCOPE_ENTITY);
	}

	private HAPEntityWraper getOperateEntityWraper(){
		Set<HAPEntityWraper> entitys = this.getTransitEntitysByStatus(HAPConstant.DATAACCESS_ENTITYSTATUS_CHANGED); 
		if(entitys.size()==0)  return null;
		else return entitys.toArray(new HAPEntityWraper[0])[0];
	}
	
	private void setOperateEntityWraper(HAPEntityWraper entity){
		this.addTransitEntity(entity, HAPConstant.DATAACCESS_ENTITYSTATUS_DEAD);
	}
	
//	@Override
	public HAPServiceData isValidOperationTransaction(HAPEntityOperationInfo operation) {
		HAPEntityID entityID = operation.getEntityID();
		if(entityID.equals(this.getOperateEntityWraper().getID())){
			return HAPServiceData.createSuccessData();
		}
		return HAPServiceData.createFailureData();
	}

	@Override
	protected HAPEntityWraper getUserContextEntityByID(HAPEntityID ID, boolean ifKeep) {
		HAPEntityID myID = this.getOperation().getEntityID();
		if(ID.equals(myID)){
			if(this.getOperateEntityWraper()==null){
				HAPEntityWraper wraper = (HAPEntityWraper)this.getUnderDataAccess().useEntityByID(ID).getData();
				this.setOperateEntityWraper(wraper.cloneEntityWraper(this));
			}
			return this.getOperateEntityWraper();
		}
		else{
			HAPEntityWraper wraper = (HAPEntityWraper)this.getUnderDataAccess().useEntityByID(ID).getData();
			return wraper;
		}
	}

	@Override
	public HAPServiceData isValidOperation(HAPEntityOperationInfo operation) {
		return HAPServiceData.createSuccessData();
	}
}
