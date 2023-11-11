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
							},
						]
					}
				
				],
				"script" : {
					task1 : function(event, info, env){
						env.executeGetInvokeServiceRequest("service1", "default");
					},
				}
			}
		}
	]
}
