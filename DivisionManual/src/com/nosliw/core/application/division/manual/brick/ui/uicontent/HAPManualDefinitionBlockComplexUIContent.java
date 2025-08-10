package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUIContent;
import com.nosliw.core.application.brick.ui.uicontent.HAPElementEvent;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEmbededScriptExpressionInContent;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainerList;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueBrick;

public class HAPManualDefinitionBlockComplexUIContent extends HAPManualDefinitionBrick{

	private static final String ID_INDEX = "idIndex";
	
	public HAPManualDefinitionBlockComplexUIContent() {
		super(HAPEnumBrickType.UICONTENT_100);
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONS, new HAPDefinitionContainerScriptExpression());
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCONTENT, new ArrayList<HAPUIEmbededScriptExpressionInContent>());
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINNORMALTAGATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCUSTOMERTAGATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.NORMALTAGEVENT, new ArrayList<HAPElementEvent>());
		this.setAttributeValueWithValue(HAPBlockComplexUIContent.CUSTOMTAGEVENT, new ArrayList<HAPElementEvent>());
		this.setAttributeValueWithValue(ID_INDEX, new Integer(0));
	}

	@Override
	public void init() {
		this.setAttributeValueWithBrick(HAPBlockComplexUIContent.CUSTOMERTAG, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINERLIST_100));
	}
	
	public void addCustomerTag(HAPManualDefinitionBlockComplexUICustomerTag customerTag) {
		HAPManualDefinitionBrickContainerList container = this.getCustomerTagContainer();
		HAPManualDefinitionAttributeInBrick attr = new HAPManualDefinitionAttributeInBrick();
		attr.setValueWrapper(new HAPManualDefinitionWrapperValueBrick(customerTag));
		
		for(HAPManualDefinitionBrickRelation relation : customerTag.getParentRelations()) {
			attr.addRelation(relation);
		}
		container.addElementWithAttribute(attr);
	}
	
	private HAPManualDefinitionBrickContainerList getCustomerTagContainer() {
		return (HAPManualDefinitionBrickContainerList)this.getAttributeValueOfBrick(HAPBlockComplexUIContent.CUSTOMERTAG);
	}

	public String getHtml() {    return (String)this.getAttributeValueOfValue(HAPBlockComplexUIContent.HTML);  }
	public void setHtml(String html) {    this.setAttributeValueWithValue(HAPBlockComplexUIContent.HTML, StringEscapeUtils.escapeHtml(html).replaceAll("(\\r|\\n)", ""));      }
	
	public List<HAPUIEmbededScriptExpressionInContent> getScriptExpressionInContent(){   return (List<HAPUIEmbededScriptExpressionInContent>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCONTENT);       }
	public void addScriptExpressionInContent(HAPUIEmbededScriptExpressionInContent scriptExpressionInContent) {   this.getScriptExpressionInContent().add(scriptExpressionInContent);    }

	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInNormalTagAttribute(){   return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINNORMALTAGATTRIBUTE);       }
	public void addScriptExpressionInNormalTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute) {   this.getScriptExpressionInNormalTagAttribute().add(scriptExpressionInAttribute);    }
	
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInCustomerTagAttribute(){   return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCUSTOMERTAGATTRIBUTE);       }
	public void addScriptExpressionInCustomerTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute) {   this.getScriptExpressionInCustomerTagAttribute().add(scriptExpressionInAttribute);    }
	
	public HAPDefinitionContainerScriptExpression getScriptExpressions() {    return (HAPDefinitionContainerScriptExpression)this.getAttributeValueOfValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONS);      }

	public List<HAPElementEvent> getNormalTagEvents(){    return (List<HAPElementEvent>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.NORMALTAGEVENT);       }
	public void addNormalTagEvent(HAPElementEvent event) {    this.getNormalTagEvents().add(event);     }

	public List<HAPElementEvent> getCustomerTagEvents(){    return (List<HAPElementEvent>)this.getAttributeValueOfValue(HAPBlockComplexUIContent.CUSTOMERTAG);       }
	public void addCustomerTagEvent(HAPElementEvent event) {    this.getCustomerTagEvents().add(event);     }

	@Override
	public String generateId() {
		int idIndex = (Integer)this.getAttributeValueOfValue(ID_INDEX);
		idIndex++;
		this.setAttributeValueWithValue(ID_INDEX, new Integer(idIndex));
		return idIndex+"";
	}
	
}
