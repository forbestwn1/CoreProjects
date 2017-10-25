//get/create package
var packageObj = library.getChildPackage("data");    

(function(packageObj){
//get used node
var node_getObjectType;
var node_makeObjectWithType;
var node_CONSTANT;	
var node_createData;
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_utility = function(){
	
	return {
		
		createDataOfAppData : function(appData){
			var out = node_createData(appData, node_CONSTANT.DATA_TYPE_APPDATA);
			return out;
		},
		
		createDataOfObject : function(obj){
			var out = node_createData(obj, node_CONSTANT.DATA_TYPE_OBJECT);
			return out;
		},
		
	
		/*
		 * create object data by value
		 * if object is already data, then do nothing
		 * otherwise, wraper it to data 
		 */
		createDataByObject : function(object, dataTypeInfo){
			var out = object;
			if(node_getObjectType(object)!=node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
				out = this.createDataByValue(object, dataTypeInfo);
			}
			return out;
		},
		
		/*
		 * create object data by value 
		 */
		createDataByValue : function(value, dataTypeInfo){
			var out;
			if(dataTypeInfo!=undefined){
				out = node_createData(value, dataTypeInfo);
			}
			else{
				out = node_createData(value, node_CONSTANT.DATA_TYPE_OBJECT);
			}
			return out;
		},
		
		createEmptyData : function(){
			return node_createData("");
		},
		
		isEmptyData : function(data){
			if(data==undefined)  return true;
			if(data.dataTypeInfo==undefined)  return true;
			return false;
		},
		
	};	
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.entity.createData", function(){node_createData = this.getData();});


//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
