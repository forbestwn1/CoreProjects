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

public class HAPTransactionDeleteEntity extends HAPTransaction{

	public HAPTransactionDeleteEntity(HAPConfigure configure, HAPEntityDataAccess access, HAPDataContext dataContext) {
		super(configure, access, dataContext);
		this.setOperationScope(HAPConstant.CONS_ENTITYOPERATION_SCOPE_ENTITY);
	}

	@Override
	protected HAPEntityWraper getUserContextEntityByID(HAPEntityID ID, boolean ifKeep) {
		HAPEntityWraper out = null;
		HAPEntityID myID = this.getOperation().getEntityID();
		if(ID.equals(myID)){
			Set<HAPEntityWraper> entitys = this.getTransitEntitysByStatus(HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_DEAD); 
			if(entitys.size()==0){
				HAPEntityWraper wraper = (HAPEntityWraper)this.getUnderDataAccess().useEntityByID(ID).getData();
				out = wraper.cloneEntityWraper(this);
				this.addTransitEntity(out, HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_NORMAL);
			}
			else{
				out = entitys.toArray(new HAPEntityWraper[0])[0];
			}
		}
		else{
			out = (HAPEntityWraper)this.getUnderDataAccess().useEntityByID(ID).getData();
		}
		return out;
	}

	@Override
	public HAPServiceData isValidOperation(HAPEntityOperationInfo operation) {
		if(this.getOperation()!=null)  return HAPServiceData.createFailureData();
		return HAPServiceData.createSuccessData();
	}

}
