package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUICustomerTag;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualBlockComplexUICustomerTag extends HAPManualBrickImp implements HAPBlockComplexUICustomerTag{

	public String getUITagId() {   return (String)this.getAttributeValueOfValue(UITAGID);  }
	public void setUITagId(String uiTagId) {    this.setAttributeValueWithValue(UITAGID, uiTagId);      }

	@Override
	public String getUIId() {   return (String)this.getAttributeValueOfValue(UIID);  }
	public void setUIId(String uiId) {    this.setAttributeValueWithValue(UIID, uiId);      }

	public String getBase() {   return (String)this.getAttributeValueOfValue(BASE);    }
	public void setBase(String base) {     this.setAttributeValueWithValue(BASE, base);      }
	
	public HAPResourceId getScriptResourceId(){    return (HAPResourceId)this.getAttributeValueOfValue(SCRIPTRESOURCEID);      }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(SCRIPTRESOURCEID, resourceId);      }

}
