package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPWithUIContent;
import com.nosliw.core.application.brick.ui.uicontent.HAPWithUIId;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockComplxWithUIContent extends HAPManualDefinitionBrick implements HAPWithUIId{

	protected HAPManualDefinitionBlockComplxWithUIContent(HAPIdBrickType entityType) {
		super(entityType);
	}

	@Override
	public String getUIId() {   return (String)this.getAttributeValueOfValue(HAPWithUIId.UIID);   }
	public void setUIId(String uiId) {    this.setAttributeValueWithValue(HAPWithUIId.UIID, uiId);    }
	
	public HAPManualDefinitionBlockComplexUIContent getUIContent() {    return (HAPManualDefinitionBlockComplexUIContent)this.getAttributeValueOfBrick(HAPWithUIContent.UICONTENT);  }
	public void setUIContent(HAPManualDefinitionBlockComplexUIContent uiContent) {   this.setAttributeValueWithBrick(HAPWithUIContent.UICONTENT, uiContent);     }

}
