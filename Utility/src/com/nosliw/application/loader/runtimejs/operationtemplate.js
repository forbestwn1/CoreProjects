var operationFunction = function(nosliw){
||operations||
};


if(nosliw==undefined){
	var nosliw = function(){
		var loc_dataTypes = {};
		
		var loc_out = {
			getDataTypeDefinition : function(dataType, version){
				var out = this.newDataType();
				out.dataTypeName = this.getDataTypeName(dataType, version);
				out.dataType = {
					name : dataType,
					version : version
				};
				return out;
			},
			
			addDataTypeDefinition : function(dataTypeDef){
				var dataTypeName = dataTypeDef.dataTypeName;
				
				var dataType = loc_dataTypes[dataTypeName];
				if(dataType==undefined){
					dataType = this.newDataType();
					dataType.dataType = dataTypeDef.dataType;
					loc_dataTypes[dataTypeName] = dataType;
				}
				
				var globalRequires = dataTypeDef.requires;
				var localRequires = dataTypeDef.localRequires;
				
				dataType.requires = this.mergeRequires(dataType.requires, globalRequires);
				
				for(var prop in dataTypeDef.operations){
					if(dataTypeDef.operations.hasOwnProperty(prop)){
						dataType.operations[prop] = this.processOperation(dataTypeDef.operations[prop], localRequires);
					}
				}
				
				//process converter
				if(dataTypeDef.convert!=undefined){
					dataType.convert = this.processOperation(dataTypeDef.convert, localRequires);
				}
			},
			
			processOperation : function(operationDef, localRequires){
				var operation = this.newDataTypeOperation();

				//merge requires from local 
				operation.requires = this.mergeRequires(operation.requires, localRequires);
				
				//merge requires from operation definition
				operation.requires = this.mergeRequires(operation.requires, operationDef.requires);
				
				//operation
				operation.operation = operationDef.operation;
				
				return operation;
			},
			
			getDataTypeName : function(dataType, version){
				return dataType+'--'+version;
			},
			
			newDataTypeOperation : function(){
				var dataTypeOperation = {
					operation : {},
					requires : {},
				};
				return dataTypeOperation;
			},
			
			newDataType : function(){
				var dataType = {
						requires:{},
						operations:{},
				};
				return dataType;
			},
			
			mergeRequires : function(host, guest){
				var out = {};
				for(var require in host){
					if(host.hasOwnProperty(require)){
						out[require] = host[require];
					}
				}
				for(var require in guest){
					if(guest.hasOwnProperty(require)){
						if(out[require]==undefined)  out[require] = {};
						this.copyAttribute(out[require], guest[require]);
					}
				}
				return out;
			},
			
			copyAttribute : function(host, guest){
				for(var attr in guest){
					if(guest.hasOwnProperty(attr)){
						host[attr] = guest[attr];
					}
				}
			},
			
			getDataTypes : function(){
				return loc_dataTypes;
			}
		}
		return loc_out;
	}();
}

operationFunction(nosliw);
