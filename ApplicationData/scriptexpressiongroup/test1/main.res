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
								"value": "default value of baseVarNormal"
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
				"name" : "text",
				"status": "disabled1",
				"type" : "text",
				"expression" : "hello world"
			},
			{
				"name" : "literate_script",
				"status": "disabled1",
				"type" : "literate",
				"expression" : "HaHaHa  <%=(&(baseVarNormal)&.value+&(parm2)&)+&(parm1)&%>  End!!!!"
			},
			{
				"name" : "literate_datascript",
				"status": "disabled1",
				"type" : "literate",
				"expression" : "HaHaHa  <%=#|!(test.string)!.subString(?(baseVarNormal)?,from:?(parm1)?,to:?(parm2)?)|#.value+&(parm2)&)+&(parm1)&%>  End!!!!"
			},
		],
	}
}
