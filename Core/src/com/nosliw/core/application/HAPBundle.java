package com.nosliw.core.application;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPBundle extends HAPExecutableImp{

	private HAPWrapperBrick m_entityInfo;

	private Object m_extraData;

	
	
	public HAPWrapperBrick getEntityInfo() {    return this.m_entityInfo;     }
	public void setEntityInfo(HAPWrapperBrick entityInfo) {     this.m_entityInfo = entityInfo;      }
	
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
