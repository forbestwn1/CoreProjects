{
	"info": {
		"name": "test script task group",
		"description": ""
	},
	"entity": [
		{
			"info": {
				"name" : "test-scripttaskgroup",
				"status": "disabled1"
			},
			"entity":{
				"definition" : [
					{
						"name" : "task1",
						"requirement" : [
							{
								"interface" : "interface1"
							},
							{
								"interface" : "interface2"
							}
						]
					}
				
				],
				"script" : {
					"task1" : function(taskInput, handlers, request){
						var requirement = taskInput.getRequirement();
						var interface = requirement.interface;
						return {
							"interface1" : interface.interface1(),
							"interface2" : interface.interface2(),
						}
					},
				}
			}
		}
	]
}
