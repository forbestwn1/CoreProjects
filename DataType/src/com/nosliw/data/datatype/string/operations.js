//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("base.string");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)
dataTypeDefition.localRequires = {
	"operation" : { 
		op: "core.text;1.0.0;operation"
	},
	"datatype" : {
	},
	"library" : {
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
dataTypeDefition.operations['length'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			
			var baseData = parms[base];
//			var out = context.buildFromLiterate("base.integer", baseData.data.length);
			return out;
		}, 

//define required resources for operation
	requires:{
		"operation" : { 
			op1: "base.integer;new",
		},
//		"datatype1" : {
//			integer : "base.integer"
//		},
		"helper" : {
			operation : "operationHelper"
		}
	},
	
	info : {
	}
	
};

nosliw.addDataTypeDefinition(dataTypeDefition);
