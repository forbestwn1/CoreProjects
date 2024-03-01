package com.nosliw.data.core.entity.division.manual;

import java.util.List;
import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.domain.entity.HAPAttributeEntity;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

//attribute in entity
public class HAPManualAttribute extends HAPEntityInfoImp{

	public static final String VALUEINFO = "value";

	public static final String RELATION = "relation";

	public static final String ADAPTER = "adapter";

	public static final String PARENT = "parent";

	//extra info definition
	public static final String INFO = "info";
	
	//attribute value
	private HAPManualInfoValue m_valueInfo;
	
	//multiple adapters by name
	private Map<String, HAPManualInfoAdapter> m_adapters;

	//relationship to parent
	private List<HAPManualEntityRelation> m_relations;

	//path from root
	private HAPPath m_pathFromRoot;

	//parent entity
	private HAPManualEntity m_parent;
	
	public HAPManualAttribute() {}

	public HAPManualAttribute(String name, HAPManualInfoValue valueInfo) {
		this.setName(name);
		this.m_valueInfo = valueInfo;
	}

	public HAPManualInfoValue getValueInfo() {    return this.m_valueInfo;     }
	public void setValueInfo(HAPManualInfoValue valueInfo) {    this.m_valueInfo = valueInfo;     }
	
	public void addAdapter(HAPManualInfoAdapter adapter) {    this.m_adapters.put(adapter.getName(), adapter);     }
	
	public void addRealtion(HAPManualEntityRelation relation) {    this.m_relations.add(relation);      }
	
	
	protected void cloneToEntityAttribute(HAPManualAttribute attr) {
		super.cloneToEntityAttribute(attr);
		attr.setValue((HAPEmbededDefinition)this.getValue().cloneEmbeded());
	}

	@Override
	public HAPAttributeEntity cloneEntityAttribute() {
		HAPManualAttribute out = new HAPManualAttribute();
		this.cloneToEntityAttribute(out);
		return out;
	}
}
