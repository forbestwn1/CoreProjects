package com.nosliw.data.datatype.importer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.HAPDataOperationInfo;
import com.nosliw.data.HAPDataOperationOutInfo;
import com.nosliw.data.HAPDataOperationParmInfo;
import com.nosliw.data.HAPDataTypeProvider;
import com.nosliw.data.datatype.util.HAPDBAccess;

public class HAPDataTypeImporterManager {
	
	private HAPDBAccess m_dbAccess; 
	
	public HAPDataTypeImporterManager(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		
		registerValueInfos();
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
//		m_dbAccess.saveDataType(dataType);

/*		
		List<HAPDataOperationInfo> ops = dataType.getDataOperationInfos();
		InputStream opsStream = cls.getResourceAsStream("operations.xml");
		if(opsStream!=null){
			List<HAPStringableValueEntity> ops1 = HAPStringableEntityImporterXML.readMutipleEntitys(opsStream, "data.operation");
			for(HAPStringableValueEntity op : ops1){
				ops.add((HAPDataOperationInfo)op);
			}
		}
		
		for(HAPDataOperationInfo op : ops){
			m_dbAccess.saveOperation((HAPDataOperationInfoImp)op, dataType);
		}
*/		
	}

	
	public static void main(String[] args){
		HAPDataTypeImporterManager man = new HAPDataTypeImporterManager();
		man.loadAll();
		HAPDBAccess.getInstance().close();
	}
}
