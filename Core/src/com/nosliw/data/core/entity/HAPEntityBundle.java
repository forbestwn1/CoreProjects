package com.nosliw.data.core.entity;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPEntityBundle extends HAPExecutableImp{

	private HAPExecutableEntity m_entityExe;

	private Object m_extraData;


	public HAPExecutableEntity getEntity() {   return this.m_entityExe;     }
	
	public Object getExtraData() {   return this.m_extraData;    }
	public void setExtraData(Object data) {   this.m_extraData = data;    }
	
	public Set<HAPResourceIdSimple> getComplexResourceDependency(){
		Set<HAPResourceIdSimple> out = new HashSet<HAPResourceIdSimple>();
//		for(HAPInfoResourceIdNormalize normalizedResourceId : this.m_externalComplexEntityDpendency) {
//			out.add(normalizedResourceId.getRootResourceIdSimple());
//		}
		return out;
	}

}
