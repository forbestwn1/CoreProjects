package com.nosliw.ui.entity.uicontent;

import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public class HAPDefinitionEntityComplexUIContent extends HAPDefinitionEntityInDomainComplex{

	static final public String ATTR_CONTENT = "content";  

	static final public String ATTR_SCRIPT = "script";  
	
	static final public String ATTR_CUSTOMERTAG = "tag";  
	
	static final public String ATTR_SCRIPTEXPRESSIONINCONTENT = "scriptExpressionInContent";
	
	static final public String ATTR_SCRIPTEXPRESSIONINATTRIBUTE = "scriptExpressionInAttribute";
	
	static final public String ATTR_SCRIPTEXPRESSIONINTAGATTRIBUTE = "scriptExpressionInTagAttribute";

	static final public String ATTR_STYPE = "style";  
	
	static final public String ATTR_NORMALTAGEVENT = "normalTagEvent";  
	
	static final public String ATTR_CUSTOMTAGEVENT = "customTagEvent";  
	
	static final public String ATTR_ATTRIBUTE = "attribute";  
	

	public String getUnitType() {    return null;   }

	public void addCustomTag(HAPIdEntityInDomain customTagId, HAPConfigureParentRelationComplex relationConfigure) {}
	
	public void setContent(String content) {    this.setAttributeValueObject(ATTR_CONTENT, content);        }
	public String getContent() {     return (String)this.getAttributeValue(ATTR_CONTENT);     }
	
	public void setJSBlock(HAPJsonTypeScript jsBlock){    this.setAttributeValueObject(ATTR_SCRIPT, jsBlock);        }

	public void addNormalTagEvent(HAPElementEvent event) {   ((List<HAPElementEvent>)this.getAttributeValue(ATTR_NORMALTAGEVENT)).add(event);    }
	public void addCustomTagEvent(HAPElementEvent event) {   ((List<HAPElementEvent>)this.getAttributeValue(ATTR_CUSTOMTAGEVENT)).add(event);    }
	
	public void addScriptExpressionInCustomTagAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute scriptExpression) {  ((List<HAPDefinitionUIEmbededScriptExpressionInAttribute>)this.getAttributeValue(ATTR_SCRIPTEXPRESSIONINTAGATTRIBUTE)).add(scriptExpression);    }
	public void addScriptExpressionInNormalTagAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute scriptExpression) {  ((List<HAPDefinitionUIEmbededScriptExpressionInAttribute>)this.getAttributeValue(ATTR_SCRIPTEXPRESSIONINATTRIBUTE)).add(scriptExpression);    }
	public void addScriptExpressionInContent(HAPDefinitionUIEmbededScriptExpressionInContent scriptExpression) {  ((List<HAPDefinitionUIEmbededScriptExpressionInContent>)this.getAttributeValue(ATTR_SCRIPTEXPRESSIONINCONTENT)).add(scriptExpression);    }
	 

	public void setStyle(HAPDefinitionStyle style) {}
	
	public void addAttribute(String attrName, String attrValue) {    ((Map<String, String>)this.getAttributeValue(ATTR_ATTRIBUTE)).put(attrName, attrValue);    }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexUIContent out = new HAPDefinitionEntityComplexUIContent();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
