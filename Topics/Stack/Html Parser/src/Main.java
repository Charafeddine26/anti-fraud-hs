import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printTagValue(scanner.nextLine());
    }

    public static void printTagValue(String html) {
        Pattern p = Pattern.compile("<([a-z\\d]+)>(.+)</\\1>");
        Matcher m = p.matcher(html);
        while (m.find()) {
            printTagValue(m.group(2));
            System.out.println(m.group(2));
        }
    }
}
