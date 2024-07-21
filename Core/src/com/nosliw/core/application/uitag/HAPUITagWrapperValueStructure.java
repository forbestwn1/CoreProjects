package com.nosliw.core.application.uitag;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.structure.HAPValueStructureDefinition;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPUITagWrapperValueStructure implements HAPWrapperValueStructure{

	private HAPValueStructureDefinition m_valueStructure;
	
	private String m_name;

	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	private HAPInfoImpSimple m_info;
	
	@Override
	public HAPInfo getInfo() {  return this.m_info;  }

	@Override
	public HAPValueStructureDefinition getValueStructure() {   return  this.m_valueStructure;  } 

	@Override
	public String getName() {   return this.m_name;   }

	@Override
	public String getGroupType() {   return this.m_groupType;   }

	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,
			HAPParserDataExpression expressionParser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInfo(HAPInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueStructure(HAPValueStructureDefinition valueStructure) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGroupType(String groupType) {
		// TODO Auto-generated method stub
		
	}

}
