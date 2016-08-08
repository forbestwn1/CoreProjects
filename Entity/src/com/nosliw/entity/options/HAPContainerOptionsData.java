package com.nosliw.entity.options;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.info.HAPDataTypeInfo;

/*
 * 
 */
public class HAPContainerOptionsData extends HAPDataImp{

	private HAPDataTypeInfo m_eleDataTypeInfo;
	private List<HAPWraper> m_eleWrapers;
	
	public HAPContainerOptionsData() {
		super(HAPContainerOptions.dataType);
		this.m_eleWrapers = new ArrayList<HAPWraper>();
	}

	public void setEleDataTypeInfo(HAPDataTypeInfo dataTypeInfo){this.m_eleDataTypeInfo = dataTypeInfo;}
	
	public HAPDataTypeInfo getEleDataTypeInfo(){return this.m_eleDataTypeInfo;}
	
	public void addOptionsData(HAPWraper wraper){this.m_eleWrapers.add(wraper);	}
	public void addOptionsDatas(List<HAPWraper> wrapers){this.m_eleWrapers.addAll(wrapers);	}
	public List<HAPWraper> getDatas(){return this.m_eleWrapers;}
	public boolean contains(HAPWraper wrapper){return this.m_eleWrapers.contains(wrapper);}
	
	@Override
	public HAPData cloneData() {
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		if(format.equals(HAPConstant.CONS_SERIALIZATION_JSON)){
			List<String> jsonArray = new ArrayList<String>();
			for(HAPWraper wraper : this.m_eleWrapers){
				jsonArray.add(wraper.toStringValue(format));
			}
			return HAPJsonUtility.getArrayJson(jsonArray.toArray(new String[0]));
		}
		return null;
	}

}
