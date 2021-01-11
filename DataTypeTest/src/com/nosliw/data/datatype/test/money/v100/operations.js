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

dataTypeDefinition.convert = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(data, toDataType, reverse, context){
			var info = data.info;
			if(reverse){
				//from float
				var currency;
				if(info!=undefined){
					currency = info.fromMoney;
					delete info.fromMondy;
				}
				return {
					dataTypeId : "test.money;1.0.0",
					value : {
						amount : data.value,
						currency : currency
					}
				};
			}
			else{
				//to float
				if(info==undefined)  info = {};
				info.fromMoney = data.value.currency;
				
				return {
					dataTypeId : "test.float;1.0.0",
					value : data.value.amount,
					info : info
				};
			}
		} 
};


nosliw.addDataTypeDefinition(dataTypeDefinition);
