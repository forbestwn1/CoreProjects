package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.xxx.application1.valuestructure.HAPDomainValueStructure;

public class HAPManualPartInValueContextGroupWithEntity extends HAPManualPartInValueContext{

	public static final String CHILDREN = "children";

	private List<HAPManualPartInValueContext> m_children;
	
	public HAPManualPartInValueContextGroupWithEntity(HAPManualInfoPartInValueContext partInfo) {
		super(partInfo);
		this.m_children = new ArrayList<HAPManualPartInValueContext>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY;    }

	public List<HAPManualPartInValueContext> getChildren(){   return this.m_children;   }
	
	public String addChild(HAPManualPartInValueContext child) {
		if(child.isEmptyOfValueStructure(null)) {
			return null;
		}
		child.getPartInfo().appendParentInfo(this.getPartInfo().getPriority());
		this.m_children.add(child);
		return child.getPartInfo().getName();
	}
	
	public HAPManualPartInValueContextGroupWithEntity cloneValueStructureComplexPartGroup() {
		HAPManualPartInValueContextGroupWithEntity out = new HAPManualPartInValueContextGroupWithEntity(this.getPartInfo().cloneValueStructurePartInfo());
		this.cloneToPartValueContext(out);
		for(HAPManualPartInValueContext child : this.m_children) {
			out.addChild(child.cloneValueContextPart());
		}
		return out;
	}

	@Override
	public HAPManualPartInValueContext cloneValueContextPart() {   return this.cloneValueStructureComplexPartGroup();  }

//	@Override
//	public HAPManualPartInValueContext inheritValueContextPart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates) {
//		HAPManualPartInValueContextGroupWithEntity out = new HAPManualPartInValueContextGroupWithEntity(this.getPartInfo().cloneValueStructurePartInfo());
//		this.cloneToPartValueContext(out);
//		for(HAPManualPartInValueContext child : this.m_children) {
//			out.addChild(child.inheritValueContextPart(valueStructureDomain, mode, groupTypeCandidates));
//		}
//		return out;
//	}
	
	@Override
	public void cleanValueStucture(Set<String> valueStrucutreIds) {
		for(int i=0; i<this.m_children.size(); i++) {
			HAPManualPartInValueContext child = this.m_children.get(i);
			child.cleanValueStucture(valueStrucutreIds);
			if(child.isEmpty()) {
				this.m_children.remove(i);
			}
		}
	}

	@Override
	public boolean isEmpty() {
		return this.m_children.isEmpty();
	}
	
	
	@Override
	public boolean isEmptyOfValueStructure(HAPDomainValueStructure valueStructureDomain) {	
		boolean out = true;
		for(HAPManualPartInValueContext child : this.m_children) {
			if(!child.isEmptyOfValueStructure(valueStructureDomain)) {
				out = false;
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> childrenJsonArray = new ArrayList<String>();
		for(HAPManualPartInValueContext child : this.m_children) {
			childrenJsonArray.add(child.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(CHILDREN, HAPUtilityJson.buildArrayJson(childrenJsonArray.toArray(new String[0])));
	}

}
