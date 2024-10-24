package com.nosliw.data.core.domain.entity.script.task;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;

public class HAPDefinitionEntityScriptTaskGroup extends HAPManualBrickComplex{

	public HAPDefinitionEntityScriptTaskGroup() {
		this.setAttributeValueObject(HAPExecutableEntityScriptTaskGroup.DEFINITION, new ArrayList<HAPDefinitionTaskScript>());
	}
	
	public List<HAPDefinitionTaskScript> getDefinitions(){   return (List<HAPDefinitionTaskScript>)this.getAttributeValueOfValue(HAPExecutableEntityScriptTaskGroup.DEFINITION);    }
	public void addDefinition(HAPDefinitionTaskScript def) {   this.getDefinitions().add(def);      }
	
	public HAPJsonTypeScript getScript() {   return (HAPJsonTypeScript)this.getAttributeValueOfValue(HAPExecutableEntityScriptTaskGroup.SCRIPT);     }
	public void setScript(HAPJsonTypeScript script) {   this.setAttributeValueObject(HAPExecutableEntityScriptTaskGroup.SCRIPT, script);     }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityScriptTaskGroup out = new HAPDefinitionEntityScriptTaskGroup();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
