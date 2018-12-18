if(nosliw===undefined) 
{
	var nosliw = {};
}

_.extend(nosliw, function(){
	
	var loc_moduleName = "nosliw";
	
	var loc_nodes = {}; 
	
	var loc_modules = [];
	
	var loc_out = {
		getPackage : function(packageName){
			return createPackage(packageName);
		},
		
		getNode : function(nodePath){
			return this.getPackage(nodePath).useNode();
		},
		
		getNodeData : function(nodePath){
			return this.getNode(nodePath).getData();
		},
		
		createNode : function(nodePath, nodeData){
			var node = this.getNode(nodePath);
			node.setData(nodeData);
		},
		
		//callBackFunction(eventName, nodeName), this : node
		registerNodeEvent : function(nodeName, eventName, callBackFunction){
			var node = this.getNode(nodeName);
			if(eventName=="all"){
				node.on(eventName, function(eventName, eventName, nodeName){
					callBackFunction.call(node, event, nodeName);
				}, node);
			}
			else{
				node.on(eventName, callBackFunction, node);
			}
		},

		registerSetNodeDataEvent : function(nodeName, callBackFunction){
			var node = this.getNode(nodeName);
			var nodeData = node.getData();
			if(nodeData!=undefined){
				callBackFunction.call(node, this.NODEEVENT_SETDATA, nodeName);
			}
			this.registerNodeEvent(nodeName, this.NODEEVENT_SETDATA, callBackFunction)
		},
		
		triggerNodeEvent : function(nodeName, eventName){
			this.getNode(nodeName).trigger(eventName, eventName, nodeName);
		},
		
		registerModule : function(module, packageObj){
			loc_modules.push([module, packageObj]);
		},
		
		initModules : function(){
			//execute start callback method of each module 
			for(var i in loc_modules){
				var module = loc_modules[i];
				module[0].start(module[1]);
			}
		},
		
		error : function(errorData){
			console.error(JSON.stringify(errorData));
		}
	};
	
	var createPackage = function(path){
		
		var loc_path = path; 
		
		var loc_package = {

				prv_useNode : function(){
					var node = loc_nodes[this.getName()];
					if(node==undefined){
						//if node does not exists, create empty one
						node = createNode(this.getName());
						loc_nodes[this.getName()] = node;
					}
					return node;
				},
				
				prv_setNodeData : function(nodeData){
					var node = this.prv_useNode();
					node.setData(nodeData);
					return node;
				},
				
				getPackage : function(path){
					if(path===undefined)  return this;
					return createPackage(path);
				},
				getChildPackage : function(relativePath){
					if(relativePath==undefined)  return this;
					return createPackage(loc_path+"."+relativePath);
				},
				useNode : function(path){
					var childPackage = this.getPackage(path);
					return childPackage.prv_useNode();
				},
				useChildNode : function(relativePath){
					var childPackage = this.getChildPackage(relativePath);
					return childPackage.prv_useNode();
				},
				setNodeData : function(path, nodeData){
					return this.getPackage(path).prv_setNodeData(nodeData);
				},
				setChildNodeData : function(path, nodeData){
					return this.getChildPackage(path).prv_setNodeData(nodeData);
				},
				createNode : function(path, nodeData){
					var nodePackage = this.getPackage(path);
					return nodePackage.prv_setNodeData(nodeData);
				},
				createChildNode : function(relativePath, nodeData){
					var nodePackage = this.getChildPackage(relativePath);
					return nodePackage.prv_setNodeData(nodeData);
				},
				getNodeData : function(path){
					var out = this.useNode(path).getData();
					if(out===undefined)  nosliw.logging.error(loc_moduleName, "The node", path, "cannot find by package : " + this.getName());  
					return out;
				},
				getChildNodeData : function(relativePath){
					var out = this.useChildNode(relativePath).getData();
					if(out===undefined)  nosliw.logging.error(loc_moduleName, "The node", relativePath, "cannot find by package : " + this.getName());  
					return out;
				},
				getName : function(){return loc_path;}
			};
			return loc_package;
		
	};
	
	//Create node, node is wrapper of data
	//Sometimes, node is created, but data will set later
	//It happens during build dependency. 
	//A module require another module make required node build, but at that time, the module data does not create yet
	var createNode = function(packageName){
		var loc_data;
		var loc_packageName = packageName;
		
		var loc_node = {
			getData : function(){
				return loc_data;
			},
			setData : function(data){
				loc_data = data;
				nosliw.triggerNodeEvent(this.getPackageName(), loc_out.NODEEVENT_SETDATA);
			},
			getPackageName : function(){
				return loc_packageName;
			}
		}
		//every node is a event source
		_.extend(loc_node, Backbone.Events);
		return loc_node;
	};
	
	loc_out.NODEEVENT_SETDATA = "setData";
	
	//set logging object
	loc_out.registerSetNodeDataEvent("service.loggingservice.createLoggingService", function(){	nosliw.logging = this.getData()();	});
	
	return loc_out;
}());

