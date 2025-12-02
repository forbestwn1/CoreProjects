{
	"info": {
		"name": "test.expression",
		"description": "test expression group entity"
	},
	"entity": {
		"valueContext" : {
			"entity": [
				{
					"scope" : "public",
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
		},
		"expression": "!(test.string)!.subString(base:?(localBase)?,from:?(localParm1)?,to:?(localParm2)?)",
		"attachment": {
			"extra": {
				"status": "disabled1",
				"name": "parent attachment" 
			},
			"entity": {
				"data" : [
					{
						"name" : "constantBase",
						"entity": {
							"dataTypeId": "test.string;1.0.0",
							"value": "012345678901234567890"
						}
					},
					{
						"name" : "constantFrom",
						"entity": {
							"dataTypeId": "test.integer;1.0.0",
							"value": 5
						}
					},
					{
						"name" : "constantTo",
						"entity": {
							"dataTypeId": "test.integer;1.0.0",
							"value": 7
						}
					}
				]
			}
		}
	}
}