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
		new Thread(new App(port)).start();
		java.util.List<String> results = new Client(port).hit("localhost");
		for ( String result: results ) {
			assertEquals( App.MESSAGE, result );
		}
	}


}
