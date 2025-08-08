package com.nosliw.core.xxx.application.common.dataassociation;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;

@HAPEntityWithAttribute
public class HAPDataAssociationForExpression extends HAPSerializableImp{

	@HAPAttribute
	public static String IN = "in";

	@HAPAttribute
	public static String OUT = "out";

	private HAPDataAssociation m_inDataAssociation;
	
	//data association from process to external
	private HAPDataAssociation m_outDataAssociation;

	public HAPDataAssociationForExpression() {
	}

	public HAPDataAssociation getInDataAssociation() {   return this.m_inDataAssociation;   }
	public void setInDataAssociation(HAPDataAssociation inDataAssociation) {    this.m_inDataAssociation = inDataAssociation;    }
	
	public HAPDataAssociation getOutDataAssociation(){    return this.m_outDataAssociation;     }
	public void setOutDataAssociation(HAPDataAssociation outDataAssociation) {  this.m_outDataAssociation = outDataAssociation;  } 

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(IN, this.m_inDataAssociation.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(OUT, this.m_outDataAssociation.toStringValue(HAPSerializationFormat.JSON));
	}
}
