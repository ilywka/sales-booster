package com.sashnikov.salesbooster;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SalesBoosterApplication {

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
		setupSSL();
		SpringApplication.run(SalesBoosterApplication.class, args);
	}

	private static void setupSSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[]{new X509ExtendedTrustManager() {

			@Override
			public void checkClientTrusted(
					X509Certificate[] xcs,
					String string,
					Socket socket
			) throws CertificateException {
				// skip SSL checks
			}

			@Override
			public void checkServerTrusted(
					X509Certificate[] xcs,
					String string,
					Socket socket
			) throws CertificateException {
				// skip SSL checks
			}

			@Override
			public void checkClientTrusted(
					X509Certificate[] xcs,
					String string,
					SSLEngine ssle
			) throws CertificateException {
				// skip SSL checks
			}

			@Override
			public void checkServerTrusted(
					X509Certificate[] xcs,
					String string,
					SSLEngine ssle
			) throws CertificateException {
				// skip SSL checks
			}

			public void checkClientTrusted(
					X509Certificate[] x509Certificates,
					String string
			) throws CertificateException {
				// skip SSL checks
			}

			public void checkServerTrusted(
					X509Certificate[] x509Certificates,
					String string
			) throws CertificateException {
				// skip SSL checks
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		}}, new SecureRandom());
		SSLContext.setDefault(ctx);
	}

}
