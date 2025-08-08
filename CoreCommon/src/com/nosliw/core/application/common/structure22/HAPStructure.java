package com.nosliw.core.application.common.structure22;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPStructure  extends HAPSerializable{

	@HAPAttribute
	public static final String ROOT = "root";
	
	HAPRootInStructure addRoot(HAPRootInStructure root);
	Map<String, HAPRootInStructure> getRoots();

	default public HAPRootInStructure getRootByName(String rootName) {   return this.getRoots().get(rootName);  }

	default public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	default public boolean isEmpty() {   return this.getRoots().isEmpty();   }

}
