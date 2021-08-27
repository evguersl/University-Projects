package crypto;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import static crypto.Helper.bytesToString;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {
	
	
	//---------------------------MAIN---------------------------
	public static void main(String args[]) {
		
		//==========added tests==========
		
		/*
		 * Q3.2, comparing with the values in the question
		 * 
		 * Valeur attendue pour le cipherText: {-15, 32, -1, -23, -10, -4}
		 * Test Ok
		 * 
		 
		
		byte[] plainText = {105, 32, 119, 97, 110, 116};
		byte key = 50;
		byte[] cipherText = Encrypt.caesar(plainText, key);
		
		System.out.println();
		
		key = -120;
		cipherText = Encrypt.caesar(plainText, key);
		
		*
		*/
		
		
		/*
		 * Q3.3, comparing with the values in the question
		 *
		 * Valeur attendue pour le cipherText: {-101, 32, 109, -59, -96, 106}
		 * Test OK
		 *
		
		byte[] plainText = {105, 32, 119, 97, 110, 116};
		byte[] key = {50, -10, 100};
		byte[] cipherText = Encrypt.vigenere(plainText, key);
		
		*
		*/
		
		
		/*
		 * Q3.4, comparing with the values in the question
		 *
		 * Valeur attendue pour le cipherText: {91, 32, 69, 83, 92, 70}
		 * Test OK
		 *
		
		byte[] plainText = {105, 32, 119, 97, 110, 116};
		byte key = 50;
		byte[] cipherText = Encrypt.xor(plainText, key);
		
		*
		*/
		
		/*
		 * Q3.5, own test 
		 * 
		 * Valeur attendue pour le cipherText: {115, 92}
		 * Test OK
		 * Assertion OK
		 *
		
		
		byte[] plainText = {12, -128};
		byte[] key = {127, -36};
		byte[] cipherText = Encrypt.oneTimePad(plainText, key);
		
		*
		*/
		
		/*
		 * Q3.6, own test 
		 * 
		 * Valeur attendue pour le cipherText: {57, 11, 43}
		 * Test OK
		 *
		
		
		byte[] plainText = {12, -30, 18,3,4,2,1,2,1,2,1};
		byte[] key = {53, -23};
		byte[] cipherText = Encrypt.cbc(plainText, key);
		
		 */
		
		/*
		 * Q3.7, own test
		 * 
		 * 10 valeurs aleatoires attendues
		 * Test ok
		 *
		
		byte[] pad = Encrypt.generatePad(10);
		
		*
		*/
		
		/*
		 * Q3.8, own test
		 * Test OK
		 * 
		 *
		
		String plainText = "lily a des lilas";
		String key = ":r";
		int type = 2;
		
		String cipherText = Encrypt.encrypt(plainText, key, type);
		System.out.println("cipherText : "+cipherText);
		byte[] keyByte = Helper.stringToBytes(key);
		byte[] cipher = Helper.stringToBytes(cipherText);
		String newPlainText = Helper.bytesToString(Encrypt.xor(cipher, keyByte[0]));
		System.out.println("plainText : "+newPlainText);
		
		*/
		
		
		/*
		 * Q4.1.1, own test
		 * 
		 * Test OK on retrouve bien Evgueni dans la liste
		 *
		
		
		String testMessage = "Makelele";
		byte[] plainText = stringToBytes(testMessage);
		byte[] key = stringToBytes("3E");
		String cypherString;
		
		byte[][] testResult = Decrypt.caesarBruteForce(Encrypt.caesar(plainText, key[0]));
		
		int testResultLength = testResult[0].length;
				
		for(int line=0; line<Decrypt.ALPHABETSIZE; line++)
		{
			cypherString = Helper.bytesToString(testResult[line]);
			
			if(cypherString.equals(testMessage))
			{
				System.out.println();
				System.out.println("----BANANA----");
				System.out.println();
			}
			
			System.out.print(Helper.bytesToString(testResult[line]));
			System.out.println();
		}
		*/
		
		/*
		 * Q4.1.2, own test
		 * 
		 * Test OK
		 *
		
		String testMessage = "Zidane il va marquer";
		byte[] plainText = stringToBytes(testMessage);
		byte[] key = stringToBytes("§");
		String cypherString;
		
		byte[][] testResult = Decrypt.xorBruteForce(Encrypt.xor(plainText, key[0]));
		
		int testResultLength = testResult[0].length;
				
		for(int line=0; line<Decrypt.ALPHABETSIZE; line++)
		{
			cypherString = Helper.bytesToString(testResult[line]);
			
			if(cypherString.equals(testMessage))
			{
				System.out.println();
				System.out.println("----BANANA----");
				System.out.println();
			}
			
			System.out.print(Helper.bytesToString(testResult[line]));
			System.out.println();
		}
		
		*/
		
		/*
		 * Q.4.1.3
		 * 
		 * Test OK
		 *
		
		
		String testMessage = "Zidane il va marquer";
		byte[] plainText = stringToBytes(testMessage);
		byte[] key = stringToBytes("§");
		String cypherString;
		
		byte[][] bruteForceResult = Decrypt.xorBruteForce(Encrypt.xor(plainText, key[0]));
		
		cypherString = Decrypt.arrayToString(bruteForceResult);

		System.out.println(cypherString);
		
		
		Helper.writeStringToFile(cypherString, "banana.txt");
		
		*/
		
		/*
		 * Q.4.2
		 *
		 * Test OK
		 *
				
		String testMessage = "Zidane il va marquer";
		byte[] plainText = stringToBytes(testMessage);
		byte[] key = stringToBytes("é");
		
		byte[] result = Decrypt.decryptCBC(Encrypt.cbc(plainText,key), key);

		System.out.println(Helper.bytesToString(result));
		
		*/
		
		
		
		/*
		 * Q.5.1.1
		 *
		 * Test ok
		 *
		
		byte byte1 = (byte) 230;
		int int1 = 0;
		
		int1 = (int) (byte1);
		
		System.out.println(int1);
		
		
		
		byte[] byteTab = {97, 32, 98, 99, -12, 32, -127}; 
		
		float[] resultFrequencies = Decrypt.computeFrequencies(byteTab);
		
		for(int index=0; index<resultFrequencies.length; index++)
		{
			System.out.println("resultFrequencies["+index+"] : "+resultFrequencies[index]);
		}
		
		*/
		
		/*
		 * Q.5.1.2 & Q.5.1.3
		 * Test OK
		 *
		
		int[] newTab = Decrypt.indexArray(245);
		int newTabLength = newTab.length;
		
		for(int index = 0; index<newTabLength; index++)
		{
			System.out.println("newTab["+index+"] : "+newTab[index]);
		}
		
		
		
		String plainText = "I want to become the real star of the show! It has always been nice to work as an actor in this company, but never did they take care of me as they should have. So this is why I decided to leave and show them what it means to be a real professional! Thank you still for everything. I am thankful for all the difficulties we had to overcome together. But now comes the time of my life! This is real! I m gonna be the main actor this time in my next movie, and everyone will be forced to recognize that they should have believed in me more! And that I was nothing but a rough Gem waiting to be polished, ready to shine on the big screen.";	
		plainText = Helper.cleanString(plainText);
		byte[] plainTextByte = Helper.stringToBytes(plainText);
		byte key = 127;
		//byte rightKey = -97;
		byte[] cipherText = Encrypt.caesar(plainTextByte, key);
		
		byte resultKey = Decrypt.caesarWithFrequencies(cipherText);
		
		System.out.println("resultKey : "+resultKey);
		
		byte[] plainTextResult = Encrypt.caesar(cipherText, resultKey);
		
		System.out.println(Helper.bytesToString(plainTextResult));
		
		*/
		
		/*
		 * Q.5.2.1
		 * Test OK
		
		//noSpaces test
		System.out.println("------------------------");

		byte[] tab = {32, 32};
		
		List<Byte> noSpacesArray = new ArrayList<Byte>();
		
		noSpacesArray = Decrypt.removeSpaces(tab);
		System.out.println(noSpacesArray);
		System.out.println("------------------------");

		*
		*/
		
		/*
		 * Q.5.2.2.1
		 * Test OK
		 *
		
		//AAFCAWWA
		
		List<Byte> cipher = new ArrayList<Byte>();
		
		cipher.add((byte)65);
		cipher.add((byte)65);
		cipher.add((byte)70);
		cipher.add((byte)65);
		cipher.add((byte)65);
		
		int[] numberOfOccurences = Decrypt.numberOfOccurences(cipher);
		
		for(int i=0; i<numberOfOccurences.length; i++)
		{
			System.out.println(numberOfOccurences[i]);
		}
		
		*/
		
		/*
		 * Q.5.2.2.2 et Q.5.2.2.3
		 * 
		 * Test OK
		 *
		
		int[] occurencesArray = {0,0,1,0,0,1,0,0,3,4,5,6,0,0,1,0,0,1,0,0,1,0,0,6,7,8,9,8,7,6,5,4,3,2,1,2,3,4,5,6,7,8,9,8,7,6,5,4,3,2,1};
		
		List<Integer> locMax = Decrypt.localMaximum(occurencesArray);
		
		int locMaxTaille = locMax.size();
		
		for(int index=0; index<locMaxTaille; index++)
		{
			System.out.print(locMax.get(index)+" ");
		}
		
		System.out.println();
				
		Map<Integer, Integer> testMap =  Decrypt.keyLength(locMax);
		
		for (Entry<Integer, Integer> pair : testMap.entrySet()) {
			//itérer sur les paires clé-valeur 
			System.out.println(pair.getKey() + " " + pair.getValue());
			}
		
		*/
		
		/*
		 * Q.5.2.5
		 * Test OK
		 *
		
		System.out.println();
		System.out.println();
		
		
		String plainText = "I want to become the real star of the show! It has always been nice to work as an actor in this company, but never did they take care of me as they should have. So this is why I decided to leave and show them what it means to be a real professional! Thank you still for everything. I am thankful for all the difficulties we had to overcome together. But now comes the time of my life! This is real! I m gonna be the main actor this time in my next movie, and everyone will be forced to recognize that they should have believed in me more! And that I was nothing but a rough Gem waiting to be polished, ready to shine on the big screen.";		
		plainText = Helper.cleanString(plainText);
		byte[] plainTextByte = Helper.stringToBytes(plainText);
		byte[] key = {127,2,3};
		//byte rightKey = -97;
		byte[] cipherText = Encrypt.vigenere(plainTextByte, key);
		
		byte[] newPlainText = Decrypt.vigenereWithFrequencies(cipherText);
		
		System.out.println(Helper.bytesToString(newPlainText));
		
		*/
		
		
		
		
		/*
		 * Q.5.3
		 * Test OK
		 */
		
		/*
		String inputMessage = "Zidane il va marquer";
		String key = "a";
		
		byte[] inputMessageByte = Helper.stringToBytes(inputMessage);
		byte[] keyByte = Helper.stringToBytes(key);
		
		byte[] testCaesar = Encrypt.caesar(inputMessageByte, keyByte[0], false);
		
		String plainText1 = Decrypt.breakCipher(Helper.bytesToString(testCaesar), 0);
		System.out.println(plainText1);
		
		String inputMessage2 = Helper.readStringFromFile("challenge-encrypted.txt");
		
		String plainText2 = Decrypt.breakCipher(inputMessage2, 1);
		System.out.println(plainText2);
		*/
		
		/*
		
		String inputMessage = "Zidane il va marquer";
		String key = "a";
		
		byte[] inputMessageByte = Helper.stringToBytes(inputMessage);
		byte[] keyByte = Helper.stringToBytes(key);
		
		byte[] testXOR = Encrypt.xor(inputMessageByte, keyByte[0], false);
		
		String plainText3 = Decrypt.breakCipher(Helper.bytesToString(testXOR), 2);
		System.out.println(plainText3);

		 */
		
		/*
		
		System.out.println();
		System.out.println();
		System.out.println("-----------------");
		System.out.println("RESULT 1");
		System.out.println("-----------------");
		System.out.println();
		System.out.println();
		
		System.out.println(plainText1);
		
		*/
		
		/*
		
		System.out.println();
		System.out.println();
		System.out.println("-----------------");
		System.out.println("RESULT 2");
		System.out.println("-----------------");
		System.out.println();
		System.out.println();
		
		System.out.println(plainText2);
		
		*/
		
		/*
		
		System.out.println();
		System.out.println();
		System.out.println("-----------------");
		System.out.println("RESULT 3");
		System.out.println("-----------------");
		System.out.println();
		System.out.println();
		
		System.out.println(plainText3);
		
		*/
		
		
		
		/*
		 * BONUS 1 PCBC
		 * OK
		 * 
		 *
		
		
		
		
		
		String plainText = "Zidane il va marquer";	
		plainText = Helper.cleanString(plainText);
		byte[] plainTextByte = Helper.stringToBytes(plainText);
		byte[] key = {127,2,3};
		//byte rightKey = -97;
		byte[] cipherText = Encrypt.pcbc(plainTextByte, key);
		
		byte[] newPlainText = Decrypt.decryptPCBC(cipherText, key);
		
		System.out.println(Helper.bytesToString(newPlainText));
		
		*/
		
		
		/*
		 * BONUS 2 INTERFACE
		 *
		 *OK
		*/

		//===============================




		/*
		 * 
		 *

		String inputMessage = Helper.readStringFromFile("text_three.txt");
		String key = "s2T%";

		String messageClean = cleanString(inputMessage);


		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);


		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();

		System.out.println("------Caesar------");
		testCaesar(messageBytes, keyBytes[0]);

		// TODO: TO BE COMPLETED

		System.out.println();
		System.out.println("******************");
		System.out.println();

		System.out.println("------CBC---------");
		testCBC(messageBytes , keyBytes);

		System.out.println();
		System.out.println("******************");
		System.out.println();

		System.out.println("------PCBC--------");
		testPCBC(messageBytes , keyBytes);

		System.out.println();
		System.out.println("******************");
		System.out.println();

		System.out.println("------VIGENERE-----");
		testVigenere(messageBytes , keyBytes);

		System.out.println();
		System.out.println("******************");
		System.out.println();

		System.out.println("------XOR-----");		
		testXOR(messageBytes, keyBytes[0]);

		System.out.println();
		System.out.println("******************");
		System.out.println();

		System.out.println("------ONE_TIME_PAD-----");
		testOneTimePad(messageBytes);

		*/
		
	}
	

	
	//Run the Encoding and Decoding using the caesar pattern 
	public static void testCaesar(byte[] string , byte key) {
		//Encoding
		byte[] result = Encrypt.caesar(string, key);
		String s = bytesToString(result);
		//System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Encrypt.caesar(result, (byte) (-key)));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without key
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");
		System.out.println("caesarBruteForce OK");
		
		byte decodingKey = Decrypt.caesarWithFrequencies(result);
		String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
		System.out.println("Decoded without knowing the key : " + sFD);
	}
	
	
	public static void testCBC(byte[] string , byte[] key) {
		//Encoding
		byte[] result = Encrypt.cbc(string, key);
		String s = bytesToString(result);
		//System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Decrypt.decryptCBC(result, key));
		System.out.println("Decoded knowing the key : " + sD);
		
	}
	
	public static void testPCBC(byte[] string , byte[] key) {
		//Encoding
		byte[] result = Encrypt.pcbc(string, key);
		String s = bytesToString(result);
		//System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Decrypt.decryptPCBC(result, key));
		System.out.println("Decoded knowing the key : " + sD);
		
	}
	
	public static void testVigenere(byte[] string , byte[] key) {
		//Encoding
		byte[] result = Encrypt.vigenere(string, key);
		String s = bytesToString(result);
		//System.out.println("Encoded : " + s);
		
		//Decoding without key
		byte[] sFdd = Decrypt.vigenereWithFrequencies(result);
		String sFD = bytesToString(sFdd);
		System.out.println("Decoded without knowing the key : " + sFD);
	}
	
	public static void testXOR(byte[] string , byte key) {
		//Encoding
		byte[] result = Encrypt.xor(string, key);
		String s = bytesToString(result);
		//System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Encrypt.xor(result, key));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without key
		byte[][] bruteForceResult = Decrypt.xorBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceXOR.txt");
		System.out.println("xorBruteForce OK");
		
	}
	
	public static void testOneTimePad(byte[] string) {
		//Encoding
		byte[] key = Encrypt.generatePad(string.length);
		byte[] result = Encrypt.oneTimePad(string, key);
		String s = bytesToString(result);
		//System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Encrypt.oneTimePad(result, key));
		System.out.println("Decoded knowing the key : " + sD);
		
	}
	
	
//TODO : TO BE COMPLETED
	
}


