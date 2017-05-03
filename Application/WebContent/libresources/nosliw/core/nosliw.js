if(nosliw===undefined)    var nosliw = {};

var nosliw = function(){
	
	var loc_packages = {}; 
	
	
	
	var loc_out = {
		getPackage : function(packageName){
			return createPackage(packageName);
		},
		
		getNode : function(nodePath){
			var nodePathInfo = parseNod(nodePath);
			var packageObj = createPackage(nodePathInfo.path);
			return packageObj.useNode(nodePathInfo.name);
		},
		
		createNode : function(nodePath, nodeData){
			var node = this.getNode(nodePath);
			node.setData(nodeData);
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
				
			getPackage : function(path){
				return createPackage(path);
			},
			getChildPackage : function(relativePath){
				if(relative)
				return createPackage(loc_path+"."+relativePath);
			},
			useNode : function(nodePath){
				var nodePathInfo = parseNodePath(nodePath);
				var packageObj = this.getPackage(nodePathInfo.path);
				var nodeObj = packageObj[nodePathInfo.name];
				if(nodeObj==undefined){
					//if node does not exists, create empty one
					nodeObj = createNode();
					packageObj[nodePathInfo.name] = nodeObj;
				}
				return nodeObj;
			},
			requireNode : function(nodePath){
				return this.useNode(nodePath);
			},
			createNode : function(nodePath, nodeData){
				var nodePathInfo = parseNodePath(nodePath);
				var nodePackage = this.getChildPackage(nodePathInfo.path);
				var nodeName = nodePathInfo.name;
				return nodePackage.prv_createNode(nodeName, nodeData);
			},
		};
		loc_packages[path] = loc_package;
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
	}
	
	return loc_out;
}();
