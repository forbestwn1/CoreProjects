package com.nosliw.core.application.division.manual.brick.data;

import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.data.HAPBlockData;

public class HAPManualDefinitionBlockData extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockData() {
		super(HAPEnumBrickType.DATA_100);
	}

	public HAPData getData() {    return (HAPData)this.getAttributeValueOfValue(HAPBlockData.DATA);     }

	public void setData(HAPData data) {    this.setAttributeValueWithValue(HAPBlockData.DATA, data);      }
	
}
