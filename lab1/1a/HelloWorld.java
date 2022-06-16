import java.util.StringJoiner;

class HelloWorld {
    public static void main(String[] args) {
        System.out.println(Helper.appendExclamationMark("Hello, " + name(args)));
    }

    private static String name(String[] args) {
        if (args.length > 0) {
            StringJoiner joiner = new StringJoiner(" ");
            for (String arg : args) {
                joiner.add(arg);
            }
            return joiner.toString();
        } else {
            return "Guest";
        }
    }
}

