{
	"info": {
		"name": "test data expression",
		"description": ""
	},
	"entity": [
		{
			"info": {
				"name" : "test-none-dataexpressiongroup",
				"status": "disabled1"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "runtime"
					}
				}
			},
			"entity" : {
				"element": [
					{
						"name" : "test1",
						"expression" : "!(test.string)!.subString(?(testVar3)?,from:?(testVar4)?,to:?(testVar5)?)"
					}
				]		
			}
		},
		{
			"info": {
				"name": "valueContext",
				"status": "disabled1"
			},
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"testVar3": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of testvar3"
							}
						},
						"testVar4" : {
							"definition": {
								"criteria" : "test.integer"
							},
							"defaultValue": {
								"dataTypeId": "test.integer;1.0.0",
								"value": 5
							}
						},
						"testVar5" : {
							"definition": {
								"criteria": "test.integer"
							},
							"defaultValue": {
								"dataTypeId": "test.integer;1.0.0",
								"value": 7
							}
						}
					}
				}
			]
		}
	]
}
