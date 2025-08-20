//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_getObjectType;
var node_createWraperCommon;
var node_getObjectId;
var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_wrapperFactory = function(){
	
	var loc_dataTypeHelpers = {};
	
	var loc_out = {
		
		/*
		 * Register wrapper factory by data type,
		 * Different type of data have different wrapper implementation
		 * One wrapper type may support different data type
		 * For instance: object, data
		 */	
		registerDataTypeHelper : function(dataTypeIds, dataTypeHelper){
			_.each(dataTypeIds, function(dataTypeId, index){
				loc_dataTypeHelpers[dataTypeId] = dataTypeHelper;
			});
		},
		

		/*
		 * parent wrapper + path
		 * data
		 * value + dataType 
		 */	
		createWrapper : function(parm1, parm2, requestInfo){
			var wrapperParm1;
			var dataType = undefined;
			var path = undefined;
			
			var entityType = node_getObjectType(parm1);
			if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
				dataType = parm1.getDataType();
				wrapperParm1 = parm1;
				path = parm2;
			}
			else if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
				dataType = parm1.dataTypeInfo;
				wrapperParm1 = parm1.value;
			}
			else if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VALUE){
				dataType = parm2;
				wrapperParm1 = parm1;
			}
			else{
				dataType = parm2;
				wrapperParm1 = parm1;
			}
			
			if(node_basicUtility.isStringEmpty(dataType))   dataType = node_CONSTANT.DATA_TYPE_OBJECT;
			
			var out = node_createWraperCommon(wrapperParm1, path, this.getDataTypeHelper(dataType), dataType);

			return out;
		},
		
		getDataTypeHelper : function(dataTypeInfo){
			return loc_dataTypeHelpers[dataTypeInfo];
		},
			
	};
	
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.wrapper.createWraperCommon", function(){node_createWraperCommon = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectId", function(){node_getObjectId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("wrapperFactory", node_wrapperFactory); 

})(packageObj);
