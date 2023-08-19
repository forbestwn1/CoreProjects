package com.nosliw.ui.entity.uicontent;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

public class HAPExecutableEntityComplexUIContent extends HAPExecutableEntityComplex{

	static final public String ATTR_CONTENT = "content";  

	static final public String ATTR_SCRIPT = "script";  
	
	static final public String ATTR_CUSTOMERTAG = "tag";  
	
	static final public String ATTR_SCRIPTEXPRESSIONINCONTENT = "scriptExpressionInContent";
	
	static final public String ATTR_SCRIPTEXPRESSIONINATTRIBUTE = "scriptExpressionInAttribute";
	
	static final public String ATTR_SCRIPTEXPRESSIONINTAGATTRIBUTE = "scriptExpressionInTagAttribute";

	static final public String ATTR_STYPE = "style";  
	
	static final public String ATTR_NORMALTAGEVENT = "normalTagEvent";  
	
	static final public String ATTR_CUSTOMTAGEVENT = "customTagEvent";  
	
	
	static final public String ATTR_SCRIPTEEXPRESSIONGROUP = "scriptExpressionGroup";  


	public String getUnitType() {    return null;   }

	public void addCustomTag(HAPIdEntityInDomain customTagId) {}
	
	public void setContent(String content) {    this.setAttributeValueObject(ATTR_CONTENT, content);        }
	public String getContent() {     return (String)this.getAttributeValue(ATTR_CONTENT);     }
	
	public void setJSBlock(HAPJsonTypeScript jsBlock){    this.setAttributeValueObject(ATTR_SCRIPT, jsBlock);        }

	public void addNormalTagEvent(HAPElementEvent event) {       }
	public void addCustomTagEvent(HAPElementEvent event) {       }
	
	public void addScriptExpressionInCustomTagAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute scriptExpression) {     }
	public void addScriptExpressionInNormalTagAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute scriptExpression) {}
	public void addScriptExpressionInContent(HAPDefinitionUIEmbededScriptExpressionInContent scriptExpression) {}
	
	public void setStyle(HAPDefinitionStyle style) {}
	
	public void addAttribute(String attrName, String attrValue) {}
	
}
