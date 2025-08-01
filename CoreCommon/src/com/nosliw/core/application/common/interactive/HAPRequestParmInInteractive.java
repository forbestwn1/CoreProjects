package com.nosliw.core.application.common.interactive;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.application.common.variable.HAPVariableDefinition;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;

/**
 * This is variable info for expression 
 */
@HAPEntityWithAttribute
public class HAPRequestParmInInteractive extends HAPVariableDefinition{

	public static HAPRequestParmInInteractive buildUndefinedVariableInfo() {
		return buildParm(null);
	}
	
	public static HAPRequestParmInInteractive buildParmFromObject(Object def) {
		HAPRequestParmInInteractive out = new HAPRequestParmInInteractive();
		out.buildObject(def, null);
		return out;
	}

	public static HAPRequestParmInInteractive buildParm(HAPDataTypeCriteria criteria) {
		return buildParm(null, criteria);
	}

	public static HAPRequestParmInInteractive buildParm(String name, HAPDataTypeCriteria criteria) {
		HAPRequestParmInInteractive out = new HAPRequestParmInInteractive();
		if(criteria!=null) {
			out.getDataInfo().setCriteria(criteria);
		}
		else {
			
		}
		
		if(name!=null) {
			out.setName(name);
		}
		return out;
	}
	
	protected HAPRequestParmInInteractive() {
		this.setDataInfo(new HAPVariableDataInfo());
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPRequestParmInInteractive){
			HAPRequestParmInInteractive varInfo = (HAPRequestParmInInteractive)obj;
			return super.equals(varInfo);
		}
		return out;
	}
	
	@Override
	public HAPRequestParmInInteractive cloneVariableInfo() {
		HAPRequestParmInInteractive out = new HAPRequestParmInInteractive();
		this.cloneToVariableInfo(out);
		return out;
	}
	
}
