class HelperTest {
    public static void main(String[] args) {
        String string = Helper.appendExclamationMark("Hello");
        if (string.equals("Hello!")) {
            System.out.println("HelperTest passes");
        } else {
            System.err.println("HelperTest failed");
        }
    }
}

