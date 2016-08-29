package com.nosliw.common.constant;

import java.lang.reflect.Field;
import java.util.Set;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.strvalue.basic.HAPStringableValueUtility;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPClassFilter;

public class HAPConstantManager {

	private HAPValueInfoManager m_valueInfoMan;
	
	public HAPConstantManager(){
		this.m_valueInfoMan = new HAPValueInfoManager();
		HAPValueInfoImporterXML.importFromXml("constant.xml", HAPConstantManager.class, this.m_valueInfoMan);
		HAPValueInfoImporterXML.importFromXml("group.xml", HAPConstantManager.class, this.m_valueInfoMan);
		HAPValueInfoImporterXML.importFromXml("group_attribute.xml", HAPConstantManager.class, this.m_valueInfoMan);
		HAPValueInfoImporterXML.importFromXml("group_constant.xml", HAPConstantManager.class, this.m_valueInfoMan);
	}
	
	public void addConstantGroup(HAPConstantGroup group){
		
	}
	
	public HAPValueInfoManager getValueInfoManager(){
		return this.m_valueInfoMan;
	}

	public HAPConstantGroup buildConstantGroupFromClassAttr(){
		HAPConstantGroup group = new HAPConstantGroup(HAPConstantGroup.TYPE_CLASSATTR);
		new HAPClassFilter(){
			@Override
			protected void process(Class checkClass, Object data) {
				Field[] fields = checkClass.getDeclaredFields();
				for(Field field : fields){
					String fieldName = field.getName();
					if(fieldName.startsWith(HAPStringableValueUtility.PREFIX_ENTITYPROPERTY)){
						try{
							String constantValue = field.get(null).toString();
							String constantName = fieldName.substring(HAPStringableValueUtility.PREFIX_ENTITYPROPERTY.length());
							HAPConstantInfo constantInfo = HAPConstantInfo.build(constantName, constantValue);
							group.addConstantInfo(constantInfo);
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}

			@Override
			protected boolean isValid(Class cls) {		return HAPEntityWithAttributeConstant.class.isAssignableFrom(cls);	}
		}.process(group);

		this.addConstantGroup(group);
		return group;
	}
}
