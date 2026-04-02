package main.utility;

public enum MembershipType {
    BRONZE ("0%", 100),
    SILVER ("5%", 95),
    GOLD ("10%", 90),
    PLATINUM("15%", 85);

    private final String percentage;
    private final int calculateValue;

    MembershipType (String percentage, int calculateValue) {
        this.percentage = percentage;
        this.calculateValue = calculateValue;
    }

    public String getPercentage() {
        return percentage;
    }

    public int calculateDiscountedPrice(int price) {

        return (price * this.calculateValue / 100) / 10 * 10;   // truncate ones digit
    }
}
