package com.nosliw.data.core.domain.entity.script.task;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

@HAPEntityWithAttribute
public class HAPExecutableEntityScriptTaskGroup extends HAPExecutableEntityComplex{

	@HAPAttribute
	public static final String DEFINITION = "definition";
	
	@HAPAttribute
	public static final String SCRIPT = "script";
	
	public HAPExecutableEntityScriptTaskGroup() {
		this.setAttributeValueObject(DEFINITION, new ArrayList<HAPDefinitionTaskScript>());
	}

	public List<HAPDefinitionTaskScript> getDefinitions(){   return (List<HAPDefinitionTaskScript>)this.getAttributeValue(DEFINITION);    }
	public void addDefinition(HAPDefinitionTaskScript def) {   this.getDefinitions().add(def);      }
	
	public HAPDefinitionTaskScript getDefinitionByName(String name) {
		for(HAPDefinitionTaskScript def : this.getDefinitions()) {
			if(name.equals(def.getName()))  return def;
		}
		return null;
	}
	
	public HAPJsonTypeScript getScript() {   return (HAPJsonTypeScript)this.getAttributeValue(SCRIPT);     }
	public void setScript(HAPJsonTypeScript script) {   this.setAttributeValueObject(SCRIPT, script);     }

}
