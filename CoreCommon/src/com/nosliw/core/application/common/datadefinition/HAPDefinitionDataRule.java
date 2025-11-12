package com.nosliw.core.application.common.datadefinition;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.entity.datarule.HAPDataRule;

public class HAPDefinitionDataRule extends HAPEntityInfoImp{

	@HAPAttribute
	public static String PATH = "path";

	@HAPAttribute
	public static String DATARULE = "dataRule";

	private HAPDataRule m_rule;

	private String m_path;
	
	public HAPDefinitionDataRule() {}
	
	public String getPath() {    return this.m_path;    }
	public void setPath(String path) {   this.m_path = path;      }

    public 	HAPDataRule getDataRule() {   return this.m_rule;     }
	public void setDataRule(HAPDataRule dataRule) {   this.m_rule = dataRule;   }
	
	public HAPDefinitionDataRule cloneDataRuleDef() {
		HAPDefinitionDataRule out = new HAPDefinitionDataRule();
		this.cloneToEntityInfo(out);
		out.m_path = this.m_path;
		out.m_rule = this.m_rule.cloneDataRule();
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATARULE, HAPUtilityJson.buildJson(m_rule, HAPSerializationFormat.JSON));
        jsonMap.put(PATH, this.m_path);
    }
}
