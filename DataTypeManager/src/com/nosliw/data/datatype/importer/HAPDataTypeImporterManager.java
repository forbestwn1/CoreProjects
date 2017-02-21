package com.nosliw.data.datatype.importer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.io.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPDBTableInfo;
import com.nosliw.common.strvalue.valueinfo.HAPSqlUtility;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPRelationshipPathSegment;
import com.nosliw.data.HAPDataTypePicture;
import com.nosliw.data.HAPDataTypeProvider;
import com.nosliw.data.datatype.importer.js.HAPJSImporter;
import com.nosliw.data.datatype.util.HAPDBAccess1;

public class HAPDataTypeImporterManager {
	
	private HAPDBAccess m_dbAccess; 
	
	public HAPDataTypeImporterManager(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		
		registerValueInfos();
	}
	
	private void registerValueInfos(){
		HAPValueInfoManager.getInstance().importFromXML(HAPDataTypeImporterManager.class, new String[]{
				"datatypedefinition.xml",
				"datatypeid.xml",
				"datatypeinfo.xml",
				"datatypeversion.xml",

				"datatypeoperation.xml",
				"operationvar.xml",

				"datatyperelationship.xml"
		});

		this.m_dbAccess.createDBTable("data.datatypedef");
		this.m_dbAccess.createDBTable("data.operation");
		this.m_dbAccess.createDBTable("data.operationvar");
		this.m_dbAccess.createDBTable("data.relationship");
	}
	
	public void loadAll(){
		new HAPClassFilter(){
			@Override
			protected void process(Class cls, Object data) {
				loadDataType(cls);
			}

			@Override
			protected boolean isValid(Class cls) {
				Class[] interfaces = cls.getInterfaces();
				for(Class inf : interfaces){
					if(inf.equals(HAPDataTypeProvider.class)){
						return true;
					}
				}
				return false;
			}
		}.process(null);
		
		
		
		
	}

	private void loadDataType(Class cls){
		InputStream dataTypeStream = cls.getResourceAsStream("datatype.xml");
		HAPDataTypeImpLoad dataType = (HAPDataTypeImpLoad)HAPStringableEntityImporterXML.readRootEntity(dataTypeStream, "data.datatypedef");
		dataType.resolveByConfigure(null);
		m_dbAccess.saveDataType(dataType);

		List<HAPOperationInfo> ops = dataType.getDataOperationInfos();
		InputStream opsStream = cls.getResourceAsStream("operations.xml");
		if(opsStream!=null){
			List<HAPStringableValueEntity> ops1 = HAPStringableEntityImporterXML.readMutipleEntitys(opsStream, "data.operation");
			for(HAPStringableValueEntity op : ops1){
				ops.add((HAPOperationInfo)op);
			}
		}
		
		for(HAPOperationInfo op : ops){
			m_dbAccess.saveOperation((HAPOperationInfoImp)op, dataType);
		}
	}

	private HAPDataTypePicture buildDataTypePicture(HAPDataTypeInfoImp dataTypeInfo){
		HAPDataTypeImp dataType = (HAPDataTypeImp)this.getDataType(dataTypeInfo);
		HAPDataTypePicture out = new HAPDataTypePicture(dataType);
		
		this.buildDataTypePictureFromConntectedDataType(dataType, out, HAPConstant.DATATYPE_PATHSEGMENT_PARENT);
		this.buildDataTypePictureFromConntectedDataType(dataType, out, HAPConstant.DATATYPE_PATHSEGMENT_LINKED);
		return out;
	}
	
	
	private void buildDataTypePictureFromConntectedDataType(HAPDataTypeImp dataType, HAPDataTypePicture out, int connectType){
		HAPDataTypeInfoImp connectDataTypeInfo = dataType.getConntectedDataTypeInfo(connectType);
		HAPDataTypeImp connectDataType = (HAPDataTypeImp)this.getDataType(connectDataTypeInfo);
		HAPDataTypePicture connectDataTypePic = this.getDataTypePicture(connectDataTypeInfo);
		if(connectDataTypePic==null){
			connectDataTypePic = this.buildDataTypePicture(connectDataTypeInfo);
		}
		Set<HAPDataTypePictureNodeImp> dataTypePicNodes = connectDataTypePic.getRelationships();
		for(HAPDataTypePictureNodeImp picNode : dataTypePicNodes){
			out.addNode(picNode.extendPathSegment(HAPRelationshipPathSegment.buildPathSegment(connectType)));
		}
		
		HAPDataTypePictureNodeImp connectNode = new HAPDataTypePictureNodeImp(connectDataType);
		connectNode.appendPathSegment(HAPRelationshipPathSegment.buildPathSegment(connectType));
	}
	
	private HAPDataTypePicture getDataTypePicture(HAPDataTypeInfoImp dataTypeInfo){
		return this.m_dbAccess.getDataTypePicture(dataTypeInfo);
	}
	
	
	public static void main(String[] args){
		HAPDataTypeImporterManager man = new HAPDataTypeImporterManager();
		man.loadAll();
//		
//		HAPJSImporter jsImporter = new HAPJSImporter();
//		jsImporter.loadFromFolder("C:\\Users\\ewaniwa\\Desktop\\MyWork\\CoreProjects\\DataType");
//		
//		HAPDBAccess.getInstance().close();
	}
}
