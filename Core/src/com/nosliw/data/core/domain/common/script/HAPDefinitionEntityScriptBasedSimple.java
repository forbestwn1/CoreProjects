package com.nosliw.data.core.domain.common.script;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPDefinitionEntityScriptBasedSimple extends HAPDefinitionEntityInDomain implements HAPDefinitionEntityScriptBased{

	public HAPDefinitionEntityScriptBasedSimple() {}

	@Override
	public void setScript(String script) {    this.setAttributeObject(HAPExecutableEntityScriptBased.SCRIPT, new HAPEmbededDefinition(script));    }
	@Override
	public String getScript() {   return (String)this.getAttributeValue(HAPExecutableEntityScriptBased.SCRIPT);     }

	@Override
	public void setScriptResourceId(HAPResourceId resourceId) {   this.setAttributeObject(HAPExecutableEntityScriptBased.RESOURCEID, new HAPEmbededDefinition(resourceId));     }
	@Override
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValue(HAPExecutableEntityScriptBased.RESOURCEID);     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityScriptBasedSimple out = new HAPDefinitionEntityScriptBasedSimple();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
