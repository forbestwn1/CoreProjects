package com.nosliw.data.datatype.importer;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPOperationOutInfo;
import com.nosliw.data.HAPOperationParmInfo;
import com.nosliw.data.HAPDataTypeProvider;
import com.nosliw.data.datatype.importer.js.HAPJSImporter;
import com.nosliw.data.datatype.importer.js.HAPJSOperationInfo;
import com.nosliw.data.datatype.util.HAPDBAccess;

public class HAPDataTypeImporterManager {
	
	private HAPDBAccess m_dbAccess; 
	
	private HAPJSImporter m_jsImporter;
	
	public HAPDataTypeImporterManager(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		
		registerValueInfos();
		
		this.m_jsImporter = new HAPJSImporter(this.m_dbAccess);
	}
	
	private void registerValueInfos(){
		Set<String> valueInfos = new HashSet<String>();
		valueInfos.add("datatypedefinition.xml");
		valueInfos.add("datatypeinfo.xml");
		valueInfos.add("datatypeversion.xml");

		valueInfos.add("datatypeoperation.xml");
		valueInfos.add("operationoutput.xml");
		valueInfos.add("operationparm.xml");

		HAPValueInfoManager.getInstance().importFromXML(HAPDataTypeImporterManager.class, valueInfos);
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
		HAPDataTypeImp dataType = (HAPDataTypeImp)HAPStringableEntityImporterXML.readRootEntity(dataTypeStream, "data.datatypedef");
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
		
		//find js operation
		File f = new File(cls.getProtectionDomain().getCodeSource().getLocation().getPath());
		List<HAPJSOperationInfo> jsout = new ArrayList<HAPJSOperationInfo>();
		this.m_jsImporter.importFromFolder(f.getPath(), jsout);
		for(HAPJSOperationInfo jsInfo : jsout){
			this.m_dbAccess.saveOperationInfoJS(jsInfo);
		}
	}

	
	public static void main(String[] args){
		HAPDataTypeImporterManager man = new HAPDataTypeImporterManager();
		man.loadAll();
		HAPDBAccess.getInstance().close();
	}
}
