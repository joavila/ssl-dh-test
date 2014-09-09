package cl.borrego.test.ssl.dh;

import java.io.*;
import javax.net.ssl.*;

public class Client implements Runnable
{
	public int port;

	public Client() { port = App.DEFAULT_PORT; }

	public Client(int p) { port = p; }

	public static void main( String[] args )
	{
		new Client().run();
	}

	public void run() {
		try {
			System.err.println(hit("10.157.153.196"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public java.util.List<String> hit(String host) throws Exception {
		System.err.println("Starting client...");
		SSLSocketFactory sslFact = (SSLSocketFactory)SSLSocketFactory.getDefault();
		java.util.List <String> ret = new java.util.ArrayList<String> ();
		for (String cipherSuite: sslFact.getSupportedCipherSuites() ) {
			InputStream in = null;
			try {
				System.err.println(String.format("Trying to hit {%s,%d} using {%s}.", host, port, cipherSuite));
				SSLSocket s = (SSLSocket)sslFact.createSocket(host, port);
				s.setEnabledCipherSuites(new String [] {cipherSuite} );
				in = s.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int length = 0;

				while ((length = in.read(buffer)) != -1) {
					baos.write(buffer, 0, length);
				}
				
				ret.add ( new String(baos.toByteArray(), App.CHARSET) );
				System.err.println(String.format("Successfully hit {%s,%d} using {%s}.", host, port, cipherSuite));
			} catch (SSLHandshakeException e) {
				e.printStackTrace();
			} finally {
				if (in!=null) { in.close(); }
			}
			
		}
		return ret;
	}
}
