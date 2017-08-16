//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node
	
//*******************************************   Start Node Definition  ************************************** 	

var node_wrapperFactory = function(){
	
	var loc_factoryFuns = {};
	
	var loc_out = {
		
		/*
		 * Register wrapper factory by data type,
		 * Different type of data have different wrapper implementation
		 * For instance: object, data
		 */	
		registerWrapperFactoryByDataType : function(dataType, factoryFun){
			loc_factoryFuns[dataType] = factoryFun;
		},
		
		/*
		 * parent wrapper + path
		 * data + path 
		 */	
		createWrapper : function(data, path, request){
			if(data==undefined)   return undefined;
			
			var dataType = undefined;
			
			var entityType = nosliwTypedObjectUtility.getObjectType(data);
			if(entityType==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
				//
				dataType = data.getDataType();
			}
			else{
				if(dataType==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VALUE){
					dataType = NOSLIWCONSTANT.WRAPPER_TYPE_OBJECT;
				}
			}
			
			var wrapper = undefined;
			wrapper = loc_factoryFuns[dataType].call();
			
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


//Register Node by Name
packageObj.createChildNode("wrapperFactory", node_wrapperFactory); 

})(packageObj);
