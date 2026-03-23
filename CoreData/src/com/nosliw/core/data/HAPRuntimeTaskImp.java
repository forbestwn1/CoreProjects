package com.nosliw.core.data;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.runtime.execute.HAPTaskRuntime;

public abstract class HAPRuntimeTaskImp extends HAPTaskRuntime{

	@Override
	protected HAPServiceData processResult(HAPServiceData serviceData) {
		if(serviceData.isSuccess()){
			if(this.getResultDataType()==HAPData.class) {
				//if result is data, then convert json object to data
				JSONObject dataJson = (JSONObject)serviceData.getData();
				HAPData data = HAPUtilityData.buildDataWrapperFromJson(dataJson);
				serviceData.setData(data);
			}
		}
		return serviceData;
	}
}
