//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("core.url", "1.1.0");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
	"operation" : { 
		op: "core.text;1.0.0;operation"
	},
	"datatype" : {
		text : "core.text;1.0.0"
	},
	"library" : {
		underline : "underscore;1.8.3"
	},
	"helper" : {
		globalHelper : {}
	}
};

//define what operations in this page requires (operation, datatype, library)
dataTypeDefition.localRequires = {
	"operation" : { 
		op: "core.text;1.0.0;operation"
	},
	"datatype" : {
		text : "core.text;1.0.0"
	},
	"library" : {
		underline : "underscore;1.8.3"
	},
	"helper" : {
		localHelper : {}
	}
};


//define operation
dataTypeDefition.operations['host1'] = {
	//define required resources for operation
	requires:{
		"operation" : { 
			op1: "core.text;1.0.0;operation1",
			op2: "core.text;1.0.0;operation2",
		},
		"datatype" : {
			text : "core.text;1.0.0"
		},
		"library" : {
			underline : "underscore;1.8.3"
		},
		"helper" : {
			operation : "operationHelper"
		}
	},
	
	//defined operation
	//in operation can access all the required resources by name through context
	operation : function(parms, context){
		
		context.helper.helper1;
		
		context.operation.op1;
		
		context.datatype.text;
		
		context.library.underline;
		
		context.operate("op1", parms, context);
		
		context.operate("dataType1", "operation1", parms, context);
		
		context.operate("dataType2", "operation2", parms, context);
	} 
};

nosliw.addDataTypeDefinition(dataTypeDefition);

