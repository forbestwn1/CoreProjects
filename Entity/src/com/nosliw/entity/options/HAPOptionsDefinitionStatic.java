package com.nosliw.entity.options;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.info.HAPDataTypeDefInfo;

/*
 * options that has static options values
 * every time, it return the same List of data
 * so that this options can be cached
 */
public class HAPOptionsDefinitionStatic extends HAPOptionsDefinition{
	
	List<HAPWraper> m_optionsDatas;
	
	public HAPOptionsDefinitionStatic(String name, HAPDataTypeDefInfo dataTypeDefInfo, List<HAPData> optionsDatas, String desc, HAPDataTypeManager dataTypeMan) {
		super(name, dataTypeDefInfo, desc, dataTypeMan);
		this.m_optionsDatas = new ArrayList<HAPWraper>();
		for(HAPData data : optionsDatas){
			this.m_optionsDatas.add(this.createOptionDataWraper(data));
		}
	}
	
	@Override
	public String getType(){return HAPConstant.OPTIONS_TYPE_STATIC;}
	
	public List<HAPWraper> getOptions() {
		return m_optionsDatas;
	}

	@Override
	public String toStringValue(String format) {
		return null;
	}

	@Override
	public void init() {
	}
}
