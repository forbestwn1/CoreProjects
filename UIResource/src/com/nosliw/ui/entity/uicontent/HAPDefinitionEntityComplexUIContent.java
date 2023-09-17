package com.nosliw.ui.entity.uicontent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public class HAPDefinitionEntityComplexUIContent extends HAPDefinitionEntityInDomainComplex{

	static final public String ATTR_CUSTOMERTAG = "tag";  
	
	static final public String ATTR_STYPE = "style";  
	
	static final public String ATTR_ATTRIBUTE = "attribute";  
	
	public HAPDefinitionEntityComplexUIContent() {
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINTAGATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINCONTENT, new ArrayList<HAPUIEmbededScriptExpressionInContent>());
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.NORMALTAGEVENT, new ArrayList<HAPElementEvent>());
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.CUSTOMTAGEVENT, new ArrayList<HAPElementEvent>());
	}
	
	public String getUnitType() {    return null;   }

	public void addCustomTag(HAPIdEntityInDomain customTagId, HAPConfigureParentRelationComplex relationConfigure) {}
	
	public void setHtml(String html) {
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.HTML, StringEscapeUtils.escapeHtml(html).replaceAll("(\\r|\\n)", ""));        
	}
	public String getHtml() {     return (String)this.getAttributeValue(HAPExecutableEntityComplexUIContent.HTML);     }
	
	public void setScriptBlock(HAPJsonTypeScript jsBlock){    this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPT, jsBlock);        }
	public HAPJsonTypeScript getScriptBlock() {   return (HAPJsonTypeScript)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPT);     }

	public void addNormalTagEvent(HAPElementEvent event) {   this.getNormalTagEvents().add(event);    }
	public void addCustomTagEvent(HAPElementEvent event) {   this.getCustomTagEvent().add(event);    }
	
	public List<HAPElementEvent> getNormalTagEvents(){    return (List<HAPElementEvent>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.NORMALTAGEVENT);     }
	public List<HAPElementEvent> getCustomTagEvent(){    return (List<HAPElementEvent>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.CUSTOMTAGEVENT);     }
	
	public void addScriptExpressionInCustomTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpression) {  getScriptExpressionInCustomTagAttributes().add(scriptExpression);    }
	public void addScriptExpressionInNormalTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpression) {  getScriptExpressionInNormalTagAttributes().add(scriptExpression);    }
	public void addScriptExpressionInContent(HAPUIEmbededScriptExpressionInContent scriptExpression) {  getScriptExpressionInContents().add(scriptExpression);    }
	 
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInCustomTagAttributes() {  return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINTAGATTRIBUTE);    }
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInNormalTagAttributes() {  return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINATTRIBUTE);    }
	public List<HAPUIEmbededScriptExpressionInContent> getScriptExpressionInContents() {  return (List<HAPUIEmbededScriptExpressionInContent>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINCONTENT);    }
	
	public void setStyle(HAPDefinitionStyle style) {}
	
	public void addAttribute(String attrName, String attrValue) {    ((Map<String, String>)this.getAttributeValue(ATTR_ATTRIBUTE)).put(attrName, attrValue);    }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexUIContent out = new HAPDefinitionEntityComplexUIContent();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
