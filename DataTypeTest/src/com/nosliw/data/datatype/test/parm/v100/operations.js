//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.parm");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)

//define operation
dataTypeDefition.operations['getValue'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			return this.value.data;
		},

		requires:{
		},
};

dataTypeDefition.operations['getCriteria'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var out = this.value.outputCriteria;
			return out;
		},

		requires:{
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
