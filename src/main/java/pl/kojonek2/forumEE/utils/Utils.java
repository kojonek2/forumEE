package pl.kojonek2.forumEE.utils;

import java.security.NoSuchAlgorithmException;

import org.apache.catalina.realm.MessageDigestCredentialHandler;

public class Utils {

	public static String digestPassword(String password) {
		MessageDigestCredentialHandler digester = new MessageDigestCredentialHandler();
		digester.setEncoding("UTF-8");
		
		try {
			digester.setAlgorithm("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();//this should never happen
			return null;
		}
		
		digester.setIterations(50);
		digester.setSaltLength(20);
		return digester.mutate(password);
	}
}
