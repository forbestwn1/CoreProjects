package com.nosliw.core.application.division.manual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.HAPAttributeEntity;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

//attribute in entity
public class HAPManualAttribute extends HAPEntityInfoImp implements HAPTreeNode{

	public static final String VALUEINFO = "value";

	public static final String RELATION = "relation";

	public static final String ADAPTER = "adapter";

	public static final String PARENT = "parent";

	//extra info definition
	public static final String INFO = "info";
	
	//attribute value
	private HAPManualWrapperValue m_valueInfo;
	
	//multiple adapters by name
	private Map<String, HAPManualInfoAdapter> m_adapters;

	//relationship to parent
	private List<HAPManualBrickRelation> m_relations;

	//path from root
	private HAPPath m_pathFromRoot;

	//parent entity
	private HAPManualBrick m_parent;
	
	public HAPManualAttribute() {
		this.m_relations = new ArrayList<HAPManualBrickRelation>();
		this.m_adapters = new LinkedHashMap<String, HAPManualInfoAdapter>();
	}

	public HAPManualAttribute(String name, HAPManualWrapperValue valueInfo) {
		this();
		this.setName(name);
		this.m_valueInfo = valueInfo;
	}

	public HAPManualWrapperValue getValueInfo() {    return this.m_valueInfo;     }
	public void setValueInfo(HAPManualWrapperValue valueInfo) {    this.m_valueInfo = valueInfo;     }
	
	public void addAdapter(HAPManualInfoAdapter adapter) {    this.m_adapters.put(adapter.getName(), adapter);     }
	public Set<HAPManualInfoAdapter> getAdapters(){   return new HashSet<HAPManualInfoAdapter>(this.m_adapters.values());      }
	
	public void addRelation(HAPManualBrickRelation relation) {    this.m_relations.add(relation);      }
	public List<HAPManualBrickRelation> getRelations(){    return this.m_relations;     }
	
	@Override
	public HAPPath getPathFromRoot() {   return this.m_pathFromRoot;  }
	public void setPathFromRoot(HAPPath path) {    this.m_pathFromRoot = path;     }

	@Override
	public Object getNodeValue() {   return this.m_valueInfo;    }

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEINFO, this.m_valueInfo.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PATHFROMROOT, this.m_pathFromRoot.toString());
	}

	
	
	
	
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
