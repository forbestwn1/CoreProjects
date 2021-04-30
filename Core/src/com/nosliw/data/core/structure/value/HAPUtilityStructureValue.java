package com.nosliw.data.core.structure.value;

import java.util.HashSet;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPProcessorContextDefinitionElement;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPUtilityContext;

public class HAPUtilityStructureValue {

	public static HAPStructureValueDefinitionFlat updateRootName(HAPStructureValueDefinitionFlat structure, HAPUpdateName nameUpdate) {
		HAPStructureValueDefinitionFlat out = new HAPStructureValueDefinitionFlat();
		
		for(String rootName : structure.getRootNames()) {
			HAPRoot root = structure.getRoot(rootName).cloneRoot();
			
			
			
		}
		
		//update context
		for(String rootName : new HashSet<String>(this.getRootNames())) {
			HAPRoot root = this.getRoot(rootName);
			root.setName(nameUpdate.getUpdatedName(root.getName()));
			HAPUtilityContext.processContextRootElement(root, rootName, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
					if(eleInfo.getElement() instanceof HAPElementLeafRelative) {
						HAPElementLeafRelative relative = (HAPElementLeafRelative)eleInfo.getElement();
						if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(relative.getParent())) {
							//update local relative path
							HAPReferenceElement path = relative.getPathFormat();
							relative.setPath(new HAPReferenceElement(new HAPIdContextDefinitionRoot(path.getRootStructureId().getCategary(), nameUpdate.getUpdatedName(path.getRootStructureId().getName())), path.getSubPath()));
						}
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement ele, Object value) { }
			}, null);
			//update root name
			this.m_roots.remove(rootName);
			this.addRoot(nameUpdate.getUpdatedName(rootName), root);
		}
	}

	
}
