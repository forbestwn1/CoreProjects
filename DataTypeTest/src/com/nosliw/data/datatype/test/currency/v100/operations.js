//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefinition = nosliw.getDataTypeDefinition("test.money");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefinition.requires = {
};

//define what operations in this page requires (operation, datatype, library)
dataTypeDefinition.localRequires = {
	"operation" : { 
//		op: "base.text;1.0.0;operation"
	},
	"datatype" : {
	},
	"library" : {
	},
	"helper" : {
		globalHelper : {
			a : 123,
			b : "bb",
			c : "cc",
			d : function(parm1){
				return parm1 = parm1 + "fff";
			},
			e : [1, 2, 3, 4],
			f : true,
			h : [true, false, true]
		}
	}
};

nosliw.addDataTypeDefinition(dataTypeDefinition);
