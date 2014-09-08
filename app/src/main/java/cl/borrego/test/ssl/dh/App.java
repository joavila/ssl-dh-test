package cl.borrego.test.ssl.dh;

import java.io.*;
import javax.net.ssl.*;

public class App implements Runnable
{
	public int port;
	public static int DEFAULT_PORT = 9090;
	public static final String MESSAGE = "Hello World!";
	public static final String CHARSET = "UTF-8";

	public App() { port = DEFAULT_PORT; }

	public App(int p) { port = p; }

	public static void main( String[] args )
	{
		new App().run();
	}

	public void run() {
		SSLServerSocket s;
		try {
			SSLServerSocketFactory sslSrvFact =
				(SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			s = (SSLServerSocket)sslSrvFact.createServerSocket(port);
			final String[] cipherSuites = Client.getDHCiphers(
				sslSrvFact.getSupportedCipherSuites(), "@@@ %s");
			/*for(String cs: cipherSuites) {
				System.err.println("*** " + cs);
			}//*/
			s.setEnabledCipherSuites(
					cipherSuites
					);

			System.err.println(String.format("Server listening in port {%d}...", port));
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
