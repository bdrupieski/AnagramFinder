package anagramutils.models;

public enum IsSameWhenRearrangedEnum {
    TRUE(1), FALSE(0), TOO_LONG_TO_COMPUTE(-1);

    private int databaseValue;

    IsSameWhenRearrangedEnum(int i) {
        this.databaseValue = i;
    }

    public int getDatabaseValue() {
        return databaseValue;
    }

    public static IsSameWhenRearrangedEnum fromDatabaseInt(int x) {
        switch(x) {
            case 0:
                return FALSE;
            case 1:
                return TRUE;
            case -1:
                return TOO_LONG_TO_COMPUTE;
        }
        return null;
    }
}
