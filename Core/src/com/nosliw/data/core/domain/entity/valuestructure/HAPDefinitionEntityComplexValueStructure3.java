package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocal;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPDefinitionEntityComplexValueStructure3 extends HAPDefinitionEntityInDomainSimple{

	public static final String PART = "part";
	
	private int m_idIndex;
	
	private List<HAPExecutablePartValueContext> m_parts;
	
	private HAPGeneratorId m_idGenerator;
	 
	public HAPDefinitionEntityComplexValueStructure3() {
		this.m_parts = new ArrayList<HAPExecutablePartValueContext>();
	}
	
	public List<HAPExecutablePartValueContext> getParts(){   return this.m_parts;  }
	
	public List<HAPExecutablePartValueContext> getPart(String name) {
		List<HAPExecutablePartValueContext> out = new ArrayList<HAPExecutablePartValueContext>();
		for(int i : this.findPartByName(name)) {
			out.add(this.m_parts.get(i));
		}
		return out;
	}

	public void addPartSimple(HAPValueStructure valueStructure, HAPInfoPartValueStructure partInfo) {
		partInfo.setId(this.generateId(partInfo));
		HAPExecutablePartValueContextSimple part = new HAPExecutablePartValueContextSimple(valueStructure.cloneValueStructure(), partInfo);
		this.addPart(part);
	}
	
	public void addPartGroup(List<HAPExecutablePartValueContext> children, HAPInfoPartValueStructure partInfo) {
		partInfo.setId(this.generateId(partInfo));
		HAPExecutablePartValueContextGroupWithEntity part = new HAPExecutablePartValueContextGroupWithEntity(partInfo);
		for(HAPExecutablePartValueContext child : children) {
			part.addChild(child.cloneValueContextPart());
		}
		this.addPart(part);
	}
	
	public void addPart(HAPExecutablePartValueContext part) {
		this.m_parts.add(part);
		HAPUtilityValueContext.sortParts(m_parts);
	}
	
	public void copyPart(HAPExecutablePartValueContext part) {
		this.m_parts.add(part);
		HAPUtilityValueContext.sortParts(m_parts);
	}
	
	public HAPDefinitionEntityComplexValueStructure3 cloneValueStructureComplex() {
		HAPDefinitionEntityComplexValueStructure3 out = new HAPDefinitionEntityComplexValueStructure3();
		for(HAPExecutablePartValueContext part : this.m_parts) {
			this.m_parts.add(part.cloneValueContextPart());
		}
		return out;
	}
	
	private List<Integer> findPartByName(String name) {
		List<Integer> out = new ArrayList<Integer>();
		for(int i=0; i<this.m_parts.size(); i++) {
			HAPExecutablePartValueContext part = this.m_parts.get(i);
			if(name.equals(part.getName())) {
				out.add(i);
			}
		}
		return out;
	}
	
	private String generateId(HAPInfoPartValueStructure partInfo) {
		return partInfo.getName() + "_" + this.m_idIndex++;
	}

	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityDefinitionLocal entityDefDomain){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> partJsonArray = new ArrayList<String>();
		for(HAPExecutablePartValueContext part : this.m_parts) {
			partJsonArray.add(part.toExpandedJsonString(entityDefDomain));
		}
		jsonMap.put(PART, HAPUtilityJson.buildArrayJson(partJsonArray.toArray(new String[0])));
	}

}
