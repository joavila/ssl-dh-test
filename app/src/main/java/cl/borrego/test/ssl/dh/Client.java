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
	
	public String hit(String host) throws Exception {
		InputStream in = null;
		try {
			System.err.println("Starting client...");
			SSLSocketFactory sslFact =
				(SSLSocketFactory)SSLSocketFactory.getDefault();
			System.err.println(String.format("Trying to hit {%s,%d}", host, port));
			SSLSocket s = (SSLSocket)sslFact.createSocket(host, port);
			s.setEnabledCipherSuites(
					getDHCiphers(
						sslFact.getSupportedCipherSuites(), "### %s"));
			in = s.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;

			while ((length = in.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}
			return new String(baos.toByteArray(), App.CHARSET);
		} finally {
			if (in!=null) { in.close(); }
		}
	}
	
	protected static String[] getDHCiphers(String[] availableCipherSuites, final String format) {
		java.util.List<String> ret = new java.util.ArrayList<String>();
		for( String cipherSuite: availableCipherSuites ) {
			if (cipherSuite.contains("DHE") ) {
				System.err.println(String.format(format, cipherSuite));
				ret.add(cipherSuite);
			}
		}
		return ret.toArray(new String[0]);
	}
}
