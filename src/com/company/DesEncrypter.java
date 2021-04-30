package com.company;
import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

class DesEncrypter {
        Cipher ecipher;
        Cipher dcipher;
        //Конструктор
        public DesEncrypter(SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        }
         //Функция шифровнаия
        public String encrypt(String str) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            return new String(java.util.Base64.getMimeEncoder().encode(enc), StandardCharsets.UTF_8);
        }
         //Функция расшифрования
        public String decrypt(String str) throws IOException, IllegalBlockSizeException, BadPaddingException {
            byte[] dec = Base64.getMimeDecoder().decode(str);
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        }
        // читаем файл с помощью Scanner
        private static String readUsingScanner(String fileName) throws IOException {
            Scanner scanner = new Scanner(Paths.get(fileName), StandardCharsets.UTF_8.name());
            String data = scanner.useDelimiter("\\A").next();
            scanner.close();
            return data;
        }
         //Функция для проверки правильности работы класса
        public static void main(String[] s) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException {
            KeyGenerator keygen = KeyGenerator.getInstance("DES");
            SecretKey sKey = keygen.generateKey();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("key.txt")));
            oos.writeObject(sKey);
            oos.close();

            DesEncrypter encrypter = new DesEncrypter(sKey);
            String fileName = "cyphered_text.txt";
            String OStr1 = readUsingScanner(fileName);
            String SStr = encrypter.encrypt(OStr1);
            String OStr2 = encrypter.decrypt(SStr);
            System.out.println("Open String:"+OStr1+
                    "\nAfter encripting: "+SStr+"\nAfter decripting: "+OStr2);

            String print = "Open String:"+OStr1+"\n\n\nAfter encripting: "+SStr+"\n\n\nAfter decripting: "+OStr2;
            try(FileOutputStream fos=new FileOutputStream("otchet.txt"))
            {
                byte[] buffer = print.getBytes();
                fos.write(buffer, 0, buffer.length);
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
            System.out.println("The file has been written");
        }
    }
