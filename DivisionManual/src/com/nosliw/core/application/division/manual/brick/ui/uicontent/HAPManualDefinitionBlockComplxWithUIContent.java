package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUIContent;
import com.nosliw.core.application.brick.ui.uicontent.HAPWithUIContent;
import com.nosliw.core.application.brick.ui.uicontent.HAPWithUIId;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainerList;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockComplex;

public class HAPManualDefinitionBlockComplxWithUIContent extends HAPManualDefinitionBrickBlockComplex implements HAPWithUIId{

	protected HAPManualDefinitionBlockComplxWithUIContent(HAPIdBrickType entityType) {
		super(entityType);
		
	}

	@Override
	protected void init() {
		this.setAttributeWithValueBrick(HAPBlockComplexUIContent.CUSTOMERTAG, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINERLIST_100));
	}

	@Override
	public String getUIId() {   return (String)this.getAttributeValueWithValue(HAPWithUIId.UIID);   }
	public String setUIId(String uiId) {    return (String)this.getAttributeValueWithValue(HAPWithUIId.UIID);    }
	
	public HAPManualDefinitionBlockComplexUIContent getUIContent() {    return (HAPManualDefinitionBlockComplexUIContent)this.getAttributeValueWithBrick(HAPWithUIContent.UICONTENT);  }
	public void setUIContent(HAPManualDefinitionBlockComplexUIContent uiContent) {   this.setAttributeWithValueBrick(HAPWithUIContent.UICONTENT, uiContent);     }
	
	public void addCustomerTag(HAPManualDefinitionBlockComplexUICustomerTag customerTag) {    this.getCustomerTagContainer().addElement(customerTag);    }
	
	private HAPManualDefinitionBrickContainerList getCustomerTagContainer() {
		return (HAPManualDefinitionBrickContainerList)this.getAttributeValueWithBrick(HAPBlockComplexUIContent.CUSTOMERTAG);
	}


}
