package com.nosliw.common.utils;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Set;

import com.google.common.reflect.ClassPath;

public abstract class HAPClassFilter {
	
	public void process(Object data){
		try {
			ClassLoader cl = HAPClassFilter.class.getClassLoader();
			ClassPath classPath;
			classPath = ClassPath.from(cl);
		    Set<ClassPath.ClassInfo> classeInfos = classPath.getAllClasses();
		    //loop all the classes
			for(ClassPath.ClassInfo classInfo : classeInfos){
				if(HAPSystemUtility.isHAPClass(classInfo.getClass())){
					//only check nosliw package
					Class checkClass = classInfo.load();
					if(!checkClass.isInterface() && !Modifier.isAbstract(checkClass.getModifiers())){
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
