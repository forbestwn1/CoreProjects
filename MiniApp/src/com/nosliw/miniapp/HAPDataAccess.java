package com.nosliw.miniapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.imp.io.HAPDBSource;
import com.nosliw.miniapp.data.HAPAppDataInfo;
import com.nosliw.miniapp.data.HAPAppDataInfoContainer;
import com.nosliw.miniapp.entity.HAPGroup;
import com.nosliw.miniapp.entity.HAPMiniApp;
import com.nosliw.miniapp.entity.HAPOwnerInfo;
import com.nosliw.miniapp.entity.HAPUser;
import com.nosliw.miniapp.entity.HAPUserInfo;

public class HAPDataAccess {

	private HAPDBSource m_dbSource;

	private long i = System.currentTimeMillis();
	
	public HAPDataAccess(HAPDBSource dbSource) {
		this.m_dbSource = dbSource;
	}

	public void createSampleDataForUser(String userId) {
		try {
//			HAPGroup soccerGroup = new HAPGroup(this.generateId(), "Soccer");
//			HAPGroup schoolGroup = new HAPGroup(this.generateId(), "School");
//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_GROUP (ID, NAME) VALUES ('"+soccerGroup.getId()+"', '"+soccerGroup.getName()+"');").execute();
//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_GROUP (ID, NAME) VALUES ('"+schoolGroup.getId()+"', '"+schoolGroup.getName()+"');").execute();

//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERGROUP (ID, USERID, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SoccerGroup"+"');").execute();
//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERGROUP (ID, USERID, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SchoolGroup"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERGROUP (ID, USERID, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SoccerForFun"+"');").execute();

//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"MyRealtor"+"', '"+"MyRealtor"+"');").execute();
//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"Airplane Arrival"+"', '"+"Airplane Arrival"+"');").execute();

//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SoccerApp1"+"', '"+"SoccerApp1"+"', '"+"SoccerGroup"+"');").execute();
//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SoccerApp2"+"', '"+"SoccerApp2"+"', '"+"SoccerGroup"+"');").execute();
//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SchoolApp1"+"', '"+"SchoolApp1"+"', '"+"SchoolGroup"+"');").execute();
//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"SchoolApp2"+"', '"+"SchoolApp2"+"', '"+"SchoolGroup"+"');").execute();

			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"App_SoccerForFun_PlayerInformation"+"', '"+"PlayerInformation"+"', '"+"SoccerForFun"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"App_SoccerForFun_PlayerUpdate"+"', '"+"PlayerUpdate"+"', '"+"SoccerForFun"+"');").execute();
			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME, GROUPID) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"App_SoccerForFun_PlayerLineup"+"', '"+"PlayerLineup"+"', '"+"SoccerForFun"+"');").execute();

//			this.getConnection().prepareStatement("INSERT INTO MINIAPP_USERAPP (ID, USERID, APPID, APPNAME) VALUES ('"+this.generateId()+"', '"+userId+"', '"+"AppMySchool"+"', '"+"MySchool"+"');").execute();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public HAPUser createUser() {
		HAPUser out = new HAPUser();
		out.setId(this.generateId());
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("INSERT INTO MINIAPP_USER (NAME, ID) VALUES ('"+out.getId()+"', '"+out.getId()+"');");
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPUser getUserById(String id) {
		HAPUser out = null;
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_user where id='"+id+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				out = new HAPUser();
				out.setId((String)resultSet.getObject(HAPUser.ID));
				out.setName((String)resultSet.getObject(HAPUser.NAME));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPGroup getGroupById(String id) {
		HAPGroup out = null;
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_group where id='"+id+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				out = new HAPGroup();
				out.setId((String)resultSet.getObject(HAPGroup.ID));
				out.setName((String)resultSet.getObject(HAPGroup.NAME));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public List<HAPGroup> getUserGroups(String userId){
		List<HAPGroup> out = new ArrayList<HAPGroup>();
		try {
			List<String> groupIds = new ArrayList<String>();
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_usergroup where userid='"+userId+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				groupIds.add(resultSet.getString("groupid"));
			}
			
			for(String groupId : groupIds) {
				HAPGroup group = this.getGroupById(groupId);
				out.add(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPMiniApp getMiniApp(String id) {
		HAPMiniApp out = null;
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_miniapp where id='"+id+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				out = new HAPMiniApp((String)resultSet.getObject(HAPGroup.ID), (String)resultSet.getObject(HAPMiniApp.NAME), (String)resultSet.getObject(HAPMiniApp.CATEGARY));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public void updateUserInfoWithMiniApp(HAPUserInfo userInfo) {
		PreparedStatement statement;
		try {
			statement = this.getConnection().prepareStatement("SELECT * FROM miniapp_userapp where userid='"+userInfo.getUser().getId()+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				String miniAppId = resultSet.getString("appid");
				HAPMiniApp miniApp = getMiniApp(miniAppId); 
				String groupId = resultSet.getString("groupid");
				userInfo.addMiniApp(miniApp, groupId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HAPAppDataInfoContainer getAppDataInfos(HAPOwnerInfo ownerInfo) {
		HAPAppDataInfoContainer out = new HAPAppDataInfoContainer();
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM MINIAPP_SETTING where userid='"+ownerInfo.getUserId()+"' AND componentid='"+ownerInfo.getComponentId()+"' AND componenttype='"+ownerInfo.getComponentType()+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				out.addData(buildAppDataInfoFromResult(resultSet));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public void getAppDataInfo(HAPAppDataInfo appDataInfo) {
		HAPOwnerInfo ownerInfo = appDataInfo.getOwnerInfo();
		String dataName = appDataInfo.getName();

		try {
			PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM MINIAPP_SETTING where userid='"+ownerInfo.getUserId()+"' AND componentid='"+ownerInfo.getComponentId()+"' AND componenttype='"+ownerInfo.getComponentType()+"' AND name='"+dataName+"';");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				buildAppDataInfoFromResult(resultSet, appDataInfo);
				break;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void getAppDataInfos(HAPAppDataInfoContainer appDataInfos) {
		for(HAPAppDataInfo appDataInfo : appDataInfos.getDatas()) {
			this.getAppDataInfo(appDataInfo);
		}
	}

	public void updateAppDataInfos(HAPAppDataInfoContainer appDataInfos) {
		for(HAPAppDataInfo appDataInfo : appDataInfos.getDatas()) {
			HAPOwnerInfo ownerInfo = appDataInfo.getOwnerInfo();
			try {
				if(appDataInfo.getId()==null) {
					//create
					String id = this.generateId();
					PreparedStatement statement = this.getConnection().prepareStatement("INSERT INTO MINIAPP_SETTING (ID,USERID,COMPONENTID,COMPONENTTYPE, NAME,DATA) VALUES ('"+
								id+"', '"+ownerInfo.getUserId()+"', '"+ownerInfo.getComponentId()+"', '"+ownerInfo.getComponentType()+"', '"+appDataInfo.getName()+"', '"+appDataInfo.getDataStr()+"');");
					statement.execute();
					appDataInfo.setId(id);
				}
				else {
					//update
					PreparedStatement statement = this.getConnection().prepareStatement("UPDATE MINIAPP_SETTING SET DATA='"+appDataInfo.getDataStr()+"'  WHERE ID='"+appDataInfo.getId()+"'");
					statement.execute();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private HAPAppDataInfo buildAppDataInfoFromResult(ResultSet resultSet) {
		try {
			HAPAppDataInfo data = new HAPAppDataInfo();
			this.buildAppDataInfoFromResult(resultSet, data);
			return data;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void buildAppDataInfoFromResult(ResultSet resultSet, HAPAppDataInfo data) {
		try {
			data.setId(resultSet.getString(HAPAppDataInfo.ID));
			data.setOwnerInfo(this.buildOwnerInfoFromResult(resultSet));
			data.setName(resultSet.getString(HAPAppDataInfo.NAME));
			data.setData(resultSet.getString(HAPAppDataInfo.DATA));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private HAPOwnerInfo buildOwnerInfoFromResult(ResultSet resultSet) throws SQLException {
		HAPOwnerInfo out = new HAPOwnerInfo();
		out.setUserId(resultSet.getString(HAPOwnerInfo.USERID));
		out.setComponentId(resultSet.getString(HAPOwnerInfo.COMPONENTID));
		out.setComponentType(resultSet.getString(HAPOwnerInfo.COMPONENTTYPE));
		return out;
	}
	
/*
	public HAPMiniAppSettingData createSettingData(String userId, HAPMiniAppSettingData miniAppSettingData) {
		HAPMiniAppSettingData out = miniAppSettingData;
		
		Map<String, HAPSettingData> datas = out.getDatas();
		for(String dataName : datas.keySet()) {
			HAPSettingData data = datas.get(dataName);
			try {
				String id = this.generateId();
				PreparedStatement statement = this.getConnection().prepareStatement("INSERT INTO MINIAPP_SETTING (ID,USERID,OWNERID,OWNERTYPE, DATANAME,DATA) VALUES ('"+
							id+"', '"+userId+"', '"+out.getOwnerId()+"', '"+out.getOwnerType()+"', '"+dataName+"', '"+data.getDataStr()+"');");
				statement.execute();
				data.setId(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return out;
	}
*/
	public void deleteAppData(String dataId) {
		try {
			PreparedStatement statement = this.getConnection().prepareStatement("DELETE FROM MINIAPP_SETTING WHERE ID='"+dataId+"';");
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String generateId() {  return i+++"";   }
	
	protected Connection getConnection(){		return this.m_dbSource.getConnection();	}

	public HAPDBSource getDBSource(){  return this.m_dbSource;  }

}
