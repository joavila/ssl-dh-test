package cl.borrego.test.ssl.dh;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import javax.net.ssl.*;

public class AppTest 
	extends TestCase
{
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( AppTest.class );
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() throws Exception
	{
		final int port = 9095;
		InputStream in = null;
		try {
			new Thread(new App(port)).start();
			System.out.println("Starting client...");
			SSLSocketFactory sslFact =
				(SSLSocketFactory)SSLSocketFactory.getDefault();

			SSLSocket s = (SSLSocket)sslFact.createSocket("localhost", port);
			s.setEnabledCipherSuites(
					getDHCiphers(
						sslFact.getSupportedCipherSuites()));
			in = s.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;

			while ((length = in.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}
			assertEquals( 
					App.MESSAGE, 
					new String(baos.toByteArray(), App.CHARSET)
				    );
		} finally {
			if (in!=null) { in.close(); }
		}
	}

	private String[] getDHCiphers(String[] availableCipherSuites) {
		java.util.List<String> ret = new java.util.ArrayList<String>();
		for( String cipherSuite: availableCipherSuites ) {
			if (cipherSuite.contains("") ) {
				ret.add(cipherSuite);
			}
		}
		return ret.toArray(new String[0]);
	}
}
