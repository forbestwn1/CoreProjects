package com.nosliw.core.application.division.manual.brick.data;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.data.HAPBlockData;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.data.core.data.HAPData;

public class HAPManualDefinitionBlockData extends HAPManualDefinitionBrickBlockSimple{

	public HAPManualDefinitionBlockData() {
		super(HAPEnumBrickType.DATA_100);
	}

	public HAPData getData() {    return (HAPData)this.getAttributeValueWithValue(HAPBlockData.DATA);     }

	public void setData(HAPData data) {    this.setAttributeWithValueValue(HAPBlockData.DATA, data);      }
	
}
