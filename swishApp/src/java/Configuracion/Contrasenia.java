/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Configuracion;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author hecto
 */
public class Contrasenia {
    
    public static  String hashPassword(String password) throws Exception{
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536,256);
        //Seleccionar algoritmo
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        
    }
    
    public static boolean verificarPassword(String password, String hashAlmacenado) throws Exception{
        
        String[] partes = hashAlmacenado.split(":");
        byte[] salt = Base64.getDecoder().decode(partes[0]);
        byte[] hashOriginal = Base64.getDecoder().decode(partes[1]);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hashNuevo = skf.generateSecret(spec).getEncoded();
        return MessageDigest.isEqual(hashOriginal, hashNuevo);
        
    } 
    
}
