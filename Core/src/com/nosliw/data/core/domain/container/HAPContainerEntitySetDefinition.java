package com.nosliw.data.core.domain.container;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;

public class HAPContainerEntitySetDefinition extends HAPContainerEntitySet<HAPInfoContainerElementSetDefinition> implements HAPExpandable{

	public HAPContainerEntitySetDefinition() {}
	
	public HAPContainerEntitySetDefinition(String eleType) {
		super(eleType);
	}
	
	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET; }

	@Override
	public HAPContainerEntitySetDefinition cloneContainerEntity() {
		HAPContainerEntitySetDefinition out = new HAPContainerEntitySetDefinition();
		this.cloneToContainer(out);
		return out;
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDefDomain) {
		List<String> eleArray = new ArrayList<String>();
		for(HAPInfoContainerElementSetDefinition ele : this.getAllElementsInfo()) {
			eleArray.add(ele.toExpandedJsonString(entityDefDomain));
		}
		return HAPJsonUtility.buildArrayJson(eleArray.toArray(new String[0]));
	}
}
