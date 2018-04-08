package com.nosliw.data.core;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

public class HAPRelationshipImp extends HAPSerializableImp implements HAPRelationship{

	private HAPDataTypeId m_source;
	private HAPDataTypeId m_taget;
	private HAPRelationshipPath m_path;
	
	public HAPRelationshipImp(HAPDataTypeId source, HAPDataTypeId target, HAPRelationshipPath path) {
		this.m_source = source;
		this.m_taget = target;
		this.m_path = path;
	}
	
	@Override
	public HAPDataTypeId getTarget() {  return this.m_taget;  }

	@Override
	public HAPDataTypeId getSource() {  return this.m_source;  }

	@Override
	public HAPRelationshipPath getPath() {  return this.m_path;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SOURCE, HAPSerializeManager.getInstance().toStringValue(this.getSource(), HAPSerializationFormat.LITERATE));
		jsonMap.put(TARGET, HAPSerializeManager.getInstance().toStringValue(this.getTarget(), HAPSerializationFormat.LITERATE));
		jsonMap.put(PATH, HAPSerializeManager.getInstance().toStringValue(this.getPath(), HAPSerializationFormat.LITERATE));
	}

}
