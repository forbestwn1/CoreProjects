package com.nosliw.common.clss;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Set;

import com.google.common.reflect.ClassPath;
import com.nosliw.common.utils.HAPSystemUtility;

public abstract class HAPClassFilter {
	
	public void process(Object data){
		try {
			ClassLoader cl = HAPClassFilter.class.getClassLoader();
			ClassPath classPath;
			classPath = ClassPath.from(cl);
		    Set<ClassPath.ClassInfo> classeInfos = classPath.getAllClasses();
		    //loop all the classes
			for(ClassPath.ClassInfo classInfo : classeInfos){
				
				if(classInfo.getName().equals("com.nosliw.data.core.expression.HAPExpression")){
					int kkkk = 555;
					kkkk++;
				}
				
				
				if(HAPSystemUtility.isHAPClass(classInfo.getName())){
					//only check nosliw package
					Class checkClass = classInfo.load();
//					if(!checkClass.isInterface() && !Modifier.isAbstract(checkClass.getModifiers()))
					{
						if(this.isValid(checkClass)){
							this.process(checkClass, data);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	abstract protected void process(Class cls, Object data);
	
	abstract protected boolean isValid(Class cls);
}
