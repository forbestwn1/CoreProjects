package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.domain.entity.HAPAttributeEntity;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPInfoAdapter;
import com.nosliw.data.core.entity.HAPInfoEntityType;

//attribute in entity
public class HAPAttributeEntityDefinition2 extends HAPAttributeEntity<HAPEmbededDefinition>{

	//parent info definition
	public static final String PARENT = "parent";
	
	private HAPManualInfoValue m_valueInfo;
	
	private HAPEntityInfo m_info;


	
	private HAPPath m_pathFromRoot;

	private HAPManualEntity m_parent;
	
	//multiple adapters by name
	private Map<String, HAPInfoAdapter> m_adapters;
	
	public HAPAttributeEntityDefinition2(String name, HAPEmbededDefinition embeded, HAPInfoEntityType valueTypeInfo) {
		super(name, embeded, valueTypeInfo);
	}

	public HAPAttributeEntityDefinition2() {}

	
	
	
	
	public boolean isAttributeAutoProcess() {	return this.isAttributeAutoProcess(true);	}

	protected void cloneToEntityAttribute(HAPAttributeEntityDefinition2 attr) {
		super.cloneToEntityAttribute(attr);
		attr.setValue((HAPEmbededDefinition)this.getValue().cloneEmbeded());
	}

	@Override
	public HAPAttributeEntity cloneEntityAttribute() {
		HAPAttributeEntityDefinition2 out = new HAPAttributeEntityDefinition2();
		this.cloneToEntityAttribute(out);
		return out;
	}
}
