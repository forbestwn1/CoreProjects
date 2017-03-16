package com.nosliw.data.datatype.importer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.io.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPDataTypePicture;
import com.nosliw.data.core.HAPDataTypeProvider;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.HAPRelationshipPathSegment;
import com.nosliw.data.datatype.importer.js.HAPJSImporter;

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
				"operationinfo.xml",
				"datatypeversion.xml",

				"interfaceoperation.xml",
				"operation.xml",
				"operationvar.xml",
				"datatypeoperation.xml",

				"datatyperelationship.xml"
		});

	}
	
	public void loadAllDataType(){
		this.m_dbAccess.createDBTable(HAPDataTypeImpLoad._VALUEINFO_NAME);
		this.m_dbAccess.createDBTable(HAPOperationImp._VALUEINFO_NAME);
		this.m_dbAccess.createDBTable(HAPOperationVarInfoImp._VALUEINFO_NAME);
		
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

	public void buildDataTypePictures(){
		this.m_dbAccess.createDBTable(HAPRelationshipImp._VALUEINFO_NAME);
		
		List<HAPDataTypeImp> dataTypes = this.m_dbAccess.getAllDataTypes();
		for(HAPDataTypeImp dataType : dataTypes){
			HAPDataTypePictureImp dataTypePic = this.buildDataTypePicture((HAPDataTypeIdImp)dataType.getName());
			this.m_dbAccess.saveDataTypePicture(dataTypePic);
		}
	}
	
	public void buildDataTypeOperations(){
		this.m_dbAccess.createDBTable(HAPDataTypeOperationImp._VALUEINFO_NAME);

		List<HAPDataTypeImp> dataTypes = this.m_dbAccess.getAllDataTypes();
		for(HAPDataTypeImp dataType : dataTypes){
			this.buildDataTypeOperations(dataType);
		}
	}
	
	private Map<String, HAPDataTypeOperationImp> buildDataTypeOperations(HAPDataTypeImp dataType){
		Map<String, HAPDataTypeOperationImp> out = new LinkedHashMap<String, HAPDataTypeOperationImp>();
		
		List<HAPDataTypeOperationImp> currentDataTypeOperations = this.m_dbAccess.getNormalDataTypeOperations(dataType.getName());
		if(currentDataTypeOperations.size()==0){
			//not build yet
			HAPDataTypePictureImp pic = this.m_dbAccess.getDataTypePicture((HAPDataTypeIdImp)dataType.getName());
			
			//operations from parent
			if(dataType.getParentInfo()!=null){
				HAPRelationshipImp parentRelationship = pic.getRelationship(dataType.getParentInfo());
				Map<String, HAPDataTypeOperationImp> parentDataTypeOperations = buildDataTypeOperations(parentRelationship.getTarget());
				for(String opeartionName : parentDataTypeOperations.keySet()){
					HAPDataTypeOperationImp dataTypeOp = parentDataTypeOperations.get(opeartionName);
					HAPDataTypeOperationImp dataTypeOperation = dataTypeOp.extendPathSegment(HAPRelationshipPathSegment.buildPathSegmentForParent(), (HAPDataTypeIdImp)pic.getSourceDataType().getName());
					out.put(dataTypeOperation.getName(), dataTypeOperation);
				}
			}

			if(dataType.getLinkedDataTypeId()!=null){
				HAPRelationshipImp relationship = pic.getRelationship(dataType.getLinkedDataTypeId());
				Map<String, HAPDataTypeOperationImp> ataTypeOperations = buildDataTypeOperations(relationship.getTarget());
				for(String opeartionName : ataTypeOperations.keySet()){
					HAPDataTypeOperationImp dataTypeOp = ataTypeOperations.get(opeartionName);
					HAPDataTypeOperationImp dataTypeOperation = dataTypeOp.extendPathSegment(HAPRelationshipPathSegment.buildPathSegmentForLinked(), (HAPDataTypeIdImp)pic.getSourceDataType().getName());
					out.put(dataTypeOperation.getName(), dataTypeOperation);
				}
			}

			List<HAPDataTypeOperationImp> toSave = new ArrayList<HAPDataTypeOperationImp>();
			//operations from own
			List<HAPOperationImp> ownOperations = this.m_dbAccess.getOperationInfosByDataType((HAPDataTypeIdImp)dataType.getName());
			for(HAPOperationImp ownOperation : ownOperations){
				HAPDataTypeOperationImp ownDataTypeOperation = new HAPDataTypeOperationImp(ownOperation);
				if(HAPBasicUtility.isStringEmpty(ownOperation.getType())){
					//regular operation
					out.put(ownDataTypeOperation.getName(), ownDataTypeOperation);
				}
				else{
					toSave.add(ownDataTypeOperation);
				}
			}

			toSave.addAll(out.values());
			this.m_dbAccess.saveDataTypeOperation(toSave);
		}
		else{
			for(HAPDataTypeOperationImp op : currentDataTypeOperations){
				if(HAPBasicUtility.isStringEmpty(op.getType())){
					out.put(op.getName(), op);
				}
			}
		}
		return out;
	}
	
	private void loadDataType(Class cls){
		InputStream dataTypeStream = cls.getResourceAsStream("datatype.xml");
		HAPDataTypeImpLoad dataType = (HAPDataTypeImpLoad)HAPStringableEntityImporterXML.readRootEntity(dataTypeStream, HAPDataTypeImpLoad._VALUEINFO_NAME);
		dataType.resolveByConfigure(null);
		m_dbAccess.saveDataType(dataType);

		List<HAPOperation> ops = dataType.getDataOperationInfos();
		InputStream opsStream = cls.getResourceAsStream("operations.xml");
		if(opsStream!=null){
			List<HAPStringableValueEntity> ops1 = HAPStringableEntityImporterXML.readMutipleEntitys(opsStream, HAPOperationImp._VALUEINFO_NAME);
			for(HAPStringableValueEntity op : ops1){
				ops.add((HAPOperation)op);
			}
		}
		
		for(HAPOperation op : ops){
			m_dbAccess.saveOperation((HAPOperationImp)op, dataType);
		}
	}

	private HAPDataTypePictureImp buildDataTypePicture(HAPDataTypeIdImp dataTypeId){
		HAPDataTypeImp dataType = this.getDataType(dataTypeId);
		HAPDataTypePictureImp out = new HAPDataTypePictureImp(dataType);
		
		//self as a relationship as well
		HAPRelationshipImp self = new HAPRelationshipImp(dataType);
		self.setTarget(dataType);
		out.addRelationship(self);
		
		this.buildDataTypePictureFromConntectedDataType(dataType, out, HAPConstant.DATATYPE_PATHSEGMENT_PARENT);
		this.buildDataTypePictureFromConntectedDataType(dataType, out, HAPConstant.DATATYPE_PATHSEGMENT_LINKED);
		return out;
	}
	
	private void buildDataTypePictureFromConntectedDataType(HAPDataTypeImp dataType, HAPDataTypePictureImp out, int connectType){
		HAPDataTypeIdImp connectDataTypeId = dataType.getConntectedDataTypeId(connectType);
		if(connectDataTypeId!=null){
			HAPDataTypeImp connectDataType = (HAPDataTypeImp)this.getDataType(connectDataTypeId);
			HAPDataTypePicture connectDataTypePic = this.getDataTypePicture(connectDataTypeId);
			if(connectDataTypePic==null){
				connectDataTypePic = this.buildDataTypePicture(connectDataTypeId);
			}
			Set<? extends HAPRelationship> connectRelationships = connectDataTypePic.getRelationships();
			for(HAPRelationship connectRelationship : connectRelationships){
				out.addRelationship(((HAPRelationshipImp)connectRelationship).extendPathSegmentSource(HAPRelationshipPathSegment.buildPathSegment(connectType), dataType));
			}
		}
	}
	
	private HAPDataTypePicture getDataTypePicture(HAPDataTypeIdImp dataTypeId){
		return this.m_dbAccess.getDataTypePicture(dataTypeId);
	}
	
	private HAPDataTypeImp getDataType(HAPDataTypeIdImp dataTypeId) {
		return this.m_dbAccess.getDataType(dataTypeId);
	}
	
	
	public static void main(String[] args){
		HAPDataTypeImporterManager man = new HAPDataTypeImporterManager();
		man.loadAllDataType();
		man.buildDataTypePictures();
		man.buildDataTypeOperations();
		
		HAPJSImporter jsImporter = new HAPJSImporter();
		jsImporter.loadFromFolder("C:\\Users\\ewaniwa\\Desktop\\MyWork\\CoreProjects\\DataType");
		
//		HAPDBAccess.getInstance().close();
	}
}
