package com.nosliw.data.core.domain.entity.script.task;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.data.core.domain.entity.HAPExecutableEntitySimple;

@HAPEntityWithAttribute
public class HAPExecutableEntityScriptTaskGroup extends HAPExecutableEntitySimple{

	@HAPAttribute
	public static final String DEFINITION = "definition";
	
	@HAPAttribute
	public static final String SCRIPT = "script";
	

	public List<HAPDefinitionTaskScript> getDefinitions(){   return (List<HAPDefinitionTaskScript>)this.getAttributeValue(DEFINITION);    }
	public void addDefinition(HAPDefinitionTaskScript def) {   this.getDefinitions().add(def);      }
	
	public HAPJsonTypeScript getScript() {   return (HAPJsonTypeScript)this.getAttributeValue(SCRIPT);     }
	public void setScript(HAPJsonTypeScript script) {   this.setAttributeValueObject(SCRIPT, script);     }

	
	
}
