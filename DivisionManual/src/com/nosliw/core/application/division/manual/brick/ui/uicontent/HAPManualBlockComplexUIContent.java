package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUIContent;
import com.nosliw.core.application.brick.ui.uicontent.HAPElementEvent;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEmbededScriptExpressionInContent;
import com.nosliw.core.application.common.scriptexpressio.HAPContainerScriptExpression;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;

public class HAPManualBlockComplexUIContent extends HAPManualBrickImp implements HAPBlockComplexUIContent{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCONTENT, new ArrayList<HAPUIEmbededScriptExpressionInContent>());
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINNORMALTAGATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());

		this.setAttributeValueWithValue(HAPBlockComplexUIContent.NORMALTAGEVENT, new ArrayList<HAPElementEvent>());
		
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONS, new HAPContainerScriptExpression());
		
	}
	
	@Override
	public String getHtml() {    return (String)this.getAttributeValueOfValue(HAPBlockComplexUIContent.HTML);  }
	public void setHtml(String html) {    this.setAttributeValueWithValue(HTML, html);      }

	@Override
	public List<HAPUIEmbededScriptExpressionInContent> getScriptExpressionInContent() {    return (List<HAPUIEmbededScriptExpressionInContent>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCONTENT);  }

	@Override
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInNormalTagAttribute() {return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINNORMALTAGATTRIBUTE);  }

	@Override
	public HAPContainerScriptExpression getScriptExpressions() {   return (HAPContainerScriptExpression)this.getAttributeValueOfValue(SCRIPTEXPRESSIONS);  }

	@Override
	public List<HAPElementEvent> getNormalTagEvents(){    return (List<HAPElementEvent>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.NORMALTAGEVENT);       }
	public void addNormalTagEvent(HAPElementEvent event) {    this.getNormalTagEvents().add(event);     }

	@Override
	public List<HAPElementEvent> getCustomerTagEvents() {     return (List<HAPElementEvent>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.CUSTOMTAGEVENT);       }
	public void addCustomerTagEvent(HAPElementEvent event) {   this.getCustomerTagEvents().add(event);      }
	
}
