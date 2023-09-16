package com.nosliw.ui.entity.uicontent;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;


@HAPEntityWithAttribute
public class HAPExecutableEntityComplexUIContent extends  HAPExecutableEntityComplex{

	@HAPAttribute
	static final public String HTML = "html";  

	@HAPAttribute
	static final public String SCRIPTEXPRESSIONINCONTENT = "scriptExpressionInContent";
	
	@HAPAttribute
	static final public String SCRIPTEXPRESSIONINATTRIBUTE = "scriptExpressionInAttribute";
	
	@HAPAttribute
	static final public String SCRIPTEXPRESSIONINTAGATTRIBUTE = "scriptExpressionInTagAttribute";

	
	public void setHTML(String content) {    this.setAttributeValueObject(HTML, content);        }
	public String getHTML() {     return (String)this.getAttributeValue(HTML);     }
		
	public void addScriptExpressionInCustomTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpression) {  getScriptExpressionInCustomTagAttributes().add(scriptExpression);    }
	public void addScriptExpressionInNormalTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpression) {  getScriptExpressionInNormalTagAttributes().add(scriptExpression);    }
	public void addScriptExpressionInContent(HAPUIEmbededScriptExpressionInContent scriptExpression) {  getScriptExpressionInContents().add(scriptExpression);    }
	 
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInCustomTagAttributes() {  return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINTAGATTRIBUTE);    }
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInNormalTagAttributes() {  return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINATTRIBUTE);    }
	public List<HAPUIEmbededScriptExpressionInContent> getScriptExpressionInContents() {  return (List<HAPUIEmbededScriptExpressionInContent>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINCONTENT);    }

	
}
