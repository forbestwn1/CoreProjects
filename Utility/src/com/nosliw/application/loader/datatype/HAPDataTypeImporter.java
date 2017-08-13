package com.nosliw.application.loader.datatype;

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
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypePicture;
import com.nosliw.data.core.HAPDataTypeProvider;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.HAPRelationshipPathSegment;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.HAPDataTypeImp;
import com.nosliw.data.core.imp.HAPDataTypeImpLoad;
import com.nosliw.data.core.imp.HAPDataTypeOperationImp;
import com.nosliw.data.core.imp.HAPDataTypePictureImp;
import com.nosliw.data.core.imp.HAPModuleDataType;
import com.nosliw.data.core.imp.HAPOperationImp;
import com.nosliw.data.core.imp.HAPOperationVarInfoImp;
import com.nosliw.data.core.imp.HAPRelationshipImp;

public class HAPDataTypeImporter {
	
	private HAPDataAccessDataType m_dataAccess; 
	
	public HAPDataTypeImporter(HAPDataAccessDataType dataAccess){
		this.m_dataAccess = dataAccess;
	}
	
	public void loadAllDataType(){
		this.m_dataAccess.createDBTable(HAPDataTypeImpLoad._VALUEINFO_NAME);
		this.m_dataAccess.createDBTable(HAPOperationImp._VALUEINFO_NAME);
		this.m_dataAccess.createDBTable(HAPOperationVarInfoImp._VALUEINFO_NAME);
		
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
		this.m_dataAccess.createDBTable(HAPRelationshipImp._VALUEINFO_NAME);
		
		List<HAPDataTypeImp> dataTypes = this.m_dataAccess.getAllDataTypes();
		for(HAPDataTypeImp dataType : dataTypes){
			HAPDataTypePictureImp dataTypePic = this.buildDataTypePicture((HAPDataTypeId)dataType.getName());
			this.m_dataAccess.saveDataTypePicture(dataTypePic);
		}
	}
	
	public void buildDataTypeOperations(){
		this.m_dataAccess.createDBTable(HAPDataTypeOperationImp._VALUEINFO_NAME);

		List<HAPDataTypeImp> dataTypes = this.m_dataAccess.getAllDataTypes();
		for(HAPDataTypeImp dataType : dataTypes){
			this.buildDataTypeOperations(dataType);
		}
	}
	
	private Map<String, HAPDataTypeOperationImp> buildDataTypeOperations(HAPDataTypeImp dataType){
		Map<String, HAPDataTypeOperationImp> out = new LinkedHashMap<String, HAPDataTypeOperationImp>();
		
		List<HAPDataTypeOperationImp> currentDataTypeOperations = this.m_dataAccess.getNormalDataTypeOperations(dataType.getName());
		if(currentDataTypeOperations.size()==0){
			//not build yet
			HAPDataTypePictureImp pic = this.m_dataAccess.getDataTypePicture((HAPDataTypeId)dataType.getName());
			
			//operations from parent
			List<HAPDataTypeId> parentsId = dataType.getParentsInfo();
			if(parentsId!=null){
				for(HAPDataTypeId parentId : parentsId){
					HAPRelationshipImp parentRelationship = pic.getRelationship(parentId);
					Map<String, HAPDataTypeOperationImp> parentDataTypeOperations = buildDataTypeOperations(parentRelationship.getTargetDataType());
					for(String opeartionName : parentDataTypeOperations.keySet()){
						HAPDataTypeOperationImp dataTypeOp = parentDataTypeOperations.get(opeartionName);
						HAPDataTypeOperationImp dataTypeOperation = dataTypeOp.extendPathSegment(new HAPRelationshipPathSegment(parentId), (HAPDataTypeId)pic.getSourceDataType().getName());
						out.put(dataTypeOperation.getName(), dataTypeOperation);
					}
				}
			}
			
			if(dataType.getLinkedDataTypeId()!=null){
				HAPRelationshipImp relationship = pic.getRelationship(dataType.getLinkedDataTypeId());
				Map<String, HAPDataTypeOperationImp> ataTypeOperations = buildDataTypeOperations(relationship.getTargetDataType());
				for(String opeartionName : ataTypeOperations.keySet()){
					HAPDataTypeOperationImp dataTypeOp = ataTypeOperations.get(opeartionName);
					HAPDataTypeOperationImp dataTypeOperation = dataTypeOp.extendPathSegment(new HAPRelationshipPathSegment(dataType.getLinkedVersion()), (HAPDataTypeId)pic.getSourceDataType().getName());
					out.put(dataTypeOperation.getName(), dataTypeOperation);
				}
			}

			//store all datat type operation (normal + new + others)
			List<HAPDataTypeOperationImp> toSave = new ArrayList<HAPDataTypeOperationImp>();
			//operations from own
			List<HAPOperationImp> ownOperations = this.m_dataAccess.getOperationInfosByDataType((HAPDataTypeId)dataType.getName());
			for(HAPOperationImp ownOperation : ownOperations){
				HAPDataTypeOperationImp ownDataTypeOperation = new HAPDataTypeOperationImp(ownOperation);
				if(HAPDataUtility.isNormalDataOpration(ownOperation)){
					//regular operation
					out.put(ownDataTypeOperation.getName(), ownDataTypeOperation);
				}
				else{
					toSave.add(ownDataTypeOperation);
				}
			}

			toSave.addAll(out.values());
			this.m_dataAccess.saveDataTypeOperation(toSave);
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
		m_dataAccess.saveDataType(dataType);

		List<HAPOperation> ops = dataType.getDataOperationInfos();
		InputStream opsStream = cls.getResourceAsStream("operations.xml");
		if(opsStream!=null){
			List<HAPStringableValueEntity> ops1 = HAPStringableEntityImporterXML.readMutipleEntitys(opsStream, HAPOperationImp._VALUEINFO_NAME);
			for(HAPStringableValueEntity op : ops1){
				ops.add((HAPOperation)op);
			}
		}
		
		for(HAPOperation op : ops){
			//set default value if missing
			HAPOperationImp opImp = (HAPOperationImp)op;
			List<HAPOperationParmInfo> parmsInfo = opImp.getParmsInfo();
			for(HAPOperationParmInfo p : parmsInfo){
				HAPOperationVarInfoImp parmInfo = (HAPOperationVarInfoImp)p;
				//set default name for base parm if no name is provided
				if(parmInfo.getIsBase() && HAPBasicUtility.isStringEmpty(parmInfo.getName())){
					parmInfo.setName(HAPConstant.DATAOPERATION_PARM_BASENAME);
				}
				
				//set default criteria as current data type
				if(parmInfo.getCriteria()==null){
					parmInfo.setCriteria(new HAPDataTypeCriteriaId(dataType.getName()));
				}
			}
			
			m_dataAccess.saveOperation(opImp, dataType);
		}
	}

	private HAPDataTypePictureImp buildDataTypePicture(HAPDataTypeId dataTypeId){
		HAPDataTypeImp dataType = this.getDataType(dataTypeId);
		HAPDataTypePictureImp out = new HAPDataTypePictureImp(dataType);
		
		//self as a relationship as well
		HAPRelationshipImp self = new HAPRelationshipImp();
		self.setSourceDataType(dataType);
		self.setTargetDataType(dataType);
		self.setTargetType(HAPConstant.DATATYPE_RELATIONSHIPTYPE_SELF);
		out.addRelationship(self);
		
		this.buildDataTypePictureFromParentsDataType(dataType, out);
		this.buildDataTypePictureFromLinkedDataType(dataType, out);
		return out;
	}
	
	private void buildDataTypePictureFromLinkedDataType(HAPDataTypeImp dataType, HAPDataTypePictureImp out){
		HAPDataTypeId linkedDataTypeId = dataType.getLinkedDataTypeId();
		if(linkedDataTypeId!=null){
			HAPDataTypeImp connectDataType = (HAPDataTypeImp)this.getDataType(linkedDataTypeId);
			HAPDataTypePicture connectDataTypePic = this.getDataTypePicture(linkedDataTypeId);
			if(connectDataTypePic==null){
				connectDataTypePic = this.buildDataTypePicture(linkedDataTypeId);
			}
			Set<? extends HAPRelationship> connectRelationships = connectDataTypePic.getRelationships();
			boolean isRoot = connectRelationships.size()==1;
			for(HAPRelationship connectRelationship : connectRelationships){
				HAPRelationshipImp relationship = ((HAPRelationshipImp)connectRelationship).extendPathSegmentSource(new HAPRelationshipPathSegment(linkedDataTypeId.getVersion()), dataType);
				if(isRoot)   relationship.setTargetType(HAPConstant.DATATYPE_RELATIONSHIPTYPE_ROOT);
				else relationship.setTargetType(HAPConstant.DATATYPE_RELATIONSHIPTYPE_INTERMEDIA);
				out.addRelationship(relationship);
			}
		}
	}
	
	private void buildDataTypePictureFromParentsDataType(HAPDataTypeImp dataType, HAPDataTypePictureImp out){
		List<HAPDataTypeId> parentsDataTypeId = dataType.getParentsInfo();
		if(parentsDataTypeId!=null){
			for(HAPDataTypeId parentDataTypeId : parentsDataTypeId){
				HAPDataTypeImp connectDataType = (HAPDataTypeImp)this.getDataType(parentDataTypeId);
				HAPDataTypePicture connectDataTypePic = this.getDataTypePicture(parentDataTypeId);
				if(connectDataTypePic==null){
					connectDataTypePic = this.buildDataTypePicture(parentDataTypeId);
				}
				Set<? extends HAPRelationship> connectRelationships = connectDataTypePic.getRelationships();
				boolean isRoot = connectRelationships.size()==1;
				for(HAPRelationship connectRelationship : connectRelationships){
					HAPRelationshipImp relationship = ((HAPRelationshipImp)connectRelationship).extendPathSegmentSource(new HAPRelationshipPathSegment(parentDataTypeId), dataType);
					if(isRoot)   relationship.setTargetType(HAPConstant.DATATYPE_RELATIONSHIPTYPE_ROOT);
					else relationship.setTargetType(HAPConstant.DATATYPE_RELATIONSHIPTYPE_INTERMEDIA);
					out.addRelationship(relationship);
				}
			}
		}
	}
	
	private HAPDataTypePicture getDataTypePicture(HAPDataTypeId dataTypeId){
		return this.m_dataAccess.getDataTypePicture(dataTypeId);
	}
	
	private HAPDataTypeImp getDataType(HAPDataTypeId dataTypeId) {
		return this.m_dataAccess.getDataType(dataTypeId);
	}
	
	
	public static void main(String[] args){

		HAPModuleDataType dataTypeModule = new HAPModuleDataType().init(HAPValueInfoManager.getInstance());;
		
		HAPDataTypeImporter dataTypeImporter = new HAPDataTypeImporter(dataTypeModule.getDataAccess());
		dataTypeImporter.loadAllDataType();
		dataTypeImporter.buildDataTypePictures();
		dataTypeImporter.buildDataTypeOperations();
		
//		HAPDBAccess.getInstance().close();
	}
}
