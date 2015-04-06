/**
 * CS 460: Secure Communication
 * Professor: Ting Ting Chen
 *
 * Final Project
 *
 * <LoginWindow>
 *
 * Madhav Chhura
 */
package edu.csupomona.cs460.final_project;

import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author madhavchhura
 */
public class Sha {
    
    private String salt = null;
    
    //Contructor for the Sha clas.
    Sha(){
        try {
            salt = getSalt();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Sha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    //Sha Algorithm for Encryption
    public String get_SHA_1_SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            //md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Use this method if you want to add Salt and modify the SHA encryption.
    private String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
}
