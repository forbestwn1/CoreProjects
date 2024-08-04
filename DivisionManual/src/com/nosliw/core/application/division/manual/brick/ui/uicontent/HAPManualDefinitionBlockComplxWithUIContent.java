package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPWithUIContent;
import com.nosliw.core.application.brick.ui.uicontent.HAPWithUIId;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockComplex;

public class HAPManualDefinitionBlockComplxWithUIContent extends HAPManualDefinitionBrickBlockComplex implements HAPWithUIId{

	protected HAPManualDefinitionBlockComplxWithUIContent(HAPIdBrickType entityType) {
		super(entityType);
	}

	@Override
	public String getUIId() {   return (String)this.getAttributeValueWithValue(HAPWithUIId.UIID);   }
	public String setUIId(String uiId) {    return (String)this.getAttributeValueWithValue(HAPWithUIId.UIID);    }
	
	public HAPManualDefinitionBlockComplexUIContent getUIContent() {    return (HAPManualDefinitionBlockComplexUIContent)this.getAttributeValueWithBrick(HAPWithUIContent.UICONTENT);  }
	public void setUIContent(HAPManualDefinitionBlockComplexUIContent uiContent) {   this.setAttributeWithValueBrick(HAPWithUIContent.UICONTENT, uiContent);     }

}
