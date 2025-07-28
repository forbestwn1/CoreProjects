package com.nosliw.entity.dataaccess;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;

/*
 * store entitys according to different type: normal, changed, dead, new 
 */
public class HAPTransitionEntityManager {

	private HAPEntityContainer m_entitys;
	private HAPEntityContainer m_changedEntitys;
	private HAPEntityContainer m_deadEntitys;
	private HAPEntityContainer m_newEntitys;

	public HAPTransitionEntityManager(){
		this.m_entitys = new HAPEntityContainer();
		this.m_deadEntitys = new HAPEntityContainer();
		this.m_newEntitys = new HAPEntityContainer();
		this.m_changedEntitys = new HAPEntityContainer();
	}
	
	public void clearup(){
		this.m_entitys.clearup();
		this.m_changedEntitys.clearup();
		this.m_deadEntitys.clearup();
		this.m_newEntitys.clearup();
	}
	
	public HAPEntityWraper getEntityByID(HAPEntityID ID){
		HAPEntityWraper out = null;
		out = m_entitys.getEntity(ID);
		if(out!=null)   return out;
		
		out = m_changedEntitys.getEntity(ID);
		if(out!=null)   return out;

		out = m_deadEntitys.getEntity(ID);
		if(out!=null)   return out;

		out = m_newEntitys.getEntity(ID);
		if(out!=null)   return out;

		return out;
	}
	
	/*
	 * get all entitys within this manager according to the status of entity
	 */
	public Set<HAPEntityWraper> getAllEntitys(int status){
		HAPEntityContainer entityContainer = this.getEntityContainer(status);
		return entityContainer.getAllEntitys();
	}
	
	/*
	 * get entity container within this manager according to the status of entity
	 */
	public HAPEntityContainer getEntityContainer(int status){
		HAPEntityContainer out = this.m_entitys;
		switch(status){
		case HAPConstant.DATAACCESS_ENTITYSTATUS_NORMAL:
			out = this.m_entitys;
			break;
		case HAPConstant.DATAACCESS_ENTITYSTATUS_CHANGED:
			out = this.m_changedEntitys;
			break;
		case HAPConstant.DATAACCESS_ENTITYSTATUS_NEW:
			out = this.m_newEntitys;
			break;
		case HAPConstant.DATAACCESS_ENTITYSTATUS_DEAD:
			out = this.m_deadEntitys;
			break;
		}
		return out;
	}
	
	/*
	 * add a new entity with status to this manager
	 */
	public void addEntity(HAPEntityWraper entityWraper, int status){
		switch(status){
		case HAPConstant.DATAACCESS_ENTITYSTATUS_NORMAL:
			this.m_entitys.addEntity(entityWraper);
			break;
		case HAPConstant.DATAACCESS_ENTITYSTATUS_CHANGED:
			this.m_entitys.remove(entityWraper.getID());
			if(this.m_newEntitys.getEntity(entityWraper.getID())!=null){
				//if this entity is currently a new entity, then keep it as new entity
				this.m_newEntitys.addEntity(entityWraper);
			}
			else{
				//otherwise, keep it as changed entity
				this.m_changedEntitys.addEntity(entityWraper);
			}
			break;
		case HAPConstant.DATAACCESS_ENTITYSTATUS_NEW:
			this.m_newEntitys.addEntity(entityWraper);
			break;
		case HAPConstant.DATAACCESS_ENTITYSTATUS_DEAD:
			this.m_entitys.remove(entityWraper.getID());
			this.m_changedEntitys.remove(entityWraper.getID());
			this.m_newEntitys.remove(entityWraper.getID());
			this.m_deadEntitys.addEntity(entityWraper);
			break;
		}
	}
	
}
