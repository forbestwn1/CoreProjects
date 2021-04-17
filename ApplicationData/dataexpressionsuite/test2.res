{
	"name": "test2",
	"description": "test2",
	"context": {
		"element": {
			"testVar3": {
				"definition":{
					"criteria": "test.string"
				},
				"defaultValue": {
					"dataTypeId": "test.string",
					"value": "9876543210"
				}
			},
			"constantFromContext1": {
				"definition" : {
					"value" : {
						"dataTypeId": "test.integer",
						"value": 1
					}
				}
			},
			"constantFromContext2": {
				"definition" : {
					"value" : {
						"dataTypeId": "test.integer",
						"value": 3
					}
				}
			}
		}
	},
	"element": [{
			"id": "test10",
			"name": "test10",
			"description": "standard",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(?(testVar3)?,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		},
		{
			"id": "test11",
			"name": "test11",
			"description": "discover base, constant",
			"element" : [
				{
					"expression" : "?(testVar3)?.subString(from:&(constantFromAtt1)&,to:&(constantFromContext2)&)"
				}
			]
		}
	],
	"attachment": {
		"dataexpression" : [
		],
		"value" : [
			{
				"name": "constantFromAtt1",
				"entity": {
					"value" : {
						"dataTypeId": "test.integer;1.0.0",
						"value": 1
					}
				}
			},
			{
				"name": "constantFromAtt2",
				"entity": {
					"value" : {
						"dataTypeId": "test.integer;1.0.0",
						"value": 3
					}
				}
			}
		],
		"testData" : [
			{
				"name": "testData1",
				"entity": {
					"testVar1.var1.var11": {
						"dataTypeId": "test.string;1.0.0",
						"value": "0123456789"
					},
					"testVar2": {
						"var1" : {
							"var11":{
								"dataTypeId": "test.string;1.0.0",
								"value": "0123456789"
							}
						}
					},
					"testVar3": {
						"dataTypeId": "test.string;1.0.0",
						"value": "0123456789"
					},
					"testVar4": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 1
					},
					"testVar5": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 3
					}
				}
			}
		]
	}
}
