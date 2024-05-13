package com.nosliw.core.application.common.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPReferenceBrickLocal;

@HAPEntityWithAttribute
public class HAPReferenceValuePort extends HAPSerializableImp{

	@HAPAttribute
	public static final String BRICKREFERENCE = "brickReference";

	@HAPAttribute
	public static final String VALUEPORTKEY = "valuePortKey";

	private HAPReferenceBrickLocal m_brickReference;
	
	private HAPIdValuePort m_valuePortKey;
	
	public HAPReferenceValuePort() {}
	
	public HAPReferenceValuePort(HAPReferenceBrickLocal brickRef, HAPIdValuePort valuePortKey) {
		this.m_brickReference = brickRef;
		this.m_valuePortKey = valuePortKey;
	}
	
	//which entity this value port belong
	public HAPReferenceBrickLocal getBrickReference() {    return this.m_brickReference;     }
	public void setBlockReference(HAPReferenceBrickLocal blockRef) {     this.m_brickReference = blockRef;      }
	
	public HAPIdValuePort getValuePortId() {   return this.m_valuePortKey;     }
	public void setValuePortId(HAPIdValuePort valuePortId) {     this.m_valuePortKey = valuePortId;      }

	
	@Override
	public HAPReferenceValuePort cloneValue() {
		return new HAPReferenceValuePort(this.m_brickReference.cloneValue(), this.m_valuePortKey.cloneValue());
	}
	
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
