package org.apache.commons.mail;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailTest {

	private static final String[] Test_Emails = { "ab@c.com", "a.b@c.org", "abcdefghijklmnopqrst@abcdefghijklmnopqrst.com.bd" };
	private static final String[] Empty_Emails = null;
	
	// Concrete Email Class for Testing
	private EmailConcrete email;
	
	@Before		// set up method
	public void setUpEmailTest() throws Exception {
		
		email = new EmailConcrete();
		
		//mockSmtpServer = new MockSmtpServer(EmailConfiguration.MOCK_SMTP_PORT);
	}
	
	@After
	public void tearDownEmailTest() throws Exception {
		
		// don't need to write anything, just blank
		// can write something if want to
		
	}
	// addBcc Test Cases
	@Test
	public void testAddBcc() throws Exception {

		email.addBcc(Test_Emails);
		
		assertEquals(3,  email.getBccAddresses().size());

		// assertEquals -- used to make sure expected & current behavior is equal
		
	}
	
	@Test(expected = EmailException.class)
	public void testAddBccEmpty() throws Exception {
		
		email.addBcc(Empty_Emails);
		
		assertEquals(0, email.getBccAddresses().size());
	}

	
	// addCC Test Cases
	@Test
	public void testAddCc() throws Exception {
		// email.addCc(Test_Emails);
		// assertEquals(3, this.email.getCcAddresses().size());
		
		List<InternetAddress> addCcTesting = new ArrayList<InternetAddress>();
		addCcTesting.add(new InternetAddress("abc@def.com"));
		addCcTesting.add(new InternetAddress("ghi@jkl.com"));
		addCcTesting.add(new InternetAddress("mno@pqr.com"));
		
		for(int i = 0; i < Test_Emails.length; i++)
		{
			this.email.addCc(Test_Emails[i]);
		}

		assertEquals(addCcTesting.size(), this.email.getCcAddresses().size());
	} 
	
	// addHeader(String name, String value) test cases
	@Test
	public void testAddHeader() throws Exception {
		
		
		Map<String, String> headerTesting = new Hashtable<String, String>();
		headerTesting.put("Test-One", "One");
		headerTesting.put("Test-Two", "Two");
		//headerTesting.put("Test-Three", "Sendmail");
		
		for (Iterator<Map.Entry<String, String>> items = headerTesting.entrySet().iterator(); items.hasNext();)
		{
			Map.Entry<String, String> headerEntry = items.next();
			String hName = headerEntry.getKey();
			String hValue = headerEntry.getValue();
			this.email.addHeader(hName, hValue);
		}
		
		assertEquals(headerTesting.size(), this.email.getHeaders().size());
        assertEquals(headerTesting, this.email.getHeaders());
		
		
        
	}
	
	@Test
	public void testAddHeaderException() {
		Map<String, String> hTestEmpty = new Hashtable<String, String>();
		hTestEmpty.put("Test-One", "");
		hTestEmpty.put("", "Two");
		
		Map<?, ?> hArray = new Hashtable<Object, Object>();
		for (Iterator<Map.Entry<String, String>> items = hTestEmpty.entrySet().iterator(); items.hasNext();)
		{
			Map.Entry<String, String> element = items.next();
			try
			{
				String hName = element.getKey();
				String hValue = element.getValue();
				
				this.email.addHeader(hName, hValue);
				fail("Exception");
			}
			catch (IllegalArgumentException e)
			{
				assertTrue(true); 
			} 
		}
		
		//this.email.setHeaders(headerTesting);
		
		assertEquals(hArray.size(), this.email.getHeaders().size());
        assertEquals(hArray.toString(), this.email.getHeaders().toString());
	}
	
	
	// addReplyTo Test Cases
	@Test
	public void testAddReplyTo() throws Exception {
		// email.addCc(Test_Emails);
		// assertEquals(3, this.email.getCcAddresses().size());
		
		List<InternetAddress> addReplyTest = new ArrayList<InternetAddress>();
		addReplyTest.add(new InternetAddress("abc@def.com", "Name 1"));
		addReplyTest.add(new InternetAddress("ghi@jkl.com", "Name 2"));
		addReplyTest.add(new InternetAddress("mno@pqr.com", "Name 3")); 
		
		for(int i = 0; i < Test_Emails.length; i++)
		{
			this.email.addReplyTo(Test_Emails[i]);
		}

		assertEquals(addReplyTest.size(), this.email.getReplyToAddresses().size());
	}
	
	// buildMimeMessage() Test Cases
	@Test
	public void testBuildMimeMessage() throws Exception
	{
		email.setHostName("localHost");
        email.setSSLOnConnect(true);
		email.setSmtpPort(1234);
		email.setFrom("a@b.com");
		email.addTo("c@d.com");
		email.setSubject("Test Mail");
		
		email.setCharset("ISO-8859-1");
		email.setContent("Test Content", "Text/Plain");
		email.addCc("test@abc.com");
		email.addBcc("test@abc.com");
		email.addHeader("Test", "abc");
		email.addReplyTo("test@test.com", "Name");
		email.setSentDate(this.email.getSentDate());
		email.setAuthentication(null, null);
		

		MimeMessage bmsg = this.email.getMimeMessage();
		
		email.buildMimeMessage();
		
		if (email.isSSL())
		{
			email.send();
		}
		
	}
	
	// getHostName Test Cases
	@Test
	public void testGetHostName() {
		email.setHostName("localHost");
		
		String hostname = email.getHostName();
		
		assertEquals("localHost", hostname);
	}
	
	@Test
	public void testGetSetHostNameWithNull() { 
		email.setHostName(null);
		assertEquals(null, email.getHostName());
	}
	
	@Test
	public void testGetSetHostNameWithSession() { 
   
		
		Properties props = new Properties();
		// fill all the information like host name, etc.
		Session session = Session.getDefaultInstance(props, null);
		props.put(EmailConstants.MAIL_HOST, "smtp.gmail.com");
		email.setMailSession(session);
		//email.setHostName("localHost");
		assertEquals("smtp.gmail.com", email.getHostName());
		
	}
	
	// getSentDate() Test Cases
	@Test
	public void testGetSentDate()
	{
		Date testDate = Calendar.getInstance().getTime();
        this.email.setSentDate(testDate);
        assertEquals(testDate, this.email.getSentDate());

	}
	
	// socket connection test cases
	@Test
	public void testSocket()
	{
		email.setSocketConnectionTimeout(100);
		assertEquals(100, email.getSocketConnectionTimeout());
		
	}
	
	// setFrom test cases
	@Test
	public void testSetFrom() throws Exception {

		List<InternetAddress> testFrom = new ArrayList<InternetAddress>();
		testFrom.add(new InternetAddress("abc@def.com"));
		testFrom.add(new InternetAddress("ghi@jkl.com"));
		testFrom.add(new InternetAddress("mno@pqr.com"));
		
		for(int i = 0; i < Test_Emails.length; i++)
		{
			this.email.setFrom(Test_Emails[i]);
			
			assertEquals(testFrom.get(i), this.email.getFromAddress());
		}

		
	}


	
	
}
