import java.util.List;
import java.util.Map;

public class ListMapNode {
	 
	private List<String> first; 
	private Map<String, Integer> second; 
	
	public ListMapNode(List<String> first, Map<String, Integer> second) {
		this.first = first;
		this.second = second; 
	} 
	
	public List<String> first() {
		return first; 
	}
	
	public Map<String, Integer> second() {
		return second; 
	}
	
}