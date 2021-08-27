
package crypto;

import static crypto.Helper.cleanString;
import java.util.Scanner;

public class Shell 
{
	public static final int HELP_INTEGER = 911;
	public static final String HELP_String = "911";
	public static final int EXIT_INTEGER = 666;
	public static final String EXIT_String = "666";

	public static void main(String[] args)
	{
		shell();
	}
	
	/**
	 * Generates the user interface
	 */
	public static void shell()
	{
		Scanner scanInt = new Scanner(System.in);
		Scanner scanStringFile = new Scanner(System.in);
		
		do
		{
			//Welcome message
			
			System.out.println("\n#############################################\n");
			System.out.println("Welcome in this encoding/decoding programm ;)");
			System.out.println("Tap 911 for help");
			System.out.println("Tap 666 to exit");
			System.out.println("\n#############################################\n");
			
			//Choose encrypting or decrypting
			
			int rep = encryptOrDecrypt(scanInt);

			if(1==rep)
			{
				encryptChoice(scanInt, scanStringFile);
			}
			else if(2==rep)
			{
				decryptChoice(scanInt, scanStringFile);
			}
			
		}while(true); //never breaks unless rep==666
	}
	
	/**
	 * Method to decide if we encrypt or decrypt
	 * @param scanInt the Scanner that will contain 1 for Encrypt or 2 for Decrypt
	 * @return 1 for encrypt or 2 for decrypt
	 */
	public static int encryptOrDecrypt(Scanner scanInt)
	{
		int rep=0;
		
		do
		{
			System.out.println("Would you like to Encrypt (1) or Decrypt (2) ?");
			rep = scanInt.nextInt();
			
			if(EXIT_INTEGER==rep)
			{
				exitMethod();
			}
			else if(HELP_INTEGER==rep)
			{
				help();
			}
		}while(rep!=1 && rep!=2);
		return rep;
	}
	
	/**
	 * Exits the programm if the user enters 666
	 */
	public static void exitMethod()
	{
		System.out.println("Goodbye");
		System.exit(0);
	}
	
	/**
	 * Method to encrypt a String file
	 * @param scanInt reads an int input of the user (type or keyLength)
	 * @param scanStringFile reads a String input of the user when entering a fileName
	 */
	public static void encryptChoice(Scanner scanInt, Scanner scanStringFile)
	{
		//We decide what kind of encrypting process we use
		
		int type = chooseType(scanInt);
		
		//We enter the fileName that contains the text to encrypt
		
		String fichierName = chooseFile(scanStringFile, "(containing the plain text)");
		String inputMessage = Helper.readStringFromFile(fichierName);
		String messageClean = cleanString(inputMessage);
	
		//We enter the encrypting key
		
		String key = randomKeyGenerator(type, scanInt, messageClean);

		String cipherText = null;
		
		//We encrypt the plain text
		
		if(5==type) //PCBC wasn't in the Encrypt.encrypt(String message, String key, int type) orginal method so we do it here
		{
			cipherText = casePCBC(key, messageClean);
		}
		else
		{
			cipherText = Encrypt.encrypt(messageClean, key, type);
		}
		
		//key file
		Helper.writeBytesToFile(Helper.stringToBytes(cipherText), "encrypted_"+fichierName);
		
		//cipher text file
		Helper.writeBytesToFile(Helper.stringToBytes(key), "key_"+fichierName);
		
		System.out.println("Et voilà !");
	}
	
	/**
	 * Method that generates a random key from the method coded in the class Encrypt
	 * @param type the encoding process
	 * @param scanInt the Scanner that will capture our input for the keyLength
	 * @param messageClean the message to encrypt
	 * @return our random key
	 */
	public static String randomKeyGenerator(int type, Scanner scanInt, String messageClean)
	{
		String key = null;
		String lengthMessage = "Enter the length that you wish for the key : ";
		int keyLength = 0;
		int lengthONE = 1;
		
		if(type==Encrypt.CAESAR)
		{
			key = Helper.bytesToString( Encrypt.generatePad(lengthONE) );
		}
		else if(type==Encrypt.VIGENERE)
		{
			keyLength = chooseKeyLength(scanInt, lengthMessage);
			key = Helper.bytesToString( Encrypt.generatePad(keyLength) );
		}
		else if(type==Encrypt.XOR)
		{
			key = Helper.bytesToString( Encrypt.generatePad(lengthONE) );
		}
		else if(type==Encrypt.ONETIME)
		{
			key = Helper.bytesToString( Encrypt.generatePad(messageClean.length()) );
		}
		else if(type==Encrypt.CBC)
		{
			keyLength = chooseKeyLength(scanInt, lengthMessage);
			key = Helper.bytesToString( Encrypt.generatePad(keyLength) );
		}
		else if(5==type)
		{
			keyLength = chooseKeyLength(scanInt, lengthMessage);
			key = Helper.bytesToString( Encrypt.generatePad(keyLength) );
		}
		
		return key;
	}
	
	/**
	 * Method to encrypt using PCBC
	 * @param key the key to encrypt
	 * @param messageClean the message to encrypt
	 * @return the encrypted original message
	 */
	public static String casePCBC(String key, String messageClean)
	{
		byte[] iv = Helper.stringToBytes(key);
		byte[] plainText = Helper.stringToBytes(messageClean);
		
		byte[] cipherTextByte = Encrypt.pcbc(plainText, iv);
		String cipherText = Helper.bytesToString(cipherTextByte);
		
		return cipherText;
	}
	
	/**
	 * Method to decrypt a String file
	 * @param scanInt reads an int input of the user (type)
	 * @param scanStringFile reads a String input of the user when entering a fileName
	 */
	public static void decryptChoice(Scanner scanInt, Scanner scanStringFile)
	{
		//We decide what kind of decrypting process we use
		
		int type = chooseType(scanInt);
		
		//We enter the fileName that contains the text to decrypt
		
		String fichierName = chooseFile(scanStringFile, "(containing the cipher text)");
		byte[] cipherTextByte = Helper.readBytesFromFile(fichierName);
		
		//We enter the decrypting key
		
		String keyfileName = chooseFile(scanStringFile, "(containing the key)");
		String key = Helper.bytesToString(Helper.readBytesFromFile(keyfileName));

		//We decrypt the file and then save the plain text in a new file
		
		String plainText = decrypt(cipherTextByte, key, type);
		Helper.writeStringToFile(plainText, "decrypted_"+fichierName);
		
		System.out.println("Et voilà !");
	}
	
	/**
	 * Method to choose the size of the key and to check wether 666 or 911 was enterred for the length
	 * @param scanInt the Scanner that captures the length
	 * @return the length of the key the user choosed
	 */
	public static int chooseKeyLength(Scanner scanInt, String message)
	{
		int keyLength = 0;
		
		do {
			System.out.println(message);
			keyLength = scanInt.nextInt();
			
			if(keyLength==EXIT_INTEGER)
			{
				exitMethod();
			}
			else if(keyLength==HELP_INTEGER)
			{
				help();
			}
					 
		}while(keyLength==HELP_INTEGER || keyLength<=0);
		
		return keyLength;
	}
	
	
	/**
	 * Method to check wether 666 or 911 was enterred for the fileName
	 * @param scanStringFile reads a String input of the user when entering a fileName
	 * @return the fileName that the user choosed
	 */
	public static String chooseFile(Scanner scanStringFile, String message)
	{
		String fichierName=null;
		do {
			
			System.out.println("Your file name please "+message+" - must be in the \"res\" folder of the Crypto-CS107-2020 project :  ");
			fichierName = scanStringFile.next();
			
			if(fichierName.equals(EXIT_String))
			{
				exitMethod();
			}
			else if(fichierName.equals(HELP_String))
			{
				help();
			}
			
		}while(fichierName.equals(HELP_String));
		
		return fichierName;
	}
	
	/**
	 * Method to check wether 666 or 911 was enterred for the type
	 * @param scanInt reads an int input of the user
	 * @return the type that the user choosed
	 */
	public static int chooseType(Scanner scanInt)
	{
		int type=0;
		do {
			
			System.out.println("Your choices : CAESAR = 0, VIGENERE = 1, XOR = 2, ONETIME = 3, CBC = 4, PCBC = 5 ");
			type = scanInt.nextInt();
			
			if(EXIT_INTEGER==type)
			{
				exitMethod();
			}
			else if(HELP_INTEGER==type)
			{
				help();
			}
			
		}while(type==HELP_INTEGER && type!=0 && type!=1 && type!=2 && type!=3 && type!=4 && type!=5);
		
		return type;
	}

	/**
	 * displays this text whenever you tap 911
	 */
	public static void help()
	{
		System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
		System.out.println("Here is all the help you need\n");
		
		System.out.println("1) Your first step is to decide wether you want to encrypt or decrypt a file");
		System.out.println("Tap 1 to Encrypt or 2 to Decrypt");
		System.out.println("PLEASE enter an integer otherwise the program will crash !!!\n");
		
		System.out.println("2) Your second step is to decide wich encrypting/decrypting method you want to use");
		System.out.println("Your choices : CAESAR = 0, VIGENERE = 1, XOR = 2, ONETIME = 3, CBC = 4, PCBC = 5");
		System.out.println("PLEASE enter an integer otherwise the program will crash !!!\n");
		
		System.out.println("3) Your third step is to indicate the String file to encrypt or the Byte file to Decrypt plus it's related Byte file with the key");
		System.out.println("Make sure that your file is in the \"res\" folder of the Crypto-CS107-2020 project");
		System.out.println("Please enter a valid file name otherwise the program will crash !!!\n");
		
		System.out.println("4) Your fourth step is to give the key length if it is requested by the program");
		System.out.println("The program automatically generates a random key for CAESER, XOR and ONETIME because their length is already none");
		System.out.println("PLEASE enter an integer otherwise the program will crash !!!\n");
		
		System.out.println("5) The corresponding encrypted, decrypted and key files will be placed in the \"res\" folder\n");
		
		System.out.println("#Be careful, we only provide a frequency decoding for vigenere");
		System.out.println("#The key is randomly generated\n");
		
		System.out.println("Tap 911 anytime you need help");
		System.out.println("Tap 666 anytime you wish to exit");
		System.out.println("Until next time ;)");
		
		System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
	}
	
	/**
	 * Decrypting equivalent of Encrypt.encrypt(String message, String key, int type)
	 * @param cipherTextByte text to decrypt
	 * @param key the decrypting key
	 * @param type the decrypting process
	 * @return the cipherTextByte decrypted => the plain text
	 */
	public static String decrypt(byte[] cipherTextByte, String key, int type)
	{
		byte[] keyByte = Helper.stringToBytes(key);
		String plainText = null;
		
		if(type==Encrypt.CAESAR)
		{
			plainText = Helper.bytesToString( Encrypt.caesar( cipherTextByte, (byte) (-keyByte[0]) ) );
		}
		else if(type==Encrypt.VIGENERE)
		{
			System.out.println("For Vigenere we only provide a frequency decoding if your key length isn't too big (max around 8) and that the plainText was long enough, sorry ...");
			plainText = Decrypt.breakCipher(Helper.bytesToString(cipherTextByte), type);
		}
		else if(type==Encrypt.XOR)
		{
			plainText = Helper.bytesToString(Encrypt.xor(cipherTextByte, keyByte[0]));
		}
		else if(type==Encrypt.ONETIME)
		{
			plainText = Helper.bytesToString(Encrypt.oneTimePad(cipherTextByte, keyByte));
		}
		else if(type==Encrypt.CBC)
		{
			plainText = Helper.bytesToString(Decrypt.decryptCBC(cipherTextByte, keyByte));
		}
		else if(5==type)
		{
			plainText = Helper.bytesToString(Decrypt.decryptPCBC(cipherTextByte, keyByte));
		}
		
		return plainText;
	}
	
}
