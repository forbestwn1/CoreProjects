package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueContext;

//entity that have data value structure and attachment
public abstract class HAPManualBrickBlockComplex extends HAPManualBrickBlock{

	static private String VALUECONTEXT = "valueContext"; 
	
	protected HAPManualBrickBlockComplex (HAPIdBrickType entityType) {
		super(entityType);
	}
	
	
	public HAPManualBrickValueContext getValueContextBrick() {    return (HAPManualBrickValueContext)this.getAttributeValueWithBrick(VALUECONTEXT);       }

	public void setValueContextEntity(HAPManualBrickValueContext valueContext) {}

//	public HAPManualBrickValueContext getValueContextBrick(HAPContextParser parserContext) {  return (HAPManualBrickValueContext)this.getAttributeValueEntity(HAPWithValueContext.VALUECONTEXT, parserContext);  }  
	
//	public HAPIdEntityInDomain getAttachmentContainerEntity() {  return (HAPIdEntityInDomain)this.getAttributeValue(HAPWithAttachment.ATTACHMENT);  }
//	public HAPDefinitionEntityContainerAttachment getAttachmentEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityContainerAttachment)this.getAttributeValueEntity(HAPWithAttachment.ATTACHMENT, parserContext);  }
//
//	public void setAttachmentContainerEntity(HAPIdEntityInDomain attachmentEntity) {    this.setAttributeValueSimple(HAPWithAttachment.ATTACHMENT, attachmentEntity);   }

//	public HAPIdEntityInDomain getDataExpressionGroup() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplex.DATAEEXPRESSIONGROUP);     }
//	public HAPDefinitionEntityExpressionDataGroup getDataExpressionGroupEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityExpressionDataGroup)parserContext.getGlobalDomain().getEntityInfoDefinition(getDataExpressionGroup()).getEntity();     }
//
//	public HAPIdEntityInDomain getScriptExpressionGroup() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplex.SCRIPTEEXPRESSIONGROUP);     }
//	public HAPDefinitionEntityExpressionScriptGroup getScriptExpressionGroupEntity(HAPContextParser parserContext) {		return (HAPDefinitionEntityExpressionScriptGroup)this.getAttributeValueEntity(HAPExecutableEntityComplex.SCRIPTEEXPRESSIONGROUP, parserContext);	}
//	
//	public HAPIdEntityInDomain getPlainScriptExpressionGroup() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP);     }
//	public HAPDefinitionEntityExpressionScriptGroup getPlainScriptExpressionGroupEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityExpressionScriptGroup)parserContext.getGlobalDomain().getEntityInfoDefinition(getPlainScriptExpressionGroup()).getEntity();     }

}
