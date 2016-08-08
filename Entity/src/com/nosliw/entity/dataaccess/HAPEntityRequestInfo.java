package com.nosliw.entity.dataaccess;

import java.util.Collection;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.entity.data.HAPEntityID;

/*
 * class for storing information related with entity request
 */
public class HAPEntityRequestInfo implements HAPStringable{

	//a collection of entity ID that need to be loaded
	private Collection<HAPEntityID> m_IDs;

	//whether load referenced entity
	private boolean m_loadRelated = true;
	
	//whether should cache entity for later use
	private boolean m_keepEntity = true;
	
	public HAPEntityRequestInfo(Collection<HAPEntityID> IDs){
		this.m_IDs = IDs;
	}
	
	public Collection<HAPEntityID> getEntityIDs(){  return this.m_IDs;  }
	
	public boolean ifLoadRelated(){  return this.m_loadRelated;  }
	public void setLoadRelated(boolean load){  this.m_loadRelated=load;  }
	
	public boolean ifKeepEntity(){  return this.m_keepEntity; }
	public void setKeepEntity(boolean keep){ this.m_keepEntity = keep;  }

	@Override
	public String toStringValue(String format) {
		return null;
	}
}
