//get/create package
var packageObj = library.getChildPackage("wrapper.object");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_basicUtility;
var node_parseSegment;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_utility = {
	
		handleContextContainerEachElement : function(context, contextPath, handler, that){
			var contextValue = context[contextPath.name];
			var type = contextValue.type;
			if(type==CONTEXT_TYPE_ENTITY){
				var containerPath = cascadePath(contextValue.path, contextPath.path);
				var containerWraper = getEntityAttributeWraperByPath(contextValue.data, containerPath);
				var containerData = containerWraper.data;
				for (var key in containerData) {
				    if (containerData.hasOwnProperty(key)) {
				    	var eleWraper = containerData[key];
				    	
						var categary = getWraperDataCategary(eleWraper);
						if(categary=='reference'){
							eleWraper =  NosliwEntityManager.getEntity(eleWraper.data);
					    	var contextEle = createContextElement(eleWraper); 
					    	handler.call(that, key, eleWraper, contextEle);
						}
						else{
					    	var contextEle = createContextElement(contextValue.data, cascadePath(containerPath, key)); 
					    	handler.call(that, key, eleWraper, contextEle);
						}
				    }
				}	
			}
			else if(type==CONTEXT_TYPE_CONTAINER){
				var containerWraper = contextValue.data;
				var container = containerWraper.container;
				var keyArray = container.getKeyArray();
				for (var index in keyArray) {
					var key = keyArray[index];
					var eleWraper = container.getDataByKey(key);
			    	var contextEle = createContextElement(eleWraper); 
			    	handler.call(that, key, eleWraper, contextEle);
				}	
			}
			else if(type==CONTEXT_TYPE_DATA){
				var containerData = contextValue.data.data;
				var categary = getDataCategary(containerData);
				if(categary=='container'){
					_.each(containerData.data, function(data, key, list){
						var contextEle = createContextElementData(createDataWraper(data));
				    	handler.call(that, key, data, contextEle);
					}, this);
				}
			}
			else if(type==CONTEXT_TYPE_OBJECT){
				var containerPath = cascadePath(contextValue.path, contextPath.path);
				var containerData = getObjectAttributeByPath(contextValue.data.data, containerPath);
				
				_.each(containerData, function(data, key, list){
					var contextEle = createContextElementObject(contextValue.data, cascadePath(containerPath, key));
			    	handler.call(that, key, data, contextEle);
				}, this);
			}
		},

};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
