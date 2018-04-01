//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.distance");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define operation
//define operation
dataTypeDefition.operations['shorterThan'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var distance1 = parms.getParm("dis1").value.distance;
			var distance2 = parms.getParm("dis2").value.distance;
			if(distance1<distance2){
				return {
					dataTypeId : "test.boolean;1.0.0",
					value : true,
				}
			}
			else{
				return {
					dataTypeId : "test.boolean;1.0.0",
					value : false,
				}
			}
		},
};


nosliw.addDataTypeDefinition(dataTypeDefition);
