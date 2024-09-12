package com.nosliw.data.core.domain.entity.data;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickSimple;
import com.nosliw.data.core.data.HAPData;

public class HAPDefinitionEntityData extends HAPManualBrickSimple{

	public static final String ATTR_DATA = "data";
	
	public void setData(HAPData data) {    this.setAttributeValueObject(ATTR_DATA, data);    }
	public HAPData getData() {   return (HAPData)this.getAttributeValueOfValue(ATTR_DATA);     }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityData out = new HAPDefinitionEntityData();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
