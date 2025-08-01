package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.ui.uicontent.HAPWithUIContent;
import com.nosliw.core.xxx.application1.brick.ui.uicontent.HAPWithUIId;

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
