package com.nosliw.data.core.domain.entity.data;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;

public class HAPDefinitionEntityData extends HAPDefinitionEntityInDomainSimple{

	public static final String ATTR_DATA = "data";
	
	public HAPDefinitionEntityData() {
	}
	
	public void setData(HAPData data) {    this.setNormalAttributeValueObject(ATTR_DATA, data);    }
	public HAPData getData() {   return (HAPData)this.getNormalAttributeValue(ATTR_DATA);     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityData out = new HAPDefinitionEntityData();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
