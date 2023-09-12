package com.nosliw.ui.entity.uicontent;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;


@HAPEntityWithAttribute
public class HAPExecutableEntityComplexUIContent extends  HAPExecutableEntityComplex{

	static final public String CONTENT = "content";  

	
	public void setContent(String content) {    this.setAttributeValueObject(CONTENT, content);        }
	public String getContent() {     return (String)this.getAttributeValue(CONTENT);     }
	
	
}
