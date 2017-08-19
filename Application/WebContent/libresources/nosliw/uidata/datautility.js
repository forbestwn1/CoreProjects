//get/create package
var packageObj = library.getChildPackage("data");    

(function(packageObj){
//get used node
var node_makeObjectWithType;
	
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_utility = function(){
	
	return {
		/*
		 * create object data by value
		 * if object is already data, then do nothing
		 * otherwise, wraper it to data 
		 */
		createDataByObject : function(object){
			var out = object;
			if(nosliwTypedObjectUtility.getObjectType(object)!=NOSLIWCONSTANT.TYPEDOBJECT_TYPE_DATA){
				out = this.createDataByValue(object);
			}
			return out;
		},
		
		/*
		 * create object data by value 
		 */
		createDataByValue : function(value){
			return nosliwCreateData(value, nosliwObjectUtility.dataTypeInfo);
		},
		
		createEmptyData : function(){
			return nosliwCreateData("");
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
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});


//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
