package crypto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Decrypt {
	
	
	public static final int ALPHABETSIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1 ; //256
	public static final int APOSITION = 97 + ALPHABETSIZE/2; 
	
	//source : https://en.wikipedia.org/wiki/Letter_frequency
	public static final double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};
	
	/**
	 * Method to break a string encoded with different types of cryptosystems
	 * @param type the integer representing the method to break : 0 = Caesar, 1 = Vigenere, 2 = XOR
	 * @return the decoded string or the original encoded message if type is not in the list above.
	 */
	public static String breakCipher(String cipher, int type) {
		
		byte[] cipherByte = Helper.stringToBytes(cipher);
		String plainTextSring="";
		byte[] plainTextByte; 
		
		if(Encrypt.CAESAR == type)
		{
			byte key = caesarWithFrequencies(cipherByte);
			plainTextByte = Encrypt.caesar(cipherByte, key);
			plainTextSring = Helper.bytesToString(plainTextByte);
		}
		else if(Encrypt.VIGENERE == type)
		{
			plainTextByte = vigenereWithFrequencies(cipherByte);
			plainTextSring = Helper.bytesToString(plainTextByte);
		}
		else if(Encrypt.XOR == type)
		{
			byte[][] plainTextPossibilities = xorBruteForce(cipherByte);
			plainTextSring = Decrypt.arrayToString(plainTextPossibilities);
		}
		
		return plainTextSring;
	}
	
	
	/**
	 * Converts a 2D byte array to a String
	 * @param bruteForceResult a 2D byte array containing the result of a brute force method
	 */
	public static String arrayToString(byte[][] bruteForceResult) {
		
		String bruteForceResultString="";
				
		for(int line=0; line<Decrypt.ALPHABETSIZE; line++)
		{			
			bruteForceResultString += Helper.bytesToString(bruteForceResult[line]);
			bruteForceResultString += System.lineSeparator();
		}
		
		return bruteForceResultString; 
	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 *  Method to decode a byte array  encoded using the Caesar scheme
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return a 2D byte array containing all the possibilities
	 */
	public static byte[][] caesarBruteForce(byte[] cipher) {
		
		int lenghtCipher = cipher.length;
		byte minByteValue = -128;
		byte maxByteValue = 127;
		
		byte[][] potentialCipher = new byte[ALPHABETSIZE][lenghtCipher];
		
		for(int potentialKey = minByteValue; potentialKey<(maxByteValue+1); potentialKey++)
		{
			potentialCipher[potentialKey+maxByteValue+1] = Encrypt.caesar(cipher, (byte)potentialKey);
		}

		return potentialCipher;
	}	
	
	
	/**
	 * Method that finds the key to decode a Caesar encoding by comparing frequencies
	 * @param cipherText the byte array representing the encoded text
	 * @return the encoding key
	 */
	public static byte caesarWithFrequencies(byte[] cipherText) {

		float[] charFrequencies = computeFrequencies(cipherText);
		byte key = caesarFindKey(charFrequencies);
		
		return key;
	}
	
	/**
	 * Method that computes the frequencies of letters inside a byte array corresponding to a String
	 * @param cipherText the byte array 
	 * @return the character frequencies as an array of float
	 */
	public static float[] computeFrequencies(byte[] cipherText) 
	{
		int lengthCipherText = cipherText.length;
		float[] frequencyArray = new float[ALPHABETSIZE];
		int newIndex = 0;
		int byteValue = 0;
		int nonSpaceValues = lengthCipherText;
		
		//fill frequencyArray with the number of occurences
		//frequencyArray[129] = byte -127 ...
		
		for(int indexByte = 0; indexByte<lengthCipherText; indexByte++)
		{
			byteValue = cipherText[indexByte];
			
			if(Encrypt.SPACE==byteValue)
			{
				nonSpaceValues--;
				continue;
			}
			else if(cipherText[indexByte]<0)
			{
				newIndex = ALPHABETSIZE+byteValue;
			}
			else
			{
				newIndex = byteValue;
			}
			
			frequencyArray[newIndex]++;
		}
		
		//puts in frequency the elements in frequencyArray
		
		for(int indexFrequency = 0; indexFrequency<ALPHABETSIZE; indexFrequency++)
		{
			frequencyArray[indexFrequency]/=nonSpaceValues;
		}
		
		return frequencyArray;
	}
	
	
	/**
	 * Method that finds the key used by a Caesar encoding from an array of the cipherText character's frequencies
	 * @param charFrequencies the array of the cipherText character's frequencies
	 * @return the key
	 */
	public static byte caesarFindKey(float[] charFrequencies) {
		
		//championArray[0] = index , championArray[1] = produit scalaire
		double[] championArray = new double[2];
		
		for(int index = 0; index<ALPHABETSIZE; index++)
		{
			double[] intermediateArray = new double[2];
			
			intermediateArray[0] = index;
			intermediateArray[1] = produitScalaire(charFrequencies, index);
			
			championArray = maxValue(championArray,intermediateArray);
		}
		
		return (byte) (97-championArray[0]);
	}
	
	/**
	 * Method that creates an array of all the indexes of the charFrequencies array so no letter is forgotten when we reach the end
	 * @param index goes from 0 to ALPHABETSIZE-1
	 * @return the array with the new indexes
	 */
	public static int[] indexArray(int index)
	{
		int lengthIndexArray = ENGLISHFREQUENCIES.length;
		int[] indexArray = new int[lengthIndexArray]; 
		int newIndexValue = 0;
		
		for(int indexValue=index; indexValue<index+lengthIndexArray; indexValue++)
		{
			if(indexValue>ALPHABETSIZE-1)
			{
				newIndexValue = indexValue-ALPHABETSIZE;
			}
			else
			{
				newIndexValue = indexValue;
			}
			
			indexArray[indexValue-index] = newIndexValue;
		}
		
		return indexArray;
	}
	
	/**
	 * Method that finds the maximum value between the second element of an array1 and an array2
	 * @param tab1 the array1
	 * @param tab2 the array2
	 * @return the maximum value of the two elements
	 */
	public static double[] maxValue(double[] tab1, double[] tab2)
	{
		if(tab1[1]>=tab2[1])
		{
			return tab1;
		}
		else
		{
			return tab2;
		}
	}
	
	/**
	 * Method that calculates the scalar product as indicated in the question
	 * @param cipherFrequency our frequency array
	 * @param index goes from 0 to ALPHABETSIZE-1
	 * @return the scalar product between ENGLISHFRENQUENCIES and cipherFrequency
	 */
	public static double produitScalaire(float[] cipherFrequency, int index)
	{
		int[] newIndexArray = indexArray(index);
		double produitScalaire = 0;
		int lengthEnglishFrenquency = ENGLISHFREQUENCIES.length;
		
		for(int indexValue=0; indexValue<lengthEnglishFrenquency; indexValue++)
		{
			produitScalaire += ENGLISHFREQUENCIES[indexValue]*cipherFrequency[newIndexArray[indexValue]];
		}
		
		return produitScalaire;
	}
	
	
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to decode a byte array encoded using a XOR 
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return the array of possibilities for the clear text
	 */
	public static byte[][] xorBruteForce(byte[] cipher) {
		
		int lenghtCipher = cipher.length;
		byte minByteValue = -128;
		byte maxByteValue = 127;
		
		byte[][] potentialCipher = new byte[ALPHABETSIZE][lenghtCipher];
		
		for(int potentialKey = minByteValue; potentialKey<(maxByteValue+1); potentialKey++)
		{	
			potentialCipher[potentialKey+maxByteValue+1] = Encrypt.xor(cipher, (byte)potentialKey);
		}

		return potentialCipher;
	}
	
	
	
	//-----------------------Vigenere-------------------------
	// Algorithm : see  https://www.youtube.com/watch?v=LaWp_Kq0cKs	
	/**
	 * Method to decode a byte array encoded following the Vigenere pattern, but in a clever way, 
	 * saving up on large amounts of computations
	 * @param cipher the byte array representing the encoded text
	 * @return the byte encoding of the clear text
	 */
	public static byte[] vigenereWithFrequencies(byte[] cipher) {
		
		List<Byte> removeSpacesArray = removeSpaces(cipher);
		int keyLength = vigenereFindKeyLength(removeSpacesArray);
		byte[] key = vigenereFindKey(removeSpacesArray, keyLength);
		byte[] plainText = Encrypt.vigenere(cipher, key);
		
		return plainText;
	}
	
	
	
	/**
	 * Helper Method used to remove the space character in a byte array for the clever Vigenere decoding
	 * @param array the array to clean
	 * @return a List of bytes without spaces
	 */
	public static List<Byte> removeSpaces(byte[] array){
		
		List<Byte> noSpacesArray = new ArrayList<Byte> ();
		int lengthSpaceArray = array.length;
		
		for(int byteIndex = 0; byteIndex<lengthSpaceArray; byteIndex++)
		{
			if (array[byteIndex] != Encrypt.SPACE) 
			{
				noSpacesArray.add(array[byteIndex]);
			}
		}
		return noSpacesArray;
	}

	
	
	/**
	 * Method that computes the key length for a Vigenere cipher text.
	 * @param cipher the byte array representing the encoded text without space
	 * @return the length of the key
	 */
	public static int vigenereFindKeyLength(List<Byte> cipher) {
		
		int[] numberOfOccurencesArray = numberOfOccurences(cipher);
		
		int keyLength = 0;
		List<Integer> localMaximumArray = localMaximum(numberOfOccurencesArray);
		
		Map<Integer, Integer> keyLengthMap = keyLength(localMaximumArray);
		
		for (Entry<Integer, Integer> pair : keyLengthMap.entrySet()) 
		{
			keyLength = pair.getKey();
		}
		
		return keyLength;
	}
	
	/**
	 * Method that counts the number of occurences in a byte List per shift of this same byte List
	 * @param cipher the byte List
	 * @return an int[] array with the number of occurences where each case represents a shift
	 */
	public static int[] numberOfOccurences(List<Byte> cipher)
	{
		int cipherLength = cipher.size();
		int[] occurencesArray = new int[cipherLength-1];		
		
		for(int indexIteration = 0; indexIteration<cipherLength-1; indexIteration++)
		{
			for(int indexMovableCipher = 0; indexMovableCipher<cipherLength-indexIteration-1; indexMovableCipher++)
			{
				if(cipher.get(indexMovableCipher+indexIteration+1)==cipher.get(indexMovableCipher))
				{
					occurencesArray[indexIteration]++;
				}
			}
		}
		return occurencesArray;
	}

	/**
	 * Method that reduces an int[] array with only it's local maximums
	 * @param occurencesArray the int[] array 
	 * @return a List with the local maximums of the argument occurencesArray
	 */
	public static List<Integer> localMaximum(int[] occurencesArray)
	{
		List<Integer> localMaximumList = new ArrayList<Integer>();
		
		int maxIndex = (int) Math.ceil( ( (double)(occurencesArray.length) )/2 );
		int secondToTheLeft = 0;
		int firstToTheLeft = 0;
		int firstToTheRight = 0;
		int secondToTheRight = 0;
		int valueInArray = 0;
		
		for(int index=0; index<maxIndex+1; index++)
		{
			valueInArray = occurencesArray[index];
			
			firstToTheRight = occurencesArray[index+1];
			secondToTheRight = occurencesArray[index+2];
			
			if(0==index)
			{
				firstToTheLeft = -1;
				secondToTheLeft = -1;
			}
			else if(1==index)
			{
				firstToTheLeft = occurencesArray[index-1];
				secondToTheLeft = -1;
			}
			else 
			{
				firstToTheLeft = occurencesArray[index-1];
				secondToTheLeft = occurencesArray[index-2];
			}
			
			if(secondToTheLeft<valueInArray && firstToTheLeft<valueInArray && firstToTheRight<valueInArray && secondToTheRight<valueInArray)
			{
				localMaximumList.add(index);
				//detail
				index+=2;
			}	
		}
		return localMaximumList;
	}
	
	/**
	 * Method that finds the length of a repeating pattern, therefore the key length
	 * @param localMaximumList <Integer> List with the numbers to analyze
	 * @return a HashMap with the key length as first argument
	 */
	public static Map<Integer, Integer> keyLength(List<Integer> localMaximumList)
	{
		Map<Integer, Integer> keyMap = new HashMap<>();
		//Map<length of key, number of times the length was found>
		
		int localMaximumListLength = localMaximumList.size();
		int[] distanceArray = new int[localMaximumListLength];
		int maxApparition = 0;
		int intermediateApparition = 1;
		int maxDistance = 0;
		
		
		// 1) distance between the local maximums
		for(int index=0; index<localMaximumListLength-1; index++)
		{
			distanceArray[index] = localMaximumList.get(index+1) - localMaximumList.get(index);
		}
		
		// 2) how many times the distance is repeated 
		// 3) if intermediateApparition>maxApparition we store the new information in the hashMap
		for(int firstIndex=0; firstIndex<localMaximumListLength-1; firstIndex++)
		{
			for(int secondIndex=firstIndex+1; secondIndex<localMaximumListLength-1; secondIndex++)
			{
				if(distanceArray[firstIndex]==distanceArray[secondIndex])
				{
					intermediateApparition++;
				}
				
				if(intermediateApparition>maxApparition)
				{
					maxApparition=intermediateApparition;
					maxDistance=distanceArray[firstIndex];
				}
			}
			intermediateApparition=1;
		}
		
		keyMap.put(maxDistance, maxApparition);
		
		return keyMap;	
	}
	
	
	/**
	 * Takes the cipher without space, and the key length, and uses the dot product with the English language frequencies 
	 * to compute the shifting for each letter of the key
	 * @param cipher the byte array representing the encoded text without space
	 * @param keyLength the length of the key we want to find
	 * @return the inverse key to decode the Vigenere cipher text
	 */
	public static byte[] vigenereFindKey(List<Byte> cipher, int keyLength) {
		
		byte[] key = new byte[keyLength];
		List<Byte> subCipher = new ArrayList<Byte>();
		int cipherLength = cipher.size();
		int subCipherLength = 0;
		
		for(int keyIndex = 0; keyIndex<keyLength; keyIndex++)
		{
			for(int indexCipher = keyIndex; indexCipher<cipherLength; indexCipher+=keyLength)
			{
				subCipher.add(cipher.get(indexCipher));
			}
			
			subCipherLength = subCipher.size();
			byte[] convertedSubCipher = new byte[subCipherLength];
			
			for(int index = 0; index<subCipherLength; index++)
			{
				convertedSubCipher[index] = subCipher.get(index);
			}
			
			key[keyIndex] = caesarWithFrequencies(convertedSubCipher);
			
			subCipher.clear();
		}
		return key;
	}
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method used to decode a String encoded following the CBC pattern
	 * @param cipher the byte array representing the encoded text
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return the clear text
	 */
	public static byte[] decryptCBC(byte[] cipher, byte[] iv) {
		
		int lengthIv = iv.length;
		int lengthCipher = cipher.length;
		int newByteIndex = 0;
		
		byte[] plainText = new byte[lengthCipher]; 	
		
		//We do the oneTimePad algorithm for all the elements of iv
		
		for(int byteIndex=0; byteIndex<lengthCipher && byteIndex<lengthIv; byteIndex++)
		{
			plainText[byteIndex] = (byte) (cipher[byteIndex] ^ iv[byteIndex]);
		}
		
		//If lengthIv>=lengthPlainText then the cipherText is already done
		
		if(lengthIv>=lengthCipher)
		{
			return plainText;
		}
		
		//else with a smart index we re-use the elements already in cipherText for the cbc decoding algorithm
		
		for(int byteIndex=lengthIv; byteIndex<lengthCipher; byteIndex++)
		{
			newByteIndex = byteIndex-lengthIv ;
			plainText[byteIndex] = (byte) (cipher[newByteIndex] ^ cipher[byteIndex]);
		}
		
		return plainText;
	}
	
	
	
	
	//-----------------------BONUS DECRYPT PCBC-------------------------
	
	
	/**
	 * Method used to decode a String encoded following the PCBC pattern
	 * @param cipher the byte array representing the encoded text
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return the clear text
	 */
	public static byte[] decryptPCBC(byte[] cipher, byte[] iv) {
		
		int lengthIv = iv.length;
		int lengthCipher = cipher.length;
		int newByteIndex = 0;
		
		byte[] plainText = new byte[lengthCipher]; 	
		
		//We do the oneTimePad algorithm for all the elements of iv
		
		for(int byteIndex=0; byteIndex<lengthCipher && byteIndex<lengthIv; byteIndex++)
		{
			plainText[byteIndex] = (byte) (cipher[byteIndex] ^ iv[byteIndex]);
		}
		
		//If lengthIv>=lengthPlainText then the cipherText is already done
		
		if(lengthIv>=lengthCipher)
		{
			return plainText;
		}
		
		//else with a smart index we re-use the elements already in cipherText for the pcbc decoding algorithm
		
		for(int byteIndex=lengthIv; byteIndex<lengthCipher; byteIndex++)
		{
			newByteIndex = byteIndex-lengthIv ;
			plainText[byteIndex] = (byte) (cipher[byteIndex] ^ (cipher[newByteIndex] ^ plainText[newByteIndex]) );
		}
		return plainText;
	}
	
	
		
}
