package com.nosliw.entity.transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.dataaccess.HAPEntityDataAccessImp;
import com.nosliw.entity.dataaccess.HAPOperationAllResult;
import com.nosliw.entity.dataaccess.HAPReferenceManager;
import com.nosliw.entity.dataaccess.HAPDataContext;
import com.nosliw.entity.operation.HAPEntityOperationFactory;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.utils.HAPTransactionUtility;

public abstract class HAPTransaction extends HAPEntityDataAccessImp{
	//only root operations (operations should submit when transaction commit)
	private List<HAPEntityOperationInfo> m_rootOperations;
	//all operations
	private List<HAPEntityOperationInfo> m_fullOperations;
	
	public HAPTransaction(HAPConfigure configure, HAPEntityDataAccess access, HAPDataContext dataContext){
		super(configure, access, dataContext);
		this.m_rootOperations = new ArrayList<HAPEntityOperationInfo>();
		this.m_fullOperations = new ArrayList<HAPEntityOperationInfo>();
	}
	
	@Override
	public void init(){
		this.m_queryManager = new HAPQueryManagerTransaction(this);
		this.m_referenceMan = new HAPReferenceManager(this);
		
		if(this.getOperationScope()==HAPConstant.CONS_ENTITYOPERATION_SCOPE_GLOBAL){
			HAPEntityDataAccess under = this.getUnderDataAccess();
			if(under!=null){
				for(String name : under.getAllQuerys()){
					this.getQueryManager().addQueryComponent(under.getQueryComponent(name).clone(this));
				}
			}
		}
	}
	
	@Override
	protected HAPEntityWraper getUserContextEntityByID(HAPEntityID rootID, boolean ifKeep){
		//check transit entitys
		HAPEntityWraper entity = this.getTransitEntity(rootID);
		
		//if entity is dead, then return null
		if(entity!=null && entity.getStatus()==HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_DEAD)  return null;
		
		if(entity==null){
			//if entity is not in transit, then try to get from under me data access
			HAPServiceData serviceData = this.getUnderDataAccess().readEntityByID(rootID);
			if(serviceData.isSuccess()){
				//clone wrapper for this data access
				entity = (HAPEntityWraper)((HAPEntityWraper)serviceData.getData()).cloneEntityWraper(this);
				//if keep, then put it into transit
				if(ifKeep)  this.addTransitEntity(entity, HAPConstant.CONS_DATAACCESS_ENTITYSTATUS_NORMAL);			
			}
		}
		return entity;
	}

	@Override
	public HAPOperationAllResult commit(){
		HAPEntityDataAccess underAccess = this.getUnderDataAccess();
		underAccess.openOperationResult();
		
		//submit all the root operations to underme data access 
		List<HAPEntityOperationInfo> operations = this.getRootOperations();
		for(HAPEntityOperationInfo operation : operations){
			this.getUnderDataAccess().submitOperation(operation);
		}
		
		this.getUnderDataAccess().updateQueryByResult();
		
		underAccess.closeOperationResult();
		return underAccess.getOperationResult();
	}
	
	/*
	 * when this transaction is roll backed, which data is updated
	 * this method is only used when rollback request, not for auto rollback
	 */
	public HAPOperationAllResult getRollBackResult() {
		HAPEntityDataAccess underDataAccess = this.getUnderDataAccess();
		
		//get all reversed operations for full operations
		HAPOperationAllResult result = new HAPOperationAllResult();
		List<HAPEntityOperationInfo> operations = this.getFullOperations();
		for(int i=operations.size()-1; i>=0; i--){
			HAPEntityOperationInfo reverseOpInfo = HAPEntityOperationFactory.getReverseOperation(operations.get(i));
			if(reverseOpInfo!=null)			result.addResult(reverseOpInfo);
		}
		
		//get all querys that are not belong to under me data access, so that, those query should be removed from client when rollback
		Set<String> underQuerys = underDataAccess.getAllQuerys();
		Set<String> currentQuerys = this.getAllQuerys();
		Set<String> removeQuerys = new HashSet<String>();
		for(String cq : currentQuerys){
			if(!underQuerys.contains(cq)){
				removeQuerys.add(cq);
			}
		}
		
		//call client to remove querys
		for(String removeQuery : removeQuerys){
			result.addResult(HAPEntityOperationFactory.createQueryDeleteOperation(removeQuery));
		}
		
		//call client to update querys
		if(this.getOperationScope()==HAPConstant.CONS_ENTITYOPERATION_SCOPE_GLOBAL){
			for(String query : underQuerys){
				result.addResult(HAPEntityOperationFactory.createQueryUpdateOperation(this.getQueryComponent(query)));
			}			
		}
		return result;
	}
	
	@Override
	protected void preOperate(HAPEntityOperationInfo operation){
		this.addOperation(operation);
		this.addOperationResult(operation);
	}
	
	/*
	 * add operation to operation list within data access
	 */
	public void addOperation(HAPEntityOperationInfo operation){
		this.m_fullOperations.add(operation);
		
		if(operation.isSubmitable()){
			int comScope = HAPTransactionUtility.getCurrentOperationScope(this.getUnderDataAccess().getOperationScope(), operation.getScope());
			switch(comScope){
			case HAPConstant.CONS_ENTITYOPERATION_SCOPE_ENTITY:
				if(operation.isRootOperation())  this.addRootOperation(operation);
				break;
			case HAPConstant.CONS_ENTITYOPERATION_SCOPE_GLOBAL:
				if(operation.isRootOperation())  this.addRootOperation(operation);
				break;
			case HAPConstant.CONS_ENTITYOPERATION_SCOPE_OPERATION:
				this.addRootOperation(operation);
				break;
			}
		}
	}
	
	public List<HAPEntityOperationInfo> getFullOperations(){
		return this.m_fullOperations;
	}

	public List<HAPEntityOperationInfo> getRootOperations(){
		return this.m_rootOperations;
	}
	
	private void addRootOperation(HAPEntityOperationInfo operation){
		operation.setIsRootOperation(true);
		this.m_rootOperations.add(operation);
	}
	
	protected HAPEntityOperationInfo getOperation(){
		if(this.m_rootOperations.size()<=0)  return null;
		return this.m_rootOperations.get(0);
	}
}
