//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_getObjectType;
var node_createWraperCommon;
var node_getObjectId;
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
		createWrapper : function(parm1, path){
			var wrapperParm1;
			var dataType = undefined;
			
			var entityType = node_getObjectType(parm1);
			if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
				dataType = parm1.getDataType();
				wrapperParm1 = parm1;
			}
			else if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
				dataType = parm1.dataTypeInfo;
				wrapperParm1 = parm1.value;
			}
			else if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_VALUE){
				dataType = node_CONSTANT.DATA_TYPE_OBJECT;
				wrapperParm1 = parm1;
			}
			else{
				dataType = node_CONSTANT.DATA_TYPE_OBJECT;
				wrapperParm1 = parm1;
			}
			
			var out = node_createWraperCommon(wrapperParm1, path, loc_factoryFuns[dataType].call(), dataType);
			
//			if(out.pri_isDataBased()){
//				nosliw.logging.debug("create wrapper", node_getObjectId(out), "dataBased", JSON.stringify(out.pri_getPath()), JSON.stringify(out.pri_getRootData()));
//			}
//			else{
//				nosliw.logging.debug("create wrapper", node_getObjectId(out), "parentBased", out.pri_getPath(), node_getObjectId(out.pri_getParent()));
//			}
			
//			out.registerDataOperationEventListener(undefined, function(eventName, data){
//				nosliw.logging.info("---------------------  Wrapper data change event  ----------------");
//				nosliw.logging.info("Path : " + out.pri_path);
//				nosliw.logging.info("FullPath : " + out.getFullPath());
//				nosliw.logging.info("EventName : " + eventName);
//				nosliw.logging.info("Data : " + JSON.stringify(data));
//				nosliw.logging.info("---------------------    ----------------");
//				
//			}, out);
			
			
			return out;
		}
			
	};
	
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.createWraperCommon", function(){node_createWraperCommon = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.getObjectId", function(){node_getObjectId = this.getData();});


//Register Node by Name
packageObj.createChildNode("wrapperFactory", node_wrapperFactory); 

})(packageObj);
