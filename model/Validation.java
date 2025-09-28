package model;

public final class Validation {
    private Validation() {}

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[^\\s@]+@[^\\s@]+\\.com$");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        if (!phone.matches("\\d+")) return false; 

        if (phone.startsWith("011")) {
            return phone.length() == 11;
        } else if (phone.startsWith("01")) {
            return phone.length() == 10;
        }
        return false;
    }
}
