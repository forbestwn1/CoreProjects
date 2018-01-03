package com.nosliw.data.core.datasource;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

@HAPEntityWithAttribute
public class HAPGatewayDataSource extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_GETDATA = "getData";

	@HAPAttribute
	final public static String COMMAND_GETDATA_NAME = "name";

	@HAPAttribute
	final public static String COMMAND_GETDATA_PARMS = "parms";
	
	private HAPDataSourceManager m_dataSourceManager;
	
	public HAPGatewayDataSource(HAPDataSourceManager dataSourceMan){
		this.m_dataSourceManager = dataSourceMan;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms) throws Exception {
		HAPServiceData out = null;
		switch(command){
		case COMMAND_GETDATA:
		{
			String dataSourceName = parms.getString(COMMAND_GETDATA_NAME);

			Map<String, HAPData> dataSourceParms = new LinkedHashMap<String, HAPData>();
			JSONObject parmsJson = parms.optJSONObject(COMMAND_GETDATA_PARMS);
			if(parmsJson!=null){
				Iterator<String> it = parmsJson.keys();
				while(it.hasNext()){
					String parmName = it.next();
					JSONObject dataJson = parmsJson.getJSONObject(parmName);
					HAPData parmData = HAPDataUtility.buildDataWrapperFromJson(dataJson);
					dataSourceParms.put(parmName, parmData);
				}
			}
			HAPData dataOut = this.m_dataSourceManager.getData(dataSourceName, dataSourceParms);
			out = this.createSuccessWithObject(dataOut);
			break;
		}
		}
		return out;
	}

}
