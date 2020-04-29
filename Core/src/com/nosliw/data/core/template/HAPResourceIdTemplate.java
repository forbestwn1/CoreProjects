package com.nosliw.data.core.template;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdTemplate  extends HAPResourceIdSimple{

	private HAPTemplateId m_templateId; 
	
	public HAPResourceIdTemplate(){  super(HAPConstant.RUNTIME_RESOURCE_TYPE_TEMPLATE);    }

	public HAPResourceIdTemplate(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdTemplate(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdTemplate(HAPTemplateId templateId){
		this();
		init(null, null);
		this.m_templateId = templateId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(templateId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_templateId = new HAPTemplateId(id);
	}

	public HAPTemplateId getTemplateId(){  return this.m_templateId;	}
	
	@Override
	public HAPResourceIdTemplate clone(){
		HAPResourceIdTemplate out = new HAPResourceIdTemplate();
		out.cloneFrom(this);
		return out;
	}

}
