package com.nosliw.data1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data.utils.HAPAttributeConstant;
import com.nosliw.data.utils.HAPDataUtility;

public class HAPWraper implements HAPSerializable{

	private HAPData m_data = null;
	private HAPDataTypeDefInfo m_dataTypeDefInfo; 	//the data type for this wraper
	private HAPDataTypeManager m_dataTypeMan;

	public HAPWraper(HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
	}

	public HAPWraper(HAPData data, HAPDataTypeManager dataTypeMan){
		this.setData(data);
		this.m_dataTypeDefInfo = new HAPDataTypeDefInfo(HAPDataUtility.getDataTypeInfo(data));
		this.m_dataTypeMan = dataTypeMan;
	}
	
	protected HAPWraper(HAPDataTypeDefInfo dataType, HAPDataTypeManager dataTypeMan){
		this.m_dataTypeDefInfo = dataType;
		this.m_dataTypeMan = dataTypeMan;
	}
	
	public HAPDataTypeDefInfo getDataTypeDefInfo(){	return m_dataTypeDefInfo;	}

	
	//*************************  Changing data without notification and validation
	/*
	 * set wrapped object directly without validation and notification
	 * somethings, if set listener as well, override this method
	 */
	public void setData(HAPData data){
		if(this.m_dataTypeDefInfo==null){
			this.m_dataTypeDefInfo = new HAPDataTypeDefInfo(HAPDataUtility.getDataTypeInfo(data));
		}
		this.m_data = data;
	}

	public HAPData getData(){return this.m_data;}

	/*
	 * if this wraper is empty or not
	 * if a wraper is empty or not is convention for different wraper
	 */
	public boolean isEmpty(){
		if(this.m_data==null)  return true;
		return this.m_data.isEmpty();
	}

	/*
	 * clear the value of this wraper
	 * setEmpty does not trigure event
	 * it is just a short cut for setValue
	 */
	public void setEmpty(){this.m_data=null;}

	public void clearUp(Map<String, Object> parms){
		if(this.m_data!=null)	this.m_data.clearUp(parms);
		this.m_data = null;
		this.m_dataTypeMan = null;
	}
	
	final public HAPWraper cloneWraper(){
		HAPWraper out = this.newWraper();
		this.cloneTo(out);
		out.cloneData(this.getData());
		return out;
	}
	
	protected HAPWraper newWraper(){return new HAPWraper(this.getDataTypeManager());}
	
	protected void cloneTo(HAPWraper wraper){
		if(wraper.m_dataTypeDefInfo==null)  wraper.m_dataTypeDefInfo = this.m_dataTypeDefInfo;
		if(wraper.m_dataTypeMan==null)  wraper.m_dataTypeMan = this.m_dataTypeMan;
	}

	protected void cloneData(HAPData data){
		if(data==null)  return;
		this.setData(data.cloneData());
	}

	/*
	 * sub class to override to put their own specific json info 
	 */
	protected void buildOjbectJsonMap(Map<String, String> map, Map<String, Class> dataTypeMap){}
	
	@Override
	public final String toStringValue(String format) {
		StringBuffer out = new StringBuffer();
		if(format.equals(HAPSerializationFormat.JSON)){
			Map<String, String> dataMap = new LinkedHashMap<String, String>();
			Map<String, Class> dataTypeMap = new LinkedHashMap<String, Class>();
			dataMap.put(HAPAttributeConstant.WRAPER_DATATYPE, this.getDataTypeDefInfo().toStringValue(null));
			HAPData data = this.getData();
			if(data!=null){
				dataMap.put(HAPAttributeConstant.WRAPER_DATA, data.toStringValue(format));
			}

			//other info related with special are within "info" attribute
			Map<String, Class> infoDataTypeMap = new LinkedHashMap<String, Class>();
			Map<String, String> infoDataMap = new LinkedHashMap<String, String>();
			this.buildOjbectJsonMap(infoDataMap, infoDataTypeMap);
			dataMap.put(HAPAttributeConstant.WRAPER_INFO, HAPJsonUtility.buildMapJson(infoDataMap, infoDataTypeMap));
			
			out.append(HAPJsonUtility.buildMapJson(dataMap, dataTypeMap));
		}
		return out.toString();
	}
	
	@Override
	public String toString(){return HAPJsonUtility.formatJson(this.toStringValue(HAPSerializationFormat.JSON));	}
	
	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}
}
