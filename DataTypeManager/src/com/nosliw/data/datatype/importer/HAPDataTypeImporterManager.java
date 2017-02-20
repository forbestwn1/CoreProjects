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
import com.nosliw.data.HAPOperationInfo;
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
				"operationvar.xml"
		});

		this.m_dbAccess.createDBTable("data.datatypedef");
		this.m_dbAccess.createDBTable("data.operation");
		this.m_dbAccess.createDBTable("data.operationvar");
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
