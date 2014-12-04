
/**
 * 
 * @author Vanny Nguyen
 * Object to store characters and their frequencies
 *
 */
public class CharFreq {
	private Character c = null;
	private Integer f;
	public CharFreq(Character character, Integer frequency){
		c = character;
		f = frequency;
	}
	public CharFreq(Integer frequency){
		f = frequency;
	}
	public Character getChar(){
		return c;
	}
	public Integer getFreq(){
		return f;
	}
	public String toString(){
		if (c != null){
			return c+" : "+f;
		}
		return ""+f;
	}
}
