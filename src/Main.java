import test.TestBeforeLaunch;

public class Main {

    public static void main(String[] args) {
        TestBeforeLaunch testBeforeLaunch = new TestBeforeLaunch();
        //testBeforeLaunch.create_test();
        //testBeforeLaunch.test_custom_link();
        testBeforeLaunch.test_file_utils();
        testBeforeLaunch.test_backup_app_after_close();
    }
}
