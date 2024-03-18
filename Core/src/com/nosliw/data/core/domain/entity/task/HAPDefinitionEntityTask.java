package com.nosliw.data.core.domain.entity.task;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;

public class HAPDefinitionEntityTask extends HAPManualBrickComplex{

	public void setImpEntityType(String type) {   this.setAttributeValueObject(HAPExecutableEntityTask.TYPE, type);       }
	public String getImpEntityType() {    return (String)this.getAttributeValue(HAPExecutableEntityTask.TYPE);    }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTask out = new HAPDefinitionEntityTask();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
