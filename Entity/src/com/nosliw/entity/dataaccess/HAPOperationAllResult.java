package com.nosliw.entity.dataaccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

/*
 * this is class to store the result of an entity operation in the form of a list entity operations
 * this entity is mainly used to return data back to client so that client can execute the same operations
 */
public class HAPOperationAllResult implements HAPSerializable{

	//a list of operation for result
	private List<HAPEntityOperationInfo> m_results;

	//store all the affected entitys as a result of the result
	private HAPTransitionEntityManager m_entitys;
	
	public HAPOperationAllResult(){
		this.m_results = new ArrayList<HAPEntityOperationInfo>();
		this.m_entitys = new HAPTransitionEntityManager();
	}
	
	/*
	 * 
	 */
	public void mergeWithResult(HAPOperationAllResult result){
		this.m_results.addAll(result.m_results);
	}

	/*
	 * remove operations
	 */
	public void removeResult(List<HAPEntityOperationInfo> operations){
		int k = this.m_results.size()-1;
		for(int i=operations.size()-1; i>=0; i--){
			for(; k>=0; k--){
				if(operations.get(i).equals(this.m_results.get(k))){
					this.m_results.remove(k);
					k--;
					break;
				}
			}
		}
	}
	
	public void addResult(HAPEntityOperationInfo result){	this.m_results.add(result);	}

	public void addEntity(HAPEntityWraper entityWraper, int status){	this.m_entitys.addEntity(entityWraper, status);	}	
	
	public Set<HAPEntityWraper> getAllEntitys(int status){		return this.m_entitys.getAllEntitys(status);	}
	
	public HAPEntityContainer getEntityContainer(int status){		return this.m_entitys.getEntityContainer(status);	}
	
	public void clearup(){		
		this.m_entitys.clearup();
		this.m_results = new ArrayList<HAPEntityOperationInfo>();
	}

	public void clearUpdatedEntitys(){		this.m_entitys.clearup();	}
	
	@Override
	public String toStringValue(String format) {	return HAPJsonUtility.getListObjectJson(m_results);	}
}
