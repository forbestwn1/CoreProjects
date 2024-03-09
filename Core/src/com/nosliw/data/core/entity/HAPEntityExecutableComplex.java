package com.nosliw.data.core.entity;

import com.nosliw.data.core.entity.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.entity.valuestructure.HAPExecutableEntityValueContext;

public class HAPEntityExecutableComplex extends HAPEntityExecutable{

	private HAPExecutableEntityValueContext m_valueContext;

	
	public void setValueContext(HAPExecutableEntityValueContext valueContext) {     this.m_valueContext = valueContext;      }
	public HAPExecutableEntityValueContext getValueContext() {    return this.m_valueContext;    }
	
	public void setValueStructureDomain(HAPDomainValueStructure valueStructureDomain) {   this.m_valueStructureDomain = valueStructureDomain;     }
	
	
}
