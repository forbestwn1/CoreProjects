package com.nosliw.test.resourceId;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceIdEmbeded;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPTestResourceId {

	public static void main(String[] args) {
		
		String simpleResource1 = "simpleResourceType|resourceId";
		HAPResourceIdSimple simpleResourceId1 = (HAPResourceIdSimple)HAPFactoryResourceId.newInstance(simpleResource1);
		System.out.println(HAPJsonUtility.formatJson(simpleResourceId1.toStringValue(HAPSerializationFormat.JSON_FULL)));
		
		String simpleResource2 = "simpleResourceType|*resourceId";
		HAPResourceIdSimple simpleResourceId2 = (HAPResourceIdSimple)HAPFactoryResourceId.newInstance(simpleResource2);
		System.out.println(HAPJsonUtility.formatJson(simpleResourceId2.toStringValue(HAPSerializationFormat.JSON_FULL)));
	
		String localResource1 = "localResourceType|#resourceId=c:\\resources\\resourceType";
		HAPResourceIdLocal lcoalResourceId1 = (HAPResourceIdLocal)HAPFactoryResourceId.newInstance(localResource1);
		System.out.println(HAPJsonUtility.formatJson(lcoalResourceId1.toStringValue(HAPSerializationFormat.JSON_FULL)));

		String embededResource1 = "embededResourceType|@a.b.c=simpleResourceType|resourceId";
		HAPResourceIdEmbeded embededResourceId1 = (HAPResourceIdEmbeded)HAPFactoryResourceId.newInstance(embededResource1);
		System.out.println(HAPJsonUtility.formatJson(embededResourceId1.toStringValue(HAPSerializationFormat.JSON_FULL)));
		
		String embededResource2 = "embededResourceType|@a.b.c=embededResourceType|@d.e.f=simpleResourceType|resourceId";
		HAPResourceIdEmbeded embededResourceId2 = (HAPResourceIdEmbeded)HAPFactoryResourceId.newInstance(embededResource2);
		System.out.println(HAPJsonUtility.formatJson(embededResourceId2.toStringValue(HAPSerializationFormat.JSON_FULL)));
		
	}

}
