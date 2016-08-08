package com.nosliw.entity.data;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;

public class HAPWraperContainerData extends HAPDataImp{
	List<HAPDataWraper> m_wrapers;
	
	public HAPWraperContainerData(HAPDataType dataType) {
		super(dataType);
		this.m_wrapers = new ArrayList<HAPDataWraper>();
	}

	public void addWraper(HAPDataWraper dataWraper){
		this.m_wrapers.add(dataWraper);
	}
	
	public void addWrapers(List<HAPDataWraper> dataWrapers){
		this.m_wrapers.addAll(dataWrapers);
	}
	
	public List<HAPDataWraper> getWrapers(){
		return this.m_wrapers;
	}
	
	@Override
	public HAPData cloneData() {
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		return null;
	}
}
