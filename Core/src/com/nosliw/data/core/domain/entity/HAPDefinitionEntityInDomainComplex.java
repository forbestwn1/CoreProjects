package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionEntityExpressionScriptGroup;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;

//entity that have data value structure and attachment
public abstract class HAPDefinitionEntityInDomainComplex extends HAPDefinitionEntityInDomain implements HAPWithValueContext, HAPWithAttachment{

	protected HAPDefinitionEntityInDomainComplex() {}
	
	protected HAPDefinitionEntityInDomainComplex (String entityType) {
		super(entityType);
	}
	
	@Override
	public HAPIdEntityInDomain getValueContextEntityId() {  	return (HAPIdEntityInDomain)this.getAttributeValue(HAPWithValueContext.VALUECONTEXT);	}
	public HAPDefinitionEntityValueContext getValueContextEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityValueContext)parserContext.getGlobalDomain().getEntityInfoDefinition(this.getValueContextEntityId()).getEntity();      }
	
	@Override
	public void setValueContextEntityId(HAPIdEntityInDomain valueContextEntity) {		this.setAttributeValueSimple(HAPWithValueContext.VALUECONTEXT, valueContextEntity);      }

	@Override
	public HAPIdEntityInDomain getAttachmentContainerEntity() {  return (HAPIdEntityInDomain)this.getAttributeValue(HAPWithAttachment.ATTACHMENT);  }
	public HAPDefinitionEntityContainerAttachment getAttachmentEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityContainerAttachment)parserContext.getGlobalDomain().getEntityInfoDefinition(this.getAttachmentContainerEntity()).getEntity();      }

	@Override
	public void setAttachmentContainerEntity(HAPIdEntityInDomain attachmentEntity) {    this.setAttributeValueSimple(HAPWithAttachment.ATTACHMENT, attachmentEntity);   }

	public HAPIdEntityInDomain getDataExpressionGroup() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplex.DATAEEXPRESSIONGROUP);     }
	public HAPDefinitionEntityExpressionDataGroup getDataExpressionGroupEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityExpressionDataGroup)parserContext.getGlobalDomain().getEntityInfoDefinition(getDataExpressionGroup()).getEntity();     }

	public HAPIdEntityInDomain getScriptExpressionGroup() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplex.DATAEEXPRESSIONGROUP);     }
	public HAPDefinitionEntityExpressionScriptGroup getScriptExpressionGroupEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityExpressionScriptGroup)parserContext.getGlobalDomain().getEntityInfoDefinition(getScriptExpressionGroup()).getEntity();     }
	
	public HAPIdEntityInDomain getPlainScriptExpressionGroup() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP);     }
	public HAPDefinitionEntityExpressionScriptGroup getPlainScriptExpressionGroupEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityExpressionScriptGroup)parserContext.getGlobalDomain().getEntityInfoDefinition(getPlainScriptExpressionGroup()).getEntity();     }

}
