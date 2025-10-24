package com.nosliw.core.application.common.datadefinition;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
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
		out.m_path = this.m_path;
		out.m_rule = this.m_rule.cloneDataRule();
		return out;
	}
}
