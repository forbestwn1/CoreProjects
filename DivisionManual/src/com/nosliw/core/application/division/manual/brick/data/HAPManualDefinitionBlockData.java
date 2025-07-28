package com.nosliw.core.application.division.manual.brick.data;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.data.HAPBlockData;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.data.HAPData;

public class HAPManualDefinitionBlockData extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockData() {
		super(HAPEnumBrickType.DATA_100);
	}

	public HAPData getData() {    return (HAPData)this.getAttributeValueOfValue(HAPBlockData.DATA);     }

	public void setData(HAPData data) {    this.setAttributeValueWithValue(HAPBlockData.DATA, data);      }
	
}
