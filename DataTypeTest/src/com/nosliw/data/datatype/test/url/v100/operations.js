//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefinition = nosliw.getDataTypeDefinition("test.url","1.0.0");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefinition.requires = {
};

dataTypeDefinition.convert = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(data, toDataType, reverse, context){
			var prefix = 'http:';
			if(toDataType=="test.string;1.0.0"){
				if(reverse==false){
					return {
						dataTypeId : "test.string;1.0.0",
						value : prefix+data.value,
						info : {
							fromUrl : true
						}
					};
				}
				else{
					var value = data.value;
					var info = data.info;
					if(info!=undefined&&info.fromUrl==true){
						var index = value.indexOf(prefix);
						if(index==0){
							value = value.substring(prefix.length);
						}
					}
					return {
						dataTypeId : "test.url;1.0.0",
						value : value
					};
				}
			}
			return data;
		} 
};

nosliw.addDataTypeDefinition(dataTypeDefinition);
