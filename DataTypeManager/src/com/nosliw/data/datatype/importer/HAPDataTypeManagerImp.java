package com.nosliw.data.datatype.importer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeFamily;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPDataTypeVersion;
import com.nosliw.data.HAPExpression;
import com.nosliw.data.HAPExpressionInfo;
import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPQueryInfo;
import com.nosliw.data.datatype.util.HAPDBAccess;

public class HAPDataTypeManagerImp implements HAPDataTypeManager{

	private HAPDBAccess m_dbAccess;
	
	@Override
	public HAPDataType getDataType(HAPDataTypeInfo dataTypeInfo) {
		return this.m_dbAccess.getDataTypeByInfo(dataTypeInfo);
	}

	@Override
	public HAPDataTypeFamily getDataTypeFamily(HAPDataTypeInfo dataTypeInfo) {
		List<HAPDataTypeImp> dataTypes = this.m_dbAccess.getDataTypesByName(dataTypeInfo.getName());
		HAPDataTypeFamily out = new HAPDataTypeFamily(dataTypes);
		return out;
	}

	@Override
	public List<? extends HAPOperationInfo> getLocalOperationInfos(HAPDataTypeInfo dataTypeInfo) {
		String dataTypeVersion = null;
		HAPDataTypeVersion version = dataTypeInfo.getVersion();
		dataTypeVersion = version.toStringValue(HAPSerializationFormat.LITERATE);
		return this.m_dbAccess.getOperationsInfosByDataTypeInfo(dataTypeInfo.getName(), dataTypeVersion);
	}

	@Override
	public HAPOperationInfo getLocalOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name) {
		return this.m_dbAccess.getOperationInfo(dataTypeInfo.getName(), dataTypeInfo.getVersion().toStringValue(HAPSerializationFormat.LITERATE), name);
	}

	@Override
	public List<? extends HAPOperationInfo> getOperationInfos(HAPDataTypeInfo dataTypeInfo) {
		
		
		
		String dataTypeVersion = null;
		HAPDataTypeVersion version = dataTypeInfo.getVersion();
		if(version!=null){
			dataTypeVersion = version.toStringValue(HAPSerializationFormat.LITERATE);
		}
		return this.m_dbAccess.getOperationsInfosByDataTypeInfo(dataTypeInfo.getName(), dataTypeVersion);
	}

	@Override
	public HAPOperationInfo getOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends HAPOperationInfo> getNewDataOperations(HAPDataTypeInfo dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperationInfo getNewDataOperation(HAPDataTypeInfo dataTypeInfo,
			Map<String, HAPDataTypeInfo> parmsDataTypeInfos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPExpression compileExpression(HAPExpressionInfo expressionInfo, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPDataType> queryDataType(HAPQueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getLanguages() {
		// TODO Auto-generated method stub
		return null;
	}

	private HAPDataTypePicture buildDataTypePicture(HAPDataTypeInfo dataTypeInfo){
		HAPDataTypeImp dataType = (HAPDataTypeImp)this.getDataType(dataTypeInfo);
		HAPDataTypePicture out = new HAPDataTypePicture(dataType);
		{
			HAPDataTypeInfo parentDataTypeInfo = dataType.getParentDataTypeInfo();
			HAPDataTypeImp parentDataType = (HAPDataTypeImp)this.getDataType(parentDataTypeInfo);
			HAPDataTypePicture parentDataTypePic = this.getDataTypePicture(parentDataType.getId());
			if(parentDataTypePic==null){
				parentDataTypePic = this.buildDataTypePicture(parentDataTypeInfo);
			}
			Set<HAPDataTypePictureElement> dataTypePicEles = parentDataTypePic.getDataTypeElements();
			for(HAPDataTypePictureElement picEle : dataTypePicEles){
				out.addElement(picEle.extendPathSegment(HAPDataTypePathSegment.buildPathSegmentForParent()));
			}
		}
		
		HAPDataTypeVersion linkedVersion = dataType.getLinkedVersion();
		if(linkedVersion!=null){
			HAPDataTypeInfo linkedDataTypeInfo = new HAPDataTypeInfoImp(dataTypeInfo.getName(), linkedVersion);
			HAPDataTypeImp linkedDataType = (HAPDataTypeImp)this.getDataType(linkedDataTypeInfo);
			HAPDataTypePicture linkedDataTypePic = this.getDataTypePicture(linkedDataType.getId());
			if(linkedDataTypePic==null){
				linkedDataTypePic = this.buildDataTypePicture(linkedDataTypeInfo);
			}
			Set<HAPDataTypePictureElement> dataTypePicEles = linkedDataTypePic.getDataTypeElements();
			for(HAPDataTypePictureElement picEle : dataTypePicEles){
				out.addElement(picEle.extendPathSegment(HAPDataTypePathSegment.buildPathSegmentForLinked()));
			}
		}
		return out;
	}
	
	
	private HAPDataTypePicture getDataTypePicture(String dataTypeId){
		return this.m_dbAccess.getDataTypePicture(dataTypeId);
	}
	
}
