package com.nosliw.ui.entity.uicontent;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntityComplexWithUIContent extends HAPExecutableEntityComplex{

	@HAPAttribute
	static final public String UICONTENT = "UIContent";  

	public void setUIContent(HAPExecutableEntityComplexUIContent uiContent) {     this.setAttributeValueObject(UICONTENT, uiContent);      }
	public HAPExecutableEntityComplexUIContent getUIContent() {    return (HAPExecutableEntityComplexUIContent)this.getAttributeValue(UICONTENT);     }
	
	
}
