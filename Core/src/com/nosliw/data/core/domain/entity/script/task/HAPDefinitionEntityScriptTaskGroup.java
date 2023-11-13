package com.nosliw.data.core.domain.entity.script.task;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public class HAPDefinitionEntityScriptTaskGroup extends HAPDefinitionEntityInDomainComplex{

	public HAPDefinitionEntityScriptTaskGroup() {
		this.setAttributeValueObject(HAPExecutableEntityScriptTaskGroup.DEFINITION, new ArrayList<HAPDefinitionTaskScript>());
	}
	
	public List<HAPDefinitionTaskScript> getDefinitions(){   return (List<HAPDefinitionTaskScript>)this.getAttributeValue(HAPExecutableEntityScriptTaskGroup.DEFINITION);    }
	public void addDefinition(HAPDefinitionTaskScript def) {   this.getDefinitions().add(def);      }
	
	public HAPJsonTypeScript getScript() {   return (HAPJsonTypeScript)this.getAttributeValue(HAPExecutableEntityScriptTaskGroup.SCRIPT);     }
	public void setScript(HAPJsonTypeScript script) {   this.setAttributeValueObject(HAPExecutableEntityScriptTaskGroup.SCRIPT, script);     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityScriptTaskGroup out = new HAPDefinitionEntityScriptTaskGroup();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
