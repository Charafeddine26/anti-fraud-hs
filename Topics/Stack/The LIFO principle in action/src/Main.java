import java.util.Scanner;
import java.util.Stack;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        Integer size = scanner.nextInt();
        Stack stack = new Stack();

        for(int i = 0; i < size; i++) {
            stack.push(scanner.nextInt());
        }

        while(!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
