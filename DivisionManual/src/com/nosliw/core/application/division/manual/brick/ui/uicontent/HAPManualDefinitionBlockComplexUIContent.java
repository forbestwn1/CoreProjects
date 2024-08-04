package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUIContent;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEmbededScriptExpressionInContent;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainerList;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualDefinitionContainerScriptExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueBrick;

public class HAPManualDefinitionBlockComplexUIContent extends HAPManualDefinitionBrickBlockComplex{

	private static final String ID_INDEX = "idIndex";
	
	public HAPManualDefinitionBlockComplexUIContent() {
		super(HAPEnumBrickType.UICONTENT_100);
		this.setAttributeWithValueValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONS, new HAPManualDefinitionContainerScriptExpression());
		this.setAttributeWithValueValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCONTENT, new ArrayList<HAPUIEmbededScriptExpressionInContent>());
		this.setAttributeWithValueValue(ID_INDEX, new Integer(0));
	}

	@Override
	public void init() {
		this.setAttributeWithValueBrick(HAPBlockComplexUIContent.CUSTOMERTAG, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINERLIST_100));
	}
	
	public void addCustomerTag(HAPManualDefinitionBlockComplexUICustomerTag customerTag) {
		HAPManualDefinitionBrickContainerList container = this.getCustomerTagContainer();
		HAPManualDefinitionAttributeInBrick attr = new HAPManualDefinitionAttributeInBrick();
		attr.setValueWrapper(new HAPManualDefinitionWrapperValueBrick(customerTag));
		
		for(HAPManualDefinitionBrickRelation relation : customerTag.getParentRelations()) {
			attr.addRelation(relation);
		}
		container.addElement(attr);
	}
	
	private HAPManualDefinitionBrickContainerList getCustomerTagContainer() {
		return (HAPManualDefinitionBrickContainerList)this.getAttributeValueWithBrick(HAPBlockComplexUIContent.CUSTOMERTAG);
	}

	public String getHtml() {    return (String)this.getAttributeValueWithValue(HAPBlockComplexUIContent.HTML);  }
	public void setHtml(String html) {    this.setAttributeWithValueValue(HAPBlockComplexUIContent.HTML, StringEscapeUtils.escapeHtml(html).replaceAll("(\\r|\\n)", ""));      }
	
	public List<HAPUIEmbededScriptExpressionInContent> getScriptExpressionInContent(){   return (List<HAPUIEmbededScriptExpressionInContent>)this.getAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONINCONTENT);       }
	public void addScriptExpressionInContent(HAPUIEmbededScriptExpressionInContent scriptExpressionInContent) {   this.getScriptExpressionInContent().add(scriptExpressionInContent);    }
	
	public HAPManualDefinitionContainerScriptExpression getScriptExpressions() {    return (HAPManualDefinitionContainerScriptExpression)this.getAttributeValueWithValue(HAPBlockComplexUIContent.SCRIPTEXPRESSIONS);      }

	
	@Override
	public String generateId() {
		int idIndex = (Integer)this.getAttributeValueWithValue(ID_INDEX);
		this.setAttributeWithValueValue(ID_INDEX, new Integer(idIndex++));
		return idIndex+"";
	}
	
}
