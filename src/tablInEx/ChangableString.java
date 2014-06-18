package tablInEx;

public class ChangableString {
    String str;

    public ChangableString(String str) {
        this.str = str;
    }

    public void changeTo(String newStr) {
        str = newStr;
    }

    public String toString() {
        return str;
    }
}
