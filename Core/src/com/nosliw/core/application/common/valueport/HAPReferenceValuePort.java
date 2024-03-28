package com.nosliw.core.application.common.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPReferenceBrick;

@HAPEntityWithAttribute
public class HAPReferenceValuePort extends HAPSerializableImp{

	@HAPAttribute
	public static final String BRICKREFERENCE = "brickReference";

	@HAPAttribute
	public static final String VALUEPORTKEY = "valuePortKey";

	private HAPReferenceBrick m_brickReference;
	
	private HAPIdValuePort m_valuePortKey;
	
	public HAPReferenceValuePort() {}
	
	public HAPReferenceValuePort(HAPReferenceBrick brickRef, HAPIdValuePort valuePortKey) {
		this.m_brickReference = brickRef;
		this.m_valuePortKey = valuePortKey;
	}
	
	//which entity this value port belong
	public HAPReferenceBrick getBrickReference() {    return this.m_brickReference;     }
	
	public HAPIdValuePort getValuePortId() {   return this.m_valuePortKey;     }

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		if(this.m_brickReference!=null) {
			jsonMap.put(BRICKREFERENCE, this.m_brickReference.toStringValue(HAPSerializationFormat.JSON));
		}
		if(this.m_valuePortKey!=null) {
			jsonMap.put(VALUEPORTKEY, this.m_valuePortKey.toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
