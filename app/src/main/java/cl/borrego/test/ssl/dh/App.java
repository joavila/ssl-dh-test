package cl.borrego.test.ssl.dh;

import java.io.*;
import javax.net.ssl.*;

public class App implements Runnable
{
	public int port = 9090;
	public static final String MESSAGE = "Hello World!";
	public static final String CHARSET = "UTF-8";

	public App() {}

	public App(int p) { port = p; }

	public static void main( String[] args )
	{
		System.out.println( "Hello World!" );
	}

	public void run() {
		SSLServerSocket s;
		try {
			SSLServerSocketFactory sslSrvFact =
				(SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			s = (SSLServerSocket)sslSrvFact.createServerSocket(port);
			s.setEnabledCipherSuites(sslSrvFact.getSupportedCipherSuites());
			
			SSLSocket c = (SSLSocket)s.accept();
			OutputStream out = null;
			try {
		       		out = c.getOutputStream();
				out.write(MESSAGE.getBytes(CHARSET));
			} finally {
				if (out != null) { out.close(); }
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		System.err.println("Server done.");
	}
}
