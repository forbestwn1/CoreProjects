{
	"info": {
		"name": "test task",
		"description": ""
	},
	"entity": [
		{
			"embeded": {
				"info": {
					"name" : "adapter-task",
					"status": "disabled1"
				},
				"parent": {
					"valuestructure":{
						"inherit":{
							"mode" : "runtime"
						}
					}
				},
				"entity": {
					"type" : "dataexpressionsingle",
					"implementation" : {
						"expression": "!(test.string)!.subString(base:?(localBase)?,from:?(localParm1)?,to:?(localParm2)?)"
					}
				}
			},
			"adapter":[
			]
		},
		{
			"info": {
				"name": "valueContext",
				"status": "disabled1"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "runtime"
					}
				}
			},			
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"localBase": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "this is the default value of localBase"
							}
						},
						"localParm1" : {
							"definition": {
								"criteria" : "test.integer"
							},
							"defaultValue": {
								"dataTypeId": "test.integer;1.0.0",
								"value": 5
							}
						},
						"localParm2" : {
							"definition": {
								"criteria": "test.integer"
							},
							"defaultValue": {
								"dataTypeId": "test.integer;1.0.0",
								"value": 25
							}
						}
					}
				}
			]
		}
	]
}
