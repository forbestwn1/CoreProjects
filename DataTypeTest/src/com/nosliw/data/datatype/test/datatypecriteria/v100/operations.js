//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.datatypecriteria");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)

//define operation
dataTypeDefition.operations['getChild'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var childName = parms.getParm("childName").value;
			return {
				dataTypeId : "test.datatypecriteria;1.0.0",
				value: "test.string"
			};
		},

		requires:{
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);