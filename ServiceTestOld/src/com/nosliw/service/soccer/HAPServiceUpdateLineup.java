package com.nosliw.service.soccer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.service.HAPExecutableService;
import com.nosliw.core.application.service.HAPResultInteractive;
import com.nosliw.core.application.service.HAPUtilityService;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;

public class HAPServiceUpdateLineup implements HAPExecutableService{

	private String adminEmail = "forbestwn@hotmail.com";
	
	@Override
	synchronized public HAPResultInteractive execute(Map<String, HAPData> parms){
		String actionStr = null;
		String statusStr = null;
		String oweToStr = "";
		String oweFromStr = "";
		if(parms.get("player")!=null) {
			String player = (String)parms.get("player").getValue();
			
			HAPData actionData = parms.get("action");
			String action = actionData==null? null : (String)actionData.getValue();
			
			HAPActionResult actionResult = null;
			actionResult = HAPPlayerLineupManager.getInstance().updateLineUp(player, action);
			
			HAPPlayerStatus playerStatus = null;
			if(actionResult==null) {
				playerStatus = HAPPlayerLineupManager.getInstance().getLineup().getPlayerStatus(player);
			}
			else {
				playerStatus = actionResult.getPlayerStatus();
			}
			actionStr = playerStatus.getActions().get(0);
			statusStr = player + "目前在 :  " + playerStatus.getStatusDescription();

			if(action!=null && actionResult!=null) {
				List<String> affectedPlayers = new ArrayList<String>();
				affectedPlayers.add(player);
				if(actionResult.getAffectedPlayer()!=null)  affectedPlayers.addAll(actionResult.getAffectedPlayer());
				this.sendEmailToPlayers(affectedPlayers);
			}
			
			
			HAPPlayerResult oweFrom = playerStatus.getOweFrom();
			if(oweFrom!=null) {
				oweFromStr = "应从 " + oweFrom.getPlayerName() + " 收取" + "$20";
			}
			HAPPlayerResult oweTo = playerStatus.getOweTo();
			if(oweTo!=null) {
				oweToStr = "应付给 " + oweTo.getPlayerName() + "$20 email 地址为 " + oweTo.emailStr();
			}
		}
		else {
			actionStr = "";
			statusStr = "还没有提供你的名字，请首先在‘你的信息’里面提供你的名字！！";
		}
		
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put("action", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), actionStr));
		output.put("status", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), statusStr));
		output.put("oweTo", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), oweToStr));
		output.put("oweFrom", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), oweFromStr));
		
		return HAPUtilityService.generateSuccessResult(output);
	}
	
	private void sendEmailToPlayers(List<String> players) {
		for(String player : players) {
			HAPPlayerStatus playerStatus = HAPPlayerLineupManager.getInstance().getLineup().getPlayerStatus(player);

			List<String> emailTo = new ArrayList<String>();
			emailTo.add(this.adminEmail);

			Set<HAPPlayerInfo> playerInfos = HAPPlayerManager.getInstance().getPlayerInfo(player);
			for(HAPPlayerInfo playerInfo : playerInfos) {
				String email = playerInfo.getEmail();
				if(HAPUtilityBasic.isStringNotEmpty(email)) {
					emailTo.add(email);
				}
			}
			this.sendEmail(emailTo, player+ "在soccer for fun的状态更新", player+", 你在soccer for fun group的状态为"+playerStatus.getStatusDescription());
		}
	}
	
	 private void sendEmail(final List<String> tos, final String subject, final String text) {  
		 new Thread(new Runnable() {

			@Override
			public void run() {
				  String host="smtp.live.com";  
				  final String user="mylastkilometer@hotmail.com";//change accordingly  
				  final String password="tony0818";//change accordingly  
				    
				   //Get the session object  
				   Properties props = new Properties();  

				   props.setProperty("mail.transport.protocol", "smtp");
				    props.setProperty("mail.host", "smtp.live.com");
				    props.put("mail.smtp.starttls.enable", "true");
				    props.put("mail.smtp.auth", "true");
				     
				   Session session = Session.getDefaultInstance(props,  
				    new javax.mail.Authenticator() {  
				      @Override
					protected PasswordAuthentication getPasswordAuthentication() {  
				    return new PasswordAuthentication(user,password);  
				      }  
				    });  
				  
				   //Compose the message  
				    try {  
				     MimeMessage message = new MimeMessage(session); 
				     
				     message.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
				     MimeBodyPart messageBodyPart = new MimeBodyPart();
				    // set charset to UTF8
				     messageBodyPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
				     
				     message.setFrom(new InternetAddress(user));
				     
				     List<Address> addresses = new ArrayList<Address>();
				     for(String to : tos)    	 addresses.add(new InternetAddress(to));
				     message.addRecipients(Message.RecipientType.TO, addresses.toArray(new Address[0]));
				     
				     message.setSubject(subject, "UTF-8");
				     message.setText(text, "UTF-8");  
				       
				    //send the message  
				     Transport.send(message);  
				  
				     System.out.println("message sent successfully...");  
			     } catch (MessagingException e) {
			    	 e.printStackTrace();
			    }  
			}
		 }).start();
	}  	
}
