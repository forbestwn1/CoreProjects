package com.nosliw.data.core.complex;

import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;
import com.nosliw.data.core.complex.valuestructure.HAPPartComplexValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.component.HAPWithAttachmentImp;

//entity that have data definition and attachment, static information
public class HAPDefinitionEntityComplexImp extends HAPWithAttachmentImp implements HAPDefinitionEntityComplex{

	//value structure definition within this component
	private HAPComplexValueStructure m_valueStructureComplex;

	public HAPDefinitionEntityComplexImp () {
		this.m_valueStructureComplex = new HAPComplexValueStructure();
	}
	
	@Override
	public HAPComplexValueStructure getValueStructureComplex() {  return this.m_valueStructureComplex;  }

	@Override
	public String getValueStructureTypeIfNotDefined() {  return HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT;  }

	@Override
	public void cloneToComplexEntityDefinition(HAPDefinitionEntityComplex complexEntity, boolean cloneValueStructure) {
		this.cloneToEntityInfo(complexEntity);
		this.cloneToResourceDefinition(complexEntity);
		this.cloneToAttachment(complexEntity);

		if(cloneValueStructure) {
			for(HAPPartComplexValueStructure part : this.getValueStructureComplex().getParts()) {
				complexEntity.getValueStructureComplex().addPart(part);
			}
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildJson(this.m_valueStructureComplex, HAPSerializationFormat.JSON));
		jsonMap.put(HAPWithAttachment.ATTACHMENT, this.getAttachmentContainer().toStringValue(HAPSerializationFormat.JSON));
	}

}
