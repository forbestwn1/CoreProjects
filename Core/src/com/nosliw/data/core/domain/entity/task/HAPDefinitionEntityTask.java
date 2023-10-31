package com.nosliw.data.core.domain.entity.task;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public class HAPDefinitionEntityTask extends HAPDefinitionEntityInDomainComplex{

	public void setImpEntityType(String type) {   this.setAttributeValueObject(HAPExecutableEntityTask.TYPE, type);       }
	public String getImpEntityType() {    return (String)this.getAttributeValue(HAPExecutableEntityTask.TYPE);    }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTask out = new HAPDefinitionEntityTask();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
