package be.kuleuven.foodrestservice.domain;

public enum MealType {

    //在枚举中，每个枚举项都有一个 value 属性，通过构造函数 MealType(String v) 袋设置
    VEGAN("vegan"),
    VEGGIE("veggie"),
    MEAT("meat"),
    FISH("fish");
    private final String value;

    MealType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MealType fromValue(String v) {
        for (MealType c: MealType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
