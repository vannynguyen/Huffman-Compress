
import java.util.*;
/**
 * 
 * @author Vanny Nguyen
 * For use in HuffmanCompressor's MinPriorityQueue to compare characters & their frequencies
 *
 */
public class TreeComparator implements Comparator<BinaryTree<CharFreq>> {
	public int compare(BinaryTree<CharFreq> a, BinaryTree<CharFreq> b){
		if(a.getValue().getFreq()<b.getValue().getFreq())
			return -1;
		if(a.getValue().getFreq()==b.getValue().getFreq())
			return 0;
		else
			return 1;
	}
}
