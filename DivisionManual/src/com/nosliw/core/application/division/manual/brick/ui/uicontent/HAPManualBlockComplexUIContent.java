package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUIContent;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEmbededScriptExpressionInContent;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEventHandlerInfoNormal;
import com.nosliw.core.application.common.scriptexpressio.HAPContainerScriptExpression;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;

public class HAPManualBlockComplexUIContent extends HAPManualBrickImp implements HAPBlockComplexUIContent{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCONTENT, new ArrayList<HAPUIEmbededScriptExpressionInContent>());
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINNORMALTAGATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCUSTOMERTAGATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());

		this.setAttributeValueWithValue(HAPBlockComplexUIContent.NORMALTAGEVENT, new ArrayList<HAPUIEventHandlerInfoNormal>());
		
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
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInCustomerTagAttribute() {return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCUSTOMERTAGATTRIBUTE);  }

	@Override
	public HAPContainerScriptExpression getScriptExpressions() {   return (HAPContainerScriptExpression)this.getAttributeValueOfValue(SCRIPTEXPRESSIONS);  }

	@Override
	public List<HAPUIEventHandlerInfoNormal> getNormalTagEvents(){    return (List<HAPUIEventHandlerInfoNormal>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.NORMALTAGEVENT);       }
	public void addNormalTagEvent(HAPUIEventHandlerInfoNormal event) {    this.getNormalTagEvents().add(event);     }

	
}
