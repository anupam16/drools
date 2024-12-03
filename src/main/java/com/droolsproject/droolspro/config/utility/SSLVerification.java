package com.droolsproject.droolspro.config.utility;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import jakarta.net.ssl.HostnameVerifier;
import jakarta.net.ssl.HttpsURLConnection;
import jakarta.net.ssl.SSLContext;
import jakarta.net.ssl.SSLSession;
import jakarta.net.ssl.TrustManager;
import jakarta.net.ssl.X509TrustManager;

public class SSLVerification {

	 public static void disableSslVerification() {
	    	try {
	    		// Create a trust manager that does not validate certificate chains
	    		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	    			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	    				return null;
	    			}
	    			public void checkClientTrusted(X509Certificate[] certs, String authType) { }
		            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
		          }
		        };
		        // Install the all-trusting trust manager
		        SSLContext sc = SSLContext.getInstance("SSL");
		        sc.init(null, trustAllCerts, new java.security.SecureRandom());
		        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		        // Create all-trusting host name verifier
		        HostnameVerifier allHostsValid = new HostnameVerifier() {
		        	public boolean verify(String hostname, SSLSession session) {
		        		return true;
		            }
		        };
		        // Install the all-trusting host verifier
		        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		    } catch (NoSuchAlgorithmException e) {
		    	System.out.println("NoSuchAlgorithmException : " + e);
		    } catch (KeyManagementException e) {
		    	System.out.println("KeyManagementException : " + e);
		    }
		}

}
