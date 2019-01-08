package utils;

import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.testng.annotations.Test;



public class emailReport {
	
	
	ConfigReader config = new ConfigReader();
	String html;
	
	@Test
	public void sendEmail(String testName, String sheetName){

    final String username =  "Chandrasekhar.Kulandasamy@experient-inc.com";
    

    Properties props = new Properties();
    props.put("mail.smtp.auth", false);
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.host", "smtp2.expoexchange.com");
    props.put("mail.smtp.port", "25");

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, config.getshowManagerPsw());
                }
            });

    try {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("Chandrasekar.kulandasamy@infinite.com,sreejak@infinite.com"));
        
        message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse("Chandrasekar.kulandasamy@infinite.com"));
        
        message.setSubject("Automation Report for New Production View - " + testName );
        
               
        BodyPart messageBodyPart = new MimeBodyPart();
        BodyPart attachmentPart = new MimeBodyPart();

        Multipart multipart = new MimeMultipart();

        messageBodyPart = new MimeBodyPart();
        String file = config.getexcelpath();
        String fileName = "Automation Report for New Production View.xlsx";
        DataSource source = new FileDataSource(file);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(fileName);
        
        //Get the count of Failures
        XlsUtil xls = new XlsUtil(config.getexcelpath());
        
        int count = 0;
        for (int i=1; i<=xls.getRowCount(sheetName); i++){
        	String result = xls.getCellData(sheetName, 6, i);
        	
        	if (result.contains("Fail")){
        		count++;
        	}
        }
        if(count==0) {
        	html = "<p>Hi,</p><p>PFA the Automation Test report.</p><p>Note: All are Passed :-) </p><p>Thanks,</p><p>Chandra</p>";
        }else if (count==1) {
        	html = "<p>Hi,</p><p>PFA the Automation Test report.</p><p>Note: There is one failure.. </p><p>Thanks,</p><p>Chandra</p>";
        } else {
        	html = "<p>Hi,</p><p>PFA the Automation Test report.</p><p>Note: There are "+count+" failures.. </p><p>Thanks,</p><p>Chandra</p>";
        }
        
        messageBodyPart.setContent(html,"text/html");
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);

       Transport.send(message);
       System.out.println("Email Sent");

        System.out.println("Done");

    } catch (MessagingException e) {
        e.printStackTrace();
    }
  }
}