package com.nosliw.core.application.division.manual.executable;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPWithValueContext;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPWrapperValuePort;
import com.nosliw.core.application.valuecontext.HAPValueContext;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPValuePortValueContext;

@HAPEntityWithAttribute
public class HAPManualBrickBlockComplex extends HAPManualBrickBlock implements HAPWithValueContext{

	private HAPDomainValueStructure m_valueStructureDomain; 
	
	public HAPManualBrickBlockComplex() {
	}
	
	public void setValueStructureDomain(HAPDomainValueStructure valueStructureDomain) {   this.m_valueStructureDomain = valueStructureDomain;     }

	@Override
	public HAPContainerValuePorts getInternalValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPGroupValuePorts valePortGroup = new HAPGroupValuePorts();
		valePortGroup.setName(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT);
		valePortGroup.addValuePort(new HAPWrapperValuePort(new HAPValuePortValueContext(this, this.m_valueStructureDomain)), true);
		out.addValuePortGroup(valePortGroup, true);
		
		for(HAPGroupValuePorts group : this.getInternalOtherValuePortGroups()) {
			out.addValuePortGroup(group, false);
		}
		return out;
	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPGroupValuePorts valePortGroup = new HAPGroupValuePorts();
		valePortGroup.setName(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT);
		valePortGroup.addValuePort(new HAPWrapperValuePort(new HAPValuePortValueContext(this, this.m_valueStructureDomain)), true);
		out.addValuePortGroup(valePortGroup, true);
		
		for(HAPGroupValuePorts group : this.getExternalOtherValuePortGroups()) {
			out.addValuePortGroup(group, false);
		}
		return out;
	}

	protected Set<HAPGroupValuePorts> getInternalOtherValuePortGroups() {   return new HashSet<HAPGroupValuePorts>();   }
	protected Set<HAPGroupValuePorts> getExternalOtherValuePortGroups() {   return new HashSet<HAPGroupValuePorts>();   }
	
	@Override
	protected boolean buildBrickFormatJson(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
		super.buildBrickFormatJson(jsonObj, brickMan);
		JSONObject valueContextJsonObj = jsonObj.optJSONObject(VALUECONTEXT);
		if(valueContextJsonObj!=null) {
			this.m_valueContext = new HAPValueContext();
			this.m_valueContext.buildObject(valueContextJsonObj, HAPSerializationFormat.JSON);
			
		}
		return true;
	}
}
