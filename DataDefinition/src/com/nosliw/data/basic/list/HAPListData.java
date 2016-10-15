package com.nosliw.data.basic.list;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.imp.HAPDataImp;

public class HAPListData extends HAPDataImp{

	private List<HAPData> m_listData;
	
	public HAPListData(HAPDataType dataType) {
		super(dataType);
		this.m_listData = new ArrayList<HAPData>();
	}

	public void addData(HAPData data){
		this.m_listData.add(data);
	}
	
	public HAPData getData(int index){
		return this.m_listData.get(index);
	}
	
	public int getSize(){
		return this.m_listData.size();
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
