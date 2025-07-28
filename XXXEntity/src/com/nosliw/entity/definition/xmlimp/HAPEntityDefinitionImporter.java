package com.nosliw.entity.definition.xmlimp;

import org.w3c.dom.Element;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;

public class HAPEntityDefinitionImporter extends HAPValueInfoManager{

	public HAPEntityDefinitionImporter(){
		this.init();
	}
	
	private void init(){
		this.registerValueInfo(HAPValueInfoImporterXML.importFromXML(HAPEntityDefinitionImporter.class.getResourceAsStream("valueInfo_entityDef.xml"), this));
	}
	
	public HAPEntityDefinitionCritical readEntityDefinition(Element ele){
		
		HAPEntityDefinitionCritical out = (HAPEntityDefinitionCritical)this.readEntityDefinition(ele);
		
		return out;
	}
}
