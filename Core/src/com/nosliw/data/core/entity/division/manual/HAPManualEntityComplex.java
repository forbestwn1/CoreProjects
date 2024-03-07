package com.nosliw.data.core.entity.division.manual;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionEntityExpressionScriptGroup;
import com.nosliw.data.core.domain.valueport.HAPDefinitionValuePort;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueContext;

//entity that have data value structure and attachment
public abstract class HAPManualEntityComplex extends HAPManualEntityBlock implements HAPWithValueContext, HAPWithAttachment{

	protected HAPManualEntityComplex (HAPIdEntityType entityType) {
		super(entityType);
	}
	
	@Override
	public Set<HAPDefinitionValuePort> getValuePorts(){
		Set<HAPDefinitionValuePort> out = new HashSet<HAPDefinitionValuePort>();
		out.add(this.getValueContextValuePort());
		out.addAll(this.getOtherValuePorts());
		return out;
	}
	protected HAPDefinitionValuePort getValueContextValuePort(){		return new HAPDefinitionValuePort(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT, HAPConstantShared.VALUEPORT_NAME_DEFAULT);	}
	protected Set<HAPDefinitionValuePort> getOtherValuePorts(){		return new HashSet<HAPDefinitionValuePort>();	}
	
	
	@Override
	public HAPDefinitionEntityValueContext getValueContextEntity() {}

	@Override
	public void setValueContextEntity(HAPDefinitionEntityValueContext valueContext) {}

	@Override
	public HAPIdEntityInDomain getValueContextEntityId() {  	return (HAPIdEntityInDomain)this.getAttributeValue(HAPWithValueContext.VALUECONTEXT);	}
	public HAPDefinitionEntityValueContext getValueContextEntity(HAPContextParser parserContext) {  return (HAPDefinitionEntityValueContext)this.getAttributeValueEntity(HAPWithValueContext.VALUECONTEXT, parserContext);  }  
	
	@Override
	public void setValueContextEntityId(HAPIdEntityInDomain valueContextEntity) {		
		this.setAttributeValueSimple(HAPWithValueContext.VALUECONTEXT, valueContextEntity);
		this.getAttribute(HAPWithValueContext.VALUECONTEXT).setAttributeAutoProcess(false);
	}

	@Override
	public HAPIdEntityInDomain getAttachmentContainerEntity() {  return (HAPIdEntityInDomain)this.getAttributeValue(HAPWithAttachment.ATTACHMENT);  }
	public HAPDefinitionEntityContainerAttachment getAttachmentEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityContainerAttachment)this.getAttributeValueEntity(HAPWithAttachment.ATTACHMENT, parserContext);  }

	@Override
	public void setAttachmentContainerEntity(HAPIdEntityInDomain attachmentEntity) {    this.setAttributeValueSimple(HAPWithAttachment.ATTACHMENT, attachmentEntity);   }

	public HAPIdEntityInDomain getDataExpressionGroup() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplex.DATAEEXPRESSIONGROUP);     }
	public HAPDefinitionEntityExpressionDataGroup getDataExpressionGroupEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityExpressionDataGroup)parserContext.getGlobalDomain().getEntityInfoDefinition(getDataExpressionGroup()).getEntity();     }

	public HAPIdEntityInDomain getScriptExpressionGroup() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplex.SCRIPTEEXPRESSIONGROUP);     }
	public HAPDefinitionEntityExpressionScriptGroup getScriptExpressionGroupEntity(HAPContextParser parserContext) {		return (HAPDefinitionEntityExpressionScriptGroup)this.getAttributeValueEntity(HAPExecutableEntityComplex.SCRIPTEEXPRESSIONGROUP, parserContext);	}
	
	public HAPIdEntityInDomain getPlainScriptExpressionGroup() {    return this.getAttributeValueEntityId(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP);     }
	public HAPDefinitionEntityExpressionScriptGroup getPlainScriptExpressionGroupEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityExpressionScriptGroup)parserContext.getGlobalDomain().getEntityInfoDefinition(getPlainScriptExpressionGroup()).getEntity();     }

}
