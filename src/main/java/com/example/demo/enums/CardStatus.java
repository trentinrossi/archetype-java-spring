package com.example.demo.enums;

public enum CardStatus {
    ACTIVE('A', "Active"),
    INACTIVE('I', "Inactive"),
    BLOCKED('B', "Blocked"),
    PENDING('P', "Pending"),
    CLOSED('C', "Closed"),
    SUSPENDED('S', "Suspended"),
    EXPIRED('E', "Expired"),
    LOST('L', "Lost"),
    STOLEN('T', "Stolen"),
    DAMAGED('D', "Damaged");

    private final char code;
    private final String displayName;

    CardStatus(char code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public char getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CardStatus fromCode(char code) {
        for (CardStatus status : CardStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid card status code: " + code);
    }

    public static CardStatus fromCode(String code) {
        if (code == null || code.length() != 1) {
            throw new IllegalArgumentException("Card status code must be a single character");
        }
        return fromCode(code.charAt(0));
    }

    public static boolean isValidCode(char code) {
        for (CardStatus status : CardStatus.values()) {
            if (status.code == code) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidCode(String code) {
        if (code == null || code.length() != 1) {
            return false;
        }
        return isValidCode(code.charAt(0));
    }

    @Override
    public String toString() {
        return displayName;
    }
}
