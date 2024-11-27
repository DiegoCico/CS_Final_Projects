package cs3500.threetrios.card;

/**
 * Enum representing card numbers (1-9 and A).
 */
public enum NUMBER {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  A(10);

  private final int value;

  NUMBER(int value) {
    this.value = value;
  }

  /**
   * Converts an integer to a {@code NUMBER}.
   *
   * @param value the integer value
   * @return the corresponding {@code NUMBER}
   * @throws IllegalArgumentException if the value is invalid
   */
  public static NUMBER fromValue(int value) {
    for (NUMBER number : NUMBER.values()) {
      if (number.getValue() == value) {
        return number;
      }
    }
    throw new IllegalArgumentException("Invalid number: " + value);
  }

  /**
   * getting the value.
   * @return the numeric value of the {@code NUMBER}
   */
  public int getValue() {
    return value;
  }
}
