package com.nosliw.core.application.division.manual.definition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.HAPAttributeEntity;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

//attribute in entity
public class HAPManualDefinitionAttributeInBrick extends HAPEntityInfoImp implements HAPTreeNode{

	public static final String VALUEWRAPPER = "valueWrapper";

	public static final String RELATION = "relation";

	public static final String ADAPTER = "adapter";

	public static final String ISTASK = "isTask";

	public static final String PARENT = "parent";

	//extra info definition
	public static final String INFO = "info";
	
	//attribute value
	private HAPManualDefinitionWrapperValue m_valueWrapper;
	
	//multiple adapters by name
	private Map<String, HAPManualDefinitionAdapter> m_adapters;

	//relationship to parent
	private List<HAPManualDefinitionBrickRelation> m_relations;

	//path from root
	private HAPPath m_pathFromRoot;

	//parent entity
	private HAPManualDefinitionBrick m_parent;
	
	public HAPManualDefinitionAttributeInBrick() {
		this.m_relations = new ArrayList<HAPManualDefinitionBrickRelation>();
		this.m_adapters = new LinkedHashMap<String, HAPManualDefinitionAdapter>();
	}

	public HAPManualDefinitionAttributeInBrick(String name, HAPManualDefinitionWrapperValue valueWrapper) {
		this();
		this.setName(name);
		this.m_valueWrapper = valueWrapper;
	}

	public HAPManualDefinitionWrapperValue getValueWrapper() {    return this.m_valueWrapper;     }
	public void setValueWrapper(HAPManualDefinitionWrapperValue valueWrapper) {    this.m_valueWrapper = valueWrapper;     }
	
	public void addAdapter(HAPManualDefinitionAdapter adapter) {    this.m_adapters.put(adapter.getName(), adapter);     }
	public Set<HAPManualDefinitionAdapter> getAdapters(){   return new HashSet<HAPManualDefinitionAdapter>(this.m_adapters.values());      }
	
	public void addRelation(HAPManualDefinitionBrickRelation relation) {    this.m_relations.add(relation);      }
	public List<HAPManualDefinitionBrickRelation> getRelations(){    return this.m_relations;     }
	
	@Override
	public HAPPath getPathFromRoot() {   return this.m_pathFromRoot;  }
	public void setPathFromRoot(HAPPath path) {    this.m_pathFromRoot = path;     }

	@Override
	public Object getNodeValue() {   return this.m_valueWrapper;    }

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_pathFromRoot!=null) {
			jsonMap.put(PATHFROMROOT, this.m_pathFromRoot.toString());
		}
		if(!this.m_adapters.isEmpty()) {
			jsonMap.put(ADAPTER, HAPManagerSerialize.getInstance().toStringValue(this.m_adapters, HAPSerializationFormat.JSON));
		}
	}

	
	
	
	
	protected void cloneToEntityAttribute(HAPManualDefinitionAttributeInBrick attr) {
		super.cloneToEntityAttribute(attr);
		attr.setValue((HAPEmbededDefinition)this.getValue().cloneEmbeded());
	}

	@Override
	public HAPAttributeEntity cloneEntityAttribute() {
		HAPManualDefinitionAttributeInBrick out = new HAPManualDefinitionAttributeInBrick();
		this.cloneToEntityAttribute(out);
		return out;
	}

}
