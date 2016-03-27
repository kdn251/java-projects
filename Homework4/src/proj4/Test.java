package proj4;

public class Test {

	
public static void main(String args[]) {
	NewQueue<Integer> test = new NewQueue<>();
	
	test.enqueue(1);
	test.enqueue(2);
	test.enqueue(3);
	
	while(!test.empty()) {
		System.out.println(test.peek());
		System.out.println(test.dequeue());
	}

}
	
	
}
