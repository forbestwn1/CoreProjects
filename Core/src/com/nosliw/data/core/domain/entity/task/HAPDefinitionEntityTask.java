package com.nosliw.data.core.domain.entity.task;

import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.core.application.division.manual.HAPManualEntityComplex;

public class HAPDefinitionEntityTask extends HAPManualEntityComplex{

	public void setImpEntityType(String type) {   this.setAttributeValueObject(HAPExecutableEntityTask.TYPE, type);       }
	public String getImpEntityType() {    return (String)this.getAttributeValue(HAPExecutableEntityTask.TYPE);    }
	
	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTask out = new HAPDefinitionEntityTask();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
