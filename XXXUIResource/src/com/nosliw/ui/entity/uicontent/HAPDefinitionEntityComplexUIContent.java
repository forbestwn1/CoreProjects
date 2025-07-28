package com.nosliw.ui.entity.uicontent;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.container.HAPUtilityEntityContainer;

public class HAPDefinitionEntityComplexUIContent extends HAPManualBrickComplex{

	static final public String ATTR_STYPE = "style";  
	
	public HAPDefinitionEntityComplexUIContent() {
	}
	
	public String getUnitType() {    return null;   }

	public void addCustomTag(HAPIdEntityInDomain customTagId, HAPConfigureParentRelationComplex relationConfigure, HAPContextParser parserContext) {
		HAPUtilityEntityContainer.addComplexElementAttribute(this.getAttributeValueEntityId(HAPExecutableEntityComplexUIContent.CUSTOMERTAG), customTagId, relationConfigure, parserContext);
	}
	
	public void setHtml(String html) {
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.HTML, StringEscapeUtils.escapeHtml(html).replaceAll("(\\r|\\n)", ""));        
	}
	public String getHtml() {     return (String)this.getAttributeValueOfValue(HAPExecutableEntityComplexUIContent.HTML);     }
	
	public void addNormalTagEvent(HAPElementEvent event) {   this.getNormalTagEvents().add(event);    }
	public void addCustomTagEvent(HAPElementEvent event) {   this.getCustomTagEvent().add(event);    }
	
	public List<HAPElementEvent> getNormalTagEvents(){    return (List<HAPElementEvent>)this.getAttributeValueOfValue(HAPExecutableEntityComplexUIContent.NORMALTAGEVENT);     }
	public List<HAPElementEvent> getCustomTagEvent(){    return (List<HAPElementEvent>)this.getAttributeValueOfValue(HAPExecutableEntityComplexUIContent.CUSTOMTAGEVENT);     }
	
	public void addScriptExpressionInCustomTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpression) {  getScriptExpressionInCustomTagAttributes().add(scriptExpression);    }
	public void addScriptExpressionInNormalTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpression) {  getScriptExpressionInNormalTagAttributes().add(scriptExpression);    }
	public void addScriptExpressionInContent(HAPUIEmbededScriptExpressionInContent scriptExpression) {  getScriptExpressionInContents().add(scriptExpression);    }
	 
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInCustomTagAttributes() {  return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValueOfValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINTAGATTRIBUTE);    }
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInNormalTagAttributes() {  return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValueOfValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINATTRIBUTE);    }
	public List<HAPUIEmbededScriptExpressionInContent> getScriptExpressionInContents() {  return (List<HAPUIEmbededScriptExpressionInContent>)this.getAttributeValueOfValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINCONTENT);    }
	
	public void setStyle(HAPDefinitionStyle style) {}
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexUIContent out = new HAPDefinitionEntityComplexUIContent();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
