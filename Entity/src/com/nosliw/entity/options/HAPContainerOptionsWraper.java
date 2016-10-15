package com.nosliw.entity.options;

import java.util.Map;

import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;

public class HAPContainerOptionsWraper extends HAPWraper {

	public HAPContainerOptionsWraper(HAPDataTypeManager dataTypeMan){
		super(new HAPDataTypeDefInfo(HAPContainerOptions.dataTypeInfo), dataTypeMan);
		this.setData(new HAPContainerOptionsData());
	}
	
	@Override
	protected void buildOjbectJsonMap(Map<String, String> map, Map<String, Class> dataTypeMap){
		super.buildOjbectJsonMap(map, dataTypeMap);
	}	

	public HAPContainerOptionsData getContainerData(){return (HAPContainerOptionsData)this.getData();	}
	
	public boolean contains(HAPWraper wrapper){
		return this.getContainerData().contains(wrapper);
	}
}
