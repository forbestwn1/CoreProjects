{
	"info": {
		"name": "test.expression",
		"description": "test expression group entity"
	},
	"entity": {
		"valueContext" : {
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"baseVarNormal": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of testvar3"
							}
						},
						"baseVarMatcher": {
							"definition":{
								"criteria": "test.url;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.url;1.0.0",
								"value": "default value of testvar3"
							}
						},
						"parm1" : {
							"definition": {
								"criteria" : "test.integer"
							},
							"defaultValue": {
								"dataTypeId": "test.integer;1.0.0",
								"value": 5
							}
						},
						"parm2" : {
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
		},
		"element": [
			{
				"name" : "normalized",
				"expression" : "!(test.string)!.subString(base:?(baseVarNormal)?,from:?(parm1)?,to:?(parm2)?)"
			},
			{
				"name" : "normalized1",
				"expression" : "?(baseVarNormal)?.subString(from:?(parm1)?,to:?(parm2)?)"
			},
			{
				"name" : "normal",
				"expression" : "!(test.string)!.subString(?(baseVarNormal)?,from:?(parm1)?,to:?(parm2)?)"
			},
			{
				"name" : "matcher",
				"expression" : "!(test.string)!.subString(?(baseVarMatcher)?,from:?(parm1)?,to:?(parm2)?)"
			},
		]		
	}
}
