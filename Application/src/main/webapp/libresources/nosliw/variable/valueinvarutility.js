//get/create package
var packageObj = library.getChildPackage("valueinvar");    

(function(packageObj){
//get used node
var node_getObjectType;
var node_makeObjectWithType;
var node_CONSTANT;	
var node_createValueInVar;
var node_namingConvensionUtility;
var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_utility = function(){
	
	return {

		isDataEqual : function(data1, data2){
			if(data1==undefined && data2==undefined)   return true;
			if(data1==undefined || data2==undefined)   return false;
			if(node_basicUtility.isValueEqual(data1.dataTypeInfo, data2.dataTypeInfo)){
				if(node_basicUtility.isValueEqual(data1.value, data2.value)){
					return true;
				}
			}
			return false;
		},
		
		isCyclic : function(original) {
			  var seenObjects = [];
			  var seenPaths = [];

			  function detect (obj, path) {
			    if (obj && typeof obj === 'object') {
			    	var index = seenObjects.indexOf(obj);
			      if ( index!== -1 && path.startsWith(seenPaths[index])) {
			        return true;
			      }
			      seenObjects.push(obj);
			      seenPaths.push(path);
			      for (var key in obj) {
			        if (obj.hasOwnProperty(key) && detect(obj[key], path+"."+key)) {
			        	console.log(original);
			        	console.log('cycle at ' + path+"."+key);
			        	console.log('cycle at ' + seenPaths[seenObjects.indexOf(obj[key])]);
			        	
			        	
			          return true;
			        }
			      }
			    }
			    return false;
			  }

			  return detect(original, "");
		},
		
		cloneValue : function(value){
			if(value==undefined)  return undefined;
			try{
				var copy = JSON.parse(node_basicUtility.stringify(value));
			}
			catch(e){
				this.isCyclic(value);   //kkkk
				copy = value;
			}
			return copy;
		},
		
		/**
		 * compare two path
		 * if path1 equals path2, then 0
		 * if path1 contains path2, then 1
		 * if path2 contains path1, then 2
		 * otherwise, -1
		 */
		comparePath : function(path1, path2){
			var compare = 0;
			var result = "";
			var p1 = node_basicUtility.emptyStringIfUndefined(path1)+"";
			var p2 = node_basicUtility.emptyStringIfUndefined(path2)+"";
			if(p1==p2){
				compare = 0;
				result = p1; 
			}
			else if(node_basicUtility.isStringEmpty(p1)){
				compare = 2;
				result = p2;
			}
			else if(node_basicUtility.isStringEmpty(p2)){
				compare = 1;
				result = p1;
			}
			else if(p1.startsWith(p2+".")){
				compare = 1;
				result = p1.substring((p2+".").length);
			}
			else if(p2.startsWith(p1+".")){
				compare = 2;
				result = p2.substring((p1+".").length);
			}
			else{
				compare = -1;
				result = undefined;
			}
			return {
				compare : compare,
				subPath : result
			};
		},
		
		combinePath : function(){
			var out = "";
			_.each(arguments, function(seg, index){
				out = node_namingConvensionUtility.cascadePath(out, seg);
			});
			return out;
		},
		
		createDataOfAppData : function(appData){
			var out = node_createValueInVar(appData, node_CONSTANT.DATA_TYPE_APPDATA);
			return out;
		},
		
		createDataOfObject : function(obj){
			var out = node_createValueInVar(obj, node_CONSTANT.DATA_TYPE_OBJECT);
			return out;
		},
		
		createDataOfDynamic : function(obj){
			var out = node_createValueInVar(obj, node_CONSTANT.DATA_TYPE_DYNAMIC);
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
				out = node_createValueInVar(value, dataTypeInfo);
			}
			else{
				out = node_createValueInVar(value, node_CONSTANT.DATA_TYPE_OBJECT);
			}
			return out;
		},
		
		createEmptyData : function(){
			return node_createValueInVar("");
		},
		
		isEmptyData : function(data){
			if(data==undefined)  return true;
			if(data.dataTypeInfo==undefined)  return true;
			return false;
		},
		
		getValueOfData : function(data){
			var dataTypeInfo = data.dataTypeInfo;
			var out = data.value;
			if(dataTypeInfo==node_CONSTANT.DATA_TYPE_DYNAMIC){
				out = out.getValue();
			}
			return out;
		},
		
		getDataTypeInfoFromValue : function(value){
			var out;
			if(value!=undefined && value.dataTypeId!=undefined){
				out = node_CONSTANT.TYPEDOBJECT_TYPE_DATA;
			}
			else{
				out = node_CONSTANT.DATA_TYPE_OBJECT;
			}
			return out;
		}
		
	};	
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.entity.createValueInVar", function(){node_createValueInVar = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
