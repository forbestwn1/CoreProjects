package com.nosliw.core.application.division.manual.brick.data;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.data.HAPBlockData;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.data.core.data.HAPData;

public class HAPManualDefinitionBlockData extends HAPManualDefinitionBrickBlockSimple{

	public HAPManualDefinitionBlockData(HAPIdBrickType brickType) {
		super(brickType);
	}

	public HAPData getData() {    return (HAPData)this.getAttributeValueWithValue(HAPBlockData.DATA);     }

	public void setData(HAPData data) {    this.setAttributeWithValueValue(HAPBlockData.DATA, data);      }
	
}
