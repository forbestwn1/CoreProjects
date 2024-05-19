package com.nosliw.core.application.common.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPDataAssociationForTask extends HAPSerializableImp{

	public static String OUT = "out";

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

}
