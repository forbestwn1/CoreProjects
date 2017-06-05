//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefinition = nosliw.getDataTypeDefinition("core.url", "1.1.0");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefinition.requires = {
	"operation" : { 
		op: "core.text;1.0.0;text_normal3"
	},
//	"datatype1" : {
//		text : "core.text;1.0.0"
//	},
	"library" : {
		underline : "underscore;1.8.3"
	},
	"helper" : {
		globalHelper : {
			a : "aa",
			b : function(parm1){
				parm1 = parm1 + "aaa";
			}
		}
	}
};

//define what operations in this page requires (operation, datatype, library)
dataTypeDefinition.localRequires = {
	"operation" : { 
		op: "core.text;1.0.0;operation"
	},
//	"datatype1" : {
//		text : "core.text;1.0.0"
//	},
	"library" : {
		underline : "underscore;1.8.3"
	},
	"helper" : {
		globalHelper : {
			c : "cc",
			d : function(parm1){
				parm1 = parm1 + "fff";
			}
		}
	}
};


//define operation
dataTypeDefinition.operations['url_normal2'] = {
	//define required resources for operation
	requires:{
		"operation" : { 
			op1: "core.text;1.0.0;text_normal1",
			op2: "core.text;1.0.0;text_normal2",
		},
//		"datatype1" : {
//			text : "core.text;1.0.0"
//		},
		"library" : {
			underline : "underscore;1.8.3"
		},
		"helper" : {
			operation : "operationHelper"
		}
	},
	
	info : {
	},
	
	//defined operation
	//in operation can access all the required resources by name through context
	operation : function(parms, context){
		
		context.helper.helper1;
		
		context.operation.op1;
		
		context.datatype.text;
		
		context.library.underline;
		
		context.executeOperation("dataType1", "operation1", parms, context);
		
		context.executeOperation("dataType2", "operation2", parms, context);
	} 
};

dataTypeDefinition.convertTo = {
		//define required resources for operation
		requires:{
			"operation" : { 
				op1: "core.text;1.0.0;text_normal1",
				op2: "core.text;1.0.0;text_normal2",
			},
//			"datatype1" : {
//				text : "core.text;1.0.0"
//			},
			"library" : {
				underline : "underscore;1.8.3"
			},
			"helper" : {
				operation : "operationHelper"
			}
		},
		
		info : {
			type : "convertTo"
		},
		
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(data, toDataType, context){
			"conertTo";
		} 
};

dataTypeDefinition.convertFrom = {
		//define required resources for operation
		requires:{
			"operation" : { 
				op1: "core.text;1.0.0;text_normal1",
				op2: "core.text;1.0.0;text_normal2",
			},
//			"datatype1" : {
//				text : "core.text;1.0.0"
//			},
			"library" : {
				underline : "underscore;1.8.3"
			},
			"helper" : {
				operation : "operationHelper"
			}
		},
		
		info : {
			type : "convertFrom"
		},
		
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(data, toDataType, context){
			"conertFrom";
		} 
};

dataTypeDefinition.fromLiterate = {
		
};

dataTypeDefinition.toLiterate = {
		
};

dataTypeDefinition.fromJson = {
		
};

dataTypeDefinition.toJson = {
		
};

nosliw.addDataTypeDefinition(dataTypeDefinition);

