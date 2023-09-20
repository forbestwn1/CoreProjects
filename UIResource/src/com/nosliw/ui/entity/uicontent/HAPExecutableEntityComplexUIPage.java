package com.nosliw.ui.entity.uicontent;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;

@HAPEntityWithAttribute
public class HAPExecutableEntityComplexUIPage extends HAPExecutableEntityComplexWithUIContent implements HAPWithUIId{

	@Override
	public String getUIId() {    return (String)this.getAttributeValue(HAPWithUIId.UIID);  }
	public void setUIId(String uiId) {    this.setAttributeValueObject(HAPWithUIId.UIID, uiId);    }

	
	
	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {}
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {}

	
}
