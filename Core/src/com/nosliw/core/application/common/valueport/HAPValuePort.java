package com.nosliw.core.application.common.valueport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPValuePort extends HAPEntityInfoImp{

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String IODIRECTION = "ioDirection";

	private List<HAPInfoValueStructure> m_valueStructures;

	private String m_type;
	
	private String m_ioDirection; 

	public HAPValuePort(String type, String ioDirection) {
		this.m_type = type;
		this.m_ioDirection = ioDirection;
		this.m_valueStructures = new ArrayList<HAPInfoValueStructure>();
	}
	
	public List<String> getValueStructureIds(){	return this.m_valueStructures.stream().map(HAPInfoValueStructure::getValueStructureId).collect(Collectors.toList());	}
	public void addValueStructureId(String id) {    this.addValueStructureId(id, HAPConstantShared.VALUESTRUCTURE_PRIORITY_DEFINED);    }
	public void addValueStructureId(String id, int priority) {
		this.m_valueStructures.add(new HAPInfoValueStructure(id, priority));
		this.m_valueStructures.sort((v1, v2)->v2.getPriority()-v1.getPriority());
	}

	public String getType() {     return this.m_type;    }

	public String getIODirection() {     return this.m_ioDirection;     }
	public void setIODirection(String ioDirection) {   this.m_ioDirection = ioDirection;   }

	public void cleanValueStucture(Set<String> valueStrucutreIds) {
		this.m_valueStructures =  this.m_valueStructures.stream().filter(vsInfo->!valueStrucutreIds.contains(vsInfo.getValueStructureId())).collect(Collectors.toList());
	}

	public boolean isEmpty() {
		return this.m_valueStructures.isEmpty();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(IODIRECTION, this.getIODirection());
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildArrayJson(this.getValueStructureIds().toArray(new String[0])));
	}

	class HAPInfoValueStructure{
		
		private String m_valueStructureId;
		
		private int m_priority;
		
		public HAPInfoValueStructure(String valueStructureId, int priority) {
			this.m_valueStructureId = valueStructureId;
			this.m_priority = priority;
		}
		
		public String getValueStructureId() {    return this.m_valueStructureId;     }
		
		public int getPriority() {   return this.m_priority;    }
		
	}
}
