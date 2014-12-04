
import java.util.*;
import java.io.*;
import javax.swing.JFileChooser;
/**
 * 
 * @author Vanny Nguyen  
 *
 */
public class HuffmanCompressor {

private Map<Character, Integer> charsAndFreqs= new HashMap<Character, Integer>();
private PriorityQueue<BinaryTree<CharFreq>> minPQ = new PriorityQueue<BinaryTree<CharFreq>>(11,new TreeComparator());
private Map<Character, String> charCodes = new HashMap<Character, String>();
private String inputPath = getFilePath();
private String outputPath = inputPath.substring(0, inputPath.indexOf(".txt"));
public HuffmanCompressor(){
	
 }
/**
 * @author Vanny Nguyen  
 * Frequency map for compression
 * @throws IOException
 */
public void makeKeyMap() throws IOException{
	//take in uncompressed file, add to map
	BufferedReader inputFile =  new BufferedReader(new FileReader(inputPath));
	try{
		int cCharInt = inputFile.read();
		while(cCharInt != -1){
			//cast Unicode to char
			char cChar = (char)cCharInt;
			//change to Character class
			cChar = new Character(cChar);
			if(charsAndFreqs.containsKey(cChar)){
				//increment Integer value
				int tempVal = charsAndFreqs.get(cChar).intValue() + 1;
				charsAndFreqs.put(cChar, new Integer(tempVal));
			}
			//value not in map
			else {
				charsAndFreqs.put(cChar, new Integer(1));
			}
			cCharInt = inputFile.read();
		}
	}
	finally{
		inputFile.close();
	}
 }

/**
 * @author Vanny Nguyen  
 * create CharFreq trees and insert in minPQ
 */
public void fillQueue() throws IOException{
	//iterate through Characters in map and create trees
	this.makeKeyMap();
	for(Character c:charsAndFreqs.keySet()){
		CharFreq temp = new CharFreq(c,charsAndFreqs.get(c));
		BinaryTree<CharFreq> tempTree = new BinaryTree<CharFreq>(temp);
		minPQ.add(tempTree);
	}
}

/**
 * @author Vanny Nguyen  
 * combine BinaryTrees within minPQ
 */

public void makeSingleTree() throws IOException{
	this.fillQueue();
	while(minPQ.size()>1){
		BinaryTree<CharFreq> left = minPQ.peek();
		minPQ.remove();
		BinaryTree<CharFreq> right = minPQ.peek();
		minPQ.remove();
		BinaryTree<CharFreq> merged = new BinaryTree<CharFreq>(new CharFreq(left.getValue().getFreq()+right.getValue().getFreq()),left,right);
		minPQ.add(merged);
	}
}
/**
 * @author Vanny Nguyen  
 * @param BinaryTree to traverse, String code
 */
public void makeCharCodes(BinaryTree<CharFreq> cTree, String code) throws IOException{
	//cTree.getValue().getChar()==null
	String lastCodeLeft = code;
	String lastCodeRight = code;
	if(cTree.getValue().getChar()==null){
		makeCharCodes(cTree.getLeft(),lastCodeLeft+="0");
		makeCharCodes(cTree.getRight(),lastCodeRight+="1");
	}
	else{
		if(code=="")
			charCodes.put(cTree.getValue().getChar(), "0");
		else
		charCodes.put(cTree.getValue().getChar(), code);
	}
}
/**
 * @author Vanny Nguyen  
 * @throws IOException
 * compresses text file
 */
public void compress() throws IOException{
	this.makeSingleTree();
	if(minPQ.peek()!=null){
	this.makeCharCodes(minPQ.peek(),"");
	BufferedReader inputFile =  new BufferedReader(new FileReader(inputPath));
	BufferedBitWriter outputCode = new BufferedBitWriter(outputPath+"-compressed.txt");
	try{
		
		int cCharInt = inputFile.read();
		while(cCharInt != -1){
			//cast Unicode to char
			char cChar = (char)cCharInt;
			//change to Character class
			cChar = new Character(cChar);
			//look up Character in code map
			String code = charCodes.get(cChar);
			//System.out.println(code);
			for(char c : code.toCharArray()){
				int i = 0;
				if((int)c==49)
					i = 1;
				outputCode.writeBit(i);
			}
			//write bit in output file
			cCharInt = inputFile.read();
			}
			
	}
	finally{
		inputFile.close();
		outputCode.close();
	}
	}
	
}

/**
 * @author Vanny Nguyen  
 * @throws IOException
 * Decompresses compressed file
 */
public void decompress() throws IOException{
	BufferedBitReader inputCode =  new BufferedBitReader(outputPath+"-compressed.txt");
	BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputPath+"-decompressed.txt"));
	if(minPQ.peek()!=null){
	try{
    int bit = 0;
		BinaryTree<CharFreq> temp = minPQ.peek();
		while(bit != -1){
			
			if(temp.getValue().getChar()==null){
				bit = inputCode.readBit();
				if(bit==0)
					temp = temp.getLeft();
				else
					temp = temp.getRight();
			}
			else{
				//code to deal with exception of single character file
				if(charsAndFreqs.keySet().size()<2){
					bit = inputCode.readBit();
					if(bit!=-1){
						char c = temp.getValue().getChar().charValue();
						outputFile.write((int)c);
						temp = minPQ.peek();
					}
				}
				else{
				char c = temp.getValue().getChar().charValue();
				outputFile.write((int)c);
				temp = minPQ.peek();
				}
			}
			
		}
			
	}
	finally{
		inputCode.close();
		outputFile.close();
	}
	}
}

public static String getFilePath() {
  // Create a file chooser.
  JFileChooser fc = new JFileChooser();
   
  int returnVal = fc.showOpenDialog(null);
  if (returnVal == JFileChooser.APPROVE_OPTION) {
    File file = fc.getSelectedFile();
    String pathName = file.getAbsolutePath();
    return pathName;
  }
  else
    return "";
 }

public static void main(String[] args) throws IOException{
	HuffmanCompressor test = new HuffmanCompressor();
	
	test.compress();
	test.decompress();
	
 }
}
