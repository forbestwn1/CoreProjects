package com.nosliw.core.application.common.datadefinition;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.core.application.entity.datarule.HAPDataRule;

public class HAPDefinitionDataRule extends HAPEntityInfoImp{

	@HAPAttribute
	public static String PATH = "path";

	HAPDataRule m_rule;

	String m_path;
	
	public String getPath() {    return this.m_path;    }
	
	public void setPath(String path) {   this.m_path = path;      }

    public 	HAPDataRule getRule() {   return this.m_rule;     }
	
}
