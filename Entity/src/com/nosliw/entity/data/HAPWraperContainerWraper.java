package com.nosliw.entity.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeDefInfo;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

public class HAPWraperContainerWraper extends HAPDataWraper{

	HAPWraperContainerWraper(HAPDataTypeDefInfo dataTypeInfo, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan) {
		super(new HAPDataTypeDefInfo(HAPConstant.DATATYPE_CATEGARY_CONTAINER, HAPConstant.DATATYPE_TYPE_CONTAINER_WRAPPER), dataTypeMan, entityDefMan);
		this.setData(new HAPWraperContainerData(dataTypeMan.getDataType(dataTypeInfo.getDataTypeInfo())));
	}

	public HAPWraperContainerData getWraperContainerData(){
		return (HAPWraperContainerData)this.getData();
	}
	
	@Override
	protected HAPDataWraper getChildWraper(String pathEle) {
		HAPWraperContainerWraper out = new HAPWraperContainerWraper(null, this.getDataTypeManager(), this.getEntityDefinitionManager());
		HAPWraperContainerData outData = out.getWraperContainerData();
		
		List<HAPDataWraper> wrapers = this.getWraperContainerData().getWrapers();
		for(HAPDataWraper wraper : wrapers){
			HAPDataWraper childWraper = wraper.getChildWraper(pathEle);
			if(childWraper instanceof HAPWraperContainerWraper){
				outData.addWrapers(((HAPWraperContainerWraper)childWraper).getWraperContainerData().getWrapers());
			}
			else{
				outData.addWraper(childWraper);
			}
		}
		return out;
	}

	@Override
	protected void initAttributeData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void clearUPData(Map<String, Object> scope) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HAPServiceData doOperate(HAPEntityOperationInfo operation, List<HAPEntityOperationInfo> extraOps) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Set<HAPDataWraper> getGenericChildWraper(String pathEle) {
		// TODO Auto-generated method stub
		return null;
	}
}
