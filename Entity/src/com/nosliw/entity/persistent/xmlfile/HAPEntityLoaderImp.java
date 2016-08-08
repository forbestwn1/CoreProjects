package com.nosliw.entity.persistent.xmlfile;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccessImp;
import com.nosliw.entity.dataaccess.HAPOperationAllResult;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

public abstract class HAPEntityLoaderImp extends HAPEntityDataAccessImp implements HAPEntityLoader{

	public HAPEntityLoaderImp(){
		super(null, null, null);
		clear();
		this.setOperationScope(HAPConstant.CONS_ENTITYOPERATION_SCOPE_GLOBAL);
	}
	
	@Override
	protected void preOperate(HAPEntityOperationInfo operation) {
		this.addOperationResult(operation);
	}
	
	@Override
	protected HAPEntityWraper getUserContextEntityByID(HAPEntityID ID, boolean ifKeep) {
//		HAPEntityWraper entity = this.getEntityNormalContainer().getEntity(ID);
//		if(entity!=null)  return entity;
//		
//		entity = this.getEntityChangedContainer().getEntity(ID);
//		if(entity!=null)  return entity;
//		
//		entity = this.getEntityNewContainer().getEntity(ID);
//		if(entity!=null)  return entity;
		
		return null;
	}

	@Override
	public void closeOperationResult(){
		super.closeOperationResult();
		this.commit();
	}
	
	
	@Override
	public HAPOperationAllResult commit() {
//		for(HAPEntityWraper entityWraper : this.getTransitEntitysByStatus(HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_DEAD)){
//			this.removeEntityFromPersist(entityWraper);
//		}
//		
//		for(HAPEntityWraper entityWraper : this.getTransitEntitysByStatus(HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_NEW){
//			this.persistEntity(entityWraper);
//			this.getEntityNormalContainer().addEntity(entityWraper);
//		}
//		this.getEntityNewContainer().clearup();
//
//		for(HAPEntityWraper entityWraper : this.getTransitEntitysByStatus(HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_CHANGED){
//			this.persistEntity(entityWraper);
//			this.getEntityNormalContainer().addEntity(entityWraper);
//		}
//		this.getEntityChangedContainer().clearup();
		
		return new HAPOperationAllResult();
	}
	
}
