import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void iterateOverList(ListIterator<String> iter) {
        // write your code here
        while (iter.hasNext()) {
            if(iter.next().equals("Hip")) {
                iter.add("Hop");
            }
        }
    }

    public static void printList(ListIterator<String> iter) {
        // write your code here
        for (ListIterator<String> it = iter; it.hasNext(); ) {
            System.out.println(it.next());
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> list = Arrays.stream(scanner.nextLine().split(" ")).collect(Collectors.toList());
        iterateOverList(list.listIterator());
        printList(list.listIterator());
    }
}
