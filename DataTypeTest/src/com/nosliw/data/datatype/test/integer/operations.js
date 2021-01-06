//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.integer");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define operation
dataTypeDefition.operations['add'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var parm1 = parms.getParm("data1").value;
			var parm2 = parms.getParm("data2").value;
			var out = parm1 +  parm2;
			return {
				dataTypeId : "test.integer;1.0.0",
				value : out,
			}
		},
};

dataTypeDefition.operations['largerThan'] = {
		operation : function(parms, context){
			var base = this.value;
			var data = parms.getParm("data").value;

			if(base>data){
				return {
					dataTypeId : "test.boolean;1.0.0",
					value : true
				};
			}
			else{
				return {
					dataTypeId : "test.boolean;1.0.0",
					value : false
				};
			}
		},
};

dataTypeDefition.operations['noLessThan'] = {
		operation : function(parms, context){
			var base = this.value;
			var data = parms.getParm("data").value;

			if(base>data){
				return {
					dataTypeId : "test.boolean;1.0.0",
					value : false
				};
			}
			else{
				return {
					dataTypeId : "test.boolean;1.0.0",
					value : true
				};
			}
		},
};

dataTypeDefition.operations['equals'] = {
		operation : function(parms, context){
			var base = this.value;
			var data = parms.getParm("data").value;

			if(base==data){
				return {
					dataTypeId : "test.boolean;1.0.0",
					value : true
				};
			}
			else{
				return {
					dataTypeId : "test.boolean;1.0.0",
					value : false
				};
			}
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
