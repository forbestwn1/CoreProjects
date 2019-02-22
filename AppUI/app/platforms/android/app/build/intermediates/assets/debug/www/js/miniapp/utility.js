//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoCommon;
	var node_ServiceInfo;
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;

//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	loc_out = {
			
		getLoadTemplateRequest : function(files, handlers, requestInfo){
			var out = node_createServiceRequestInfoSet(new node_ServiceInfo("LoadTemplates", {"files":files}), handlers, requestInfo); 
			
			_.each(files, function(file, index){
				var templateRequest = node_createServiceRequestInfoCommon(new node_ServiceInfo("LoadTemplate", {}));
				templateRequest.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
					$.get(file)
					  .done((source) => {
						  requestInfo.executeSuccessHandler(source, templateRequest);
					});
				}, this));
				out.addRequest(file, templateRequest);
			});

			out.setRequestProcessors({
				success : function(requestInfo, resultSet){
					return resultSet.getResults();
				}
			});
			return out;
		},
		
		getBuildTemplateRequest : function(templatesInfo, handlers, requestInfo){
			var templates = {};
			var templatesFile = [];
			var templatesInfoByFile = {};
			_.each(templatesInfo, function(templateInfo, index){
				templatesFile.push(templateInfo.file);
				templatesInfoByFile[templateInfo.file] = templateInfo;
				templates[templateInfo.name] = templateInfo;
			});
			
			var out = node_createServiceRequestInfoService(new node_ServiceInfo("BuildTemplate", {}), handlers, requestInfo);
			var requestDependency = new node_DependentServiceRequestInfo(this.getLoadTemplateRequest(templatesFile), {
				success : function(requestInfo, templateSources){
					_.each(templateSources, function(source, fileName){
						templates[templatesInfoByFile[fileName].name].template = Handlebars.compile(source); 
					});
					return templates;
				}
			});
			out.setDependentService(requestDependency);
			return out; 
		}
	};
	
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
