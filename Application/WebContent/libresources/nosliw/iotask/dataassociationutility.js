//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_dataAssociationUtility = function(){
	
	var loc_out = {

		buildDataAssociationName : function(sourceType, sourceName, targetType, targetName){
			return sourceType+"_"+sourceName+"---"+targetType+"_"+targetName;
		},
		
		//get ioData's data set value
		//ioData maybe dataSet, value, or dataAssociation
		getGetDataSetValueRequest : function(ioData, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getGetDataSetValueRequest", {}), handlers, request);
			var ioDataType = node_getObjectType(ioData);
			if(ioDataType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_IODATASET){
				//for io data set, get directly
				out.addRequest(ioData.getGetDataSetValueRequest());
			}
			else if(inputIOType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION){
				//for data association, execute data association first
				out.add(ioData.getExecuteRequest({
					success : function(request, ioDataSet){
						return ioDataSet.getGetDataSetValueRequest();
					}
				}));
			}
			else{
				//for value, build io data set first
				out.addRequest(node_createIODataSet(ioData).getGetDataSetValueRequest());
			}
			
			return out;
		},
	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("dataAssociationUtility", node_dataAssociationUtility); 

})(packageObj);
