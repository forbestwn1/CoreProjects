package com.nosliw.data.core.complex;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorValueStructure;

public class HAPConfigureParentRelationComplex extends HAPSerializableImp{

	public static final String VALUESTRUCTURE = "valuestructure";
	public static final String ATTACHMENT = "attachment";
	
	private HAPConfigureProcessorValueStructure m_valueStructureConfigure;
	private HAPConfigureComplexRelationAttachment m_attachmentMode;
	private HAPConfigureComplexRelationInfo m_infoMode;
	
	public HAPConfigureParentRelationComplex(){
		this.m_attachmentMode = new HAPConfigureComplexRelationAttachment();
		this.m_valueStructureConfigure = new HAPConfigureProcessorValueStructure();
	}
	
	//attachment merge
	public HAPConfigureComplexRelationAttachment getAttachmentRelationMode() {   return this.m_attachmentMode;    }
	public void setAttachmentRelationMode(HAPConfigureComplexRelationAttachment attachmentRelationConfigure) {   this.m_attachmentMode = attachmentRelationConfigure;   }
	
	//info merge
	public HAPConfigureComplexRelationInfo getInfoRelationMode() {    return this.m_infoMode;    }
	public void setInfoRelationMode(HAPConfigureComplexRelationInfo infoRelationConfigure) {    this.m_infoMode = infoRelationConfigure;     }
	
	public HAPConfigureProcessorValueStructure getValueStructureRelationMode() {   return this.m_valueStructureConfigure;    }
	public void setValueStructureRelationMode(HAPConfigureProcessorValueStructure valueStructureConfigure) {    this.m_valueStructureConfigure = valueStructureConfigure;    }
	
	public void mergeHard(HAPConfigureParentRelationComplex configure) {
		if(configure!=null) {
			this.m_attachmentMode.mergeHard(configure.getAttachmentRelationMode());
			this.m_valueStructureConfigure.mergeHard(configure.getValueStructureRelationMode());
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ATTACHMENT, this.m_attachmentMode.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VALUESTRUCTURE, this.m_valueStructureConfigure.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_attachmentMode.buildObject(jsonObj.opt(ATTACHMENT), HAPSerializationFormat.JSON);
		this.m_valueStructureConfigure.buildObject(jsonObj.opt(VALUESTRUCTURE), HAPSerializationFormat.JSON);
		return true;  
	}

}
