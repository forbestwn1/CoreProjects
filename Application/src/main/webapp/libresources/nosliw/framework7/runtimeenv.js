//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

// indicator ui to indicate the start of request and end of request
var node_createRuntimeEnvValues = function(framework7App){
	
	var loc_framework7App = framework7App;
	var loc_framework7View;
	
	var loc_containerView = $('<div class="view view-main" style="height1:1200px;overflow-y1: scroll; "></div>');
	
	$("#moduleDiv").append(loc_containerView);
	
	var loc_getRoutePathByUiId = function(uiId){	return "/"+uiId+"/";  };
	
	var loc_createPageWrapper = function(pageName){
    	return $("<div class='page stacked' data-name="+pageName+"/>");
	};
	
	var loc_presentPage = function(parms){
		loc_framework7View.router.navigate(loc_getRoutePathByUiId(parms.page));
	};
	
	var loc_registerAllPages = function(parms){
		var pagesCoreEntitys = parms.pageCoreEntitys;

		//view configure
		var viewConfigure = {
			stackPages : true,
			routes : [],
			routesBeforeEnter : function(to, from, resolve, reject){
				resolve();
			}
		};
		
        _.each(pagesCoreEntitys, function(pageCoreEntity, name){
	    	var pageWrapperView = loc_createPageWrapper(name); 
			pageCoreEntity.updateView(pageWrapperView);
			pageWrapperView.appendTo(loc_containerView);

			var route = {};
			route.name = name;
			route.path = loc_getRoutePathByUiId(name);
			route.pageName = name;
			viewConfigure.routes.push(route);
		});

		loc_framework7View = loc_framework7App.views.create(loc_containerView, viewConfigure);
	};
	
	var loc_out = {	};
	
	loc_out["ui.registerAllPages"] = loc_registerAllPages; 
	loc_out["ui.presentPage"] = loc_presentPage; 
	
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createRuntimeEnvValues", node_createRuntimeEnvValues); 

})(packageObj);
