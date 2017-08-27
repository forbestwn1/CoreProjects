//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_getObjectType;
//*******************************************   Start Node Definition  ************************************** 	

var node_wrapperFactory = function(){
	
	var loc_factoryFuns = {};
	
	var loc_out = {
		
		/*
		 * Register wrapper factory by data type,
		 * Different type of data have different wrapper implementation
		 * One wrapper type may support different data type
		 * For instance: object, data
		 */	
		registerWrapperFactoryByDataType : function(dataTypeIds, factoryFun){
			_.each(dataTypeIds, function(dataTypeId, index){
				loc_factoryFuns[dataTypeId] = factoryFun;
			});
		},
		
		/*
		 * parent wrapper + path
		 * data + path 
		 */	
		createWrapper : function(data, path, request){
			if(data==undefined)   return undefined;
			
			var dataType = undefined;
			
			var entityType = node_getObjectType(data);
			if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
				dataType = data.getDataType();
			}
			else if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
				dataType = data.dataTypeInfo;
			}
			else if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_VALUE){
				dataType = node_CONSTANT.DATA_TYPE_OBJECT;
			}
			
			var wrapper = loc_factoryFuns[dataType].call();
			
			var out = _.extend(nosliwCreateWraperCommon(data, path, request), wrapper);
			
			if(out.pri_isDataBased()){
				nosliwLogging.debug("create wrapper", out.getObjectId(), "dataBased", out.pri_getPath(), out.pri_getRootData());
			}
			else{
				nosliwLogging.debug("create wrapper", out.getObjectId(), "parentBased", out.pri_getPath(), out.pri_getParent().getObjectId());
			}
			
			return out;
		}
			
	};
	
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});


//Register Node by Name
packageObj.createChildNode("wrapperFactory", node_wrapperFactory); 

})(packageObj);
