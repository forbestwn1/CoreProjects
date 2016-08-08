package com.nosliw.entity.query;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPWraper;

/*
 * 
 */
public class HAPQueryEntityAttributeWraper extends HAPWraper{

	public HAPQueryEntityAttributeWraper(HAPData data, HAPDataTypeManager dataTypeMan){
		super(dataTypeMan);
		this.setData(data);
	}
	
	private HAPQueryEntityAttributeWraper(HAPDataTypeManager dataTypeMan){
		super(dataTypeMan);
	}
	
	@Override
	protected HAPWraper newWraper(){return new HAPQueryEntityAttributeWraper(this.getDataTypeManager());}

	@Override
	protected void cloneTo(HAPWraper wraper){
		super.cloneTo(wraper);
	}
	
	@Override
	protected void buildOjbectJsonMap(Map<String, String> map, Map<String, Class> dataTypeMap){
//		map.put(HAPAttributeConstant.ATTR_QUERYENTITYATTRIBUTEWRAPER_ENTITYPATH, m_entityPathInfo.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON));
	}
	
	public String toString(){return this.toStringValue(HAPConstant.CONS_SERIALIZATION_JSON);}

}
