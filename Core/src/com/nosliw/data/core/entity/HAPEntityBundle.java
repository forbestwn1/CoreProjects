package com.nosliw.data.core.entity;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPEntityBundle extends HAPExecutableImp{

	private HAPExecutableEntity m_entityExe;

	private HAPSerializable m_extraData;


	public HAPExecutableEntity getEntity() {   return this.m_entityExe;     }
	
	public HAPSerializable getExtraData() {   return this.m_extraData;    }
	
	public Set<HAPResourceIdSimple> getComplexResourceDependency(){
		Set<HAPResourceIdSimple> out = new HashSet<HAPResourceIdSimple>();
		for(HAPInfoResourceIdNormalize normalizedResourceId : this.m_externalComplexEntityDpendency) {
			out.add(normalizedResourceId.getRootResourceIdSimple());
		}
		return out;
	}

}
