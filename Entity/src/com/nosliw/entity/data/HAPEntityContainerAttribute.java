package com.nosliw.entity.data;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.imp.HAPDataTypeImp;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.entity.utils.HAPEntityDataUtility;

public class HAPEntityContainerAttribute extends HAPDataTypeImp{

	public static HAPDataType dataType;
	
	private HAPEntityDefinitionManager m_entityDefMan;
	
	public HAPEntityContainerAttribute(HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan) {
		super(new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_CONTAINER, HAPConstant.DATATYPE_TYPE_CONTAINER_ENTITYATTRIBUTE), null, null, null, "", dataTypeMan);
		this.m_entityDefMan = entityDefMan;
	}

	@Override
	public HAPData getDefaultData() {
		return null;
	}

	@Override
	public HAPData toData(Object value, String format) {
		return null;
	}

	@Override
	public HAPServiceData validate(HAPData data) {
		return HAPServiceData.createSuccessData();
	}

	public String newContainerElement(HAPEntityContainerAttributeWraper containerWraper, String eid){
		String id = HAPBasicUtility.isStringNotEmpty(eid)?eid:HAPEntityDataUtility.getNextID();
		HAPDataTypeInfo childDataType = containerWraper.getChildDataTypeDefInfo();
		HAPData data = null;
		if(HAPEntityDataTypeUtility.isAtomType(childDataType)){
			data = this.getDataTypeManager().getDefaultValue(childDataType);
		}
		else if(HAPEntityDataTypeUtility.isReferenceType(childDataType)){
		}
		else if(HAPEntityDataTypeUtility.isEntityType(childDataType)){
			HAPEntity entityDataType = this.getEntityDefManager().getEntityDataTypeByName(childDataType.getType());
			data = entityDataType.newEntity();
		}
		
		containerWraper.getContainerData().addElementData(data, id);
		return id;
	}
	
	protected HAPEntityDefinitionManager getEntityDefManager(){return this.m_entityDefMan;}
	
}
