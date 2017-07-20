if(nosliw===undefined) 
{
var nosliw = function(){
	
	var loc_moduleName = "nosliw";
	
	var loc_packages = {}; 
	
	var loc_modules = [];
	
	var loc_out = {
		getPackage : function(packageName){
			return createPackage(packageName);
		},
		
		getNode : function(nodePath){
			var nodePathInfo = parseNodePath(nodePath);
			var packageObj = createPackage(nodePathInfo.path);
			return packageObj.useNode(nodePathInfo.name);
		},
		
		getNodeData : function(nodePath){
			return this.getNode(nodePath).getData();
		},
		
		createNode : function(nodePath, nodeData){
			var node = this.getNode(nodePath);
			node.setData(nodeData);
		},
		
		//callBackFunction(nodeName, eventName, nodeValue)
		registerNodeEvent : function(nodeName, eventName, callBackFunction){
			
		},
		
		triggerNodeEvent : function(nodeName, eventName){
			
		},
		
		registerModule : function(module, packageObj){
			loc_modules.push([module, packageObj]);
		},
		
		initModules : function(){
			//set logging object
			nosliw.logging = loc_out.getNodeData("service.loggingservice.createLoggingService")();

			//execute start callback method of each module 
			for(var i in loc_modules){
				var module = loc_modules[i];
				module[0].start(module[1]);
			}
		}
	};
	
	var createPackage = function(path){
		//check if the package exists or not
		var packageObj = loc_packages[path];
		//if so, use the existing one
		if(packageObj!=undefined)  return packageObj;
		
		var loc_path = path;
		var loc_nodes = {};
		
		var loc_package = {

			prv_createNode : function(nodeName, nodeData){
				var nodeObj = loc_nodes[nodeName];
				if(nodeObj==undefined){
					//if node does not exists, create empty one
					nodeObj = createNode();
					loc_nodes[nodeName] = nodeObj;
				}
				nodeObj.setData(nodeData);
				return nodeObj;
			},
			
			prv_getNode : function(nodeName){
				return loc_nodes[nodeName];
			},
			
			getPackage : function(path){
				if(path===undefined)  return this;
				return createPackage(path);
			},
			getChildPackage : function(relativePath){
				if(relativePath==undefined)  return this;
				return createPackage(loc_path+"."+relativePath);
			},
			useNode : function(nodePath){
				var nodePathInfo = parseNodePath(nodePath);
				var packageObj = this.getPackage(nodePathInfo.path);
				var nodeObj = packageObj.prv_getNode(nodePathInfo.name);
				if(nodeObj==undefined){
					//if node does not exists, create empty one
					nodeObj = packageObj.prv_createNode(nodePathInfo.name);
				}
				return nodeObj;
			},
			requireNode : function(nodePath){
				return this.useNode(nodePath);
			},
			getNodeData : function(nodePath){
				var out = this.useNode(nodePath).getData();
				if(out===undefined)  nosliw.logging.error(loc_moduleName, "The node", nodePath, "cannot find by package" + this.getName());  
				return out;
			},
			createNode : function(nodePath, nodeData){
				var nodePathInfo = parseNodePath(nodePath);
				var nodePackage = this.getChildPackage(nodePathInfo.path);
				var nodeName = nodePathInfo.name;
				return nodePackage.prv_createNode(nodeName, nodeData);
			},
			getName : function(){return loc_path;}
		};
		loc_packages[path] = loc_package;
		//every package is a event source
		_.extend(loc_package, Backbone.Events);
		return loc_package;
	};
	
	//Create node, node is wrapper of data
	//Sometimes, node is created, but data will set later
	//It happens during build dependency. 
	//A module require another module make required node build, but at that time, the module data does not create yet
	var createNode = function(){
		var loc_data;
		
		var loc_node = {
			getData : function(){
				return loc_data;
			},
			setData : function(data){
				loc_data = data;
			}
		}
		//every node is a event source
		_.extend(loc_node, Backbone.Events);
		return loc_node;
	};
	
	var parseNodePath = function(nodePath){
		var path;
		var name;
		var index = nodePath.lastIndexOf(".");
		if(index===-1){
			name = nodePath;
		}
		else{
			name = nodePath.substring(index+1);
			path = nodePath.substring(0, index);
		}
		return {
			name : name,
			path : path
		}
	}
	
	return loc_out;
}();
}
