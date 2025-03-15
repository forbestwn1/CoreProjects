package com.nosliw.core.application.common.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;

@HAPEntityWithAttribute
public class HAPDataAssociationForTask extends HAPSerializableImp{

	@HAPAttribute
	public static String OUT = "out";

	@HAPAttribute
	public static String IN = "in";

	private HAPDataAssociation m_inDataAssociation;
	
	//data association from process to external
	private Map<String, HAPDataAssociation> m_outDataAssociation;

	public HAPDataAssociationForTask() {
		this.m_outDataAssociation = new LinkedHashMap<String, HAPDataAssociation>();
	}

	public HAPDataAssociation getInDataAssociation() {   return this.m_inDataAssociation;   }
	public void setInDataAssociation(HAPDataAssociation inDataAssociation) {    this.m_inDataAssociation = inDataAssociation;    }
	
	public Map<String, HAPDataAssociation> getOutDataAssociations(){    return this.m_outDataAssociation;     }
	public void setOutDataAssociations(Map<String, HAPDataAssociation> outDataAssociations) {  if(outDataAssociations!=null) {
		this.m_outDataAssociation.putAll(outDataAssociations);
	}   }
	public void addOutDataAssociation(String name, HAPDataAssociation dataAssociation) {  this.m_outDataAssociation.put(name, dataAssociation);   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_inDataAssociation!=null) {
			jsonMap.put(IN, this.m_inDataAssociation.toStringValue(HAPSerializationFormat.JSON));
		}
		
		Map<String, String> outJsonStr = new LinkedHashMap<String, String>(); 
		for(String outName : this.m_outDataAssociation.keySet()) {
			outJsonStr.put(outName, this.m_outDataAssociation.get(outName).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(OUT, HAPUtilityJson.buildMapJson(outJsonStr));
	}
}
