//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.boolean");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define operation
dataTypeDefition.operations['opposite'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var bValue = parms.getParm("base").value;
			var outValue;
			if(bValue==true)  outValue = false;
			else if(bValue==false)  outValue = true;
			return {
				dataTypeId : "test.boolean;1.0.0",
				value : outValue,
			}
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
