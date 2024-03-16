package com.nosliw.data.core.domain.entity.data;

import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.core.application.division.manual.HAPManualEntitySimple;
import com.nosliw.data.core.data.HAPData;

public class HAPDefinitionEntityData extends HAPManualEntitySimple{

	public static final String ATTR_DATA = "data";
	
	public void setData(HAPData data) {    this.setAttributeValueObject(ATTR_DATA, data);    }
	public HAPData getData() {   return (HAPData)this.getAttributeValue(ATTR_DATA);     }
	
	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityData out = new HAPDefinitionEntityData();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
