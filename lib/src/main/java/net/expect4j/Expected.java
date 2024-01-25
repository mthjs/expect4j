package net.expect4j;

import java.util.function.Supplier;

/**
 * Expected allows for exceptions to be caught and treated as values.
 *
 * https://en.cppreference.com/w/cpp/utility/expected
 *
 * @param <T> The type of the expected value.
 */
public class Expected<T> {

  /**
   * from executes supplier and provides an `Expected<T>` which either holds
   * a value or any error that might have been thrown.
   */
  public static <T> Expected<T> from(Supplier<T> supplier) {
    var expected = new Expected<T>();
    try {
      expected.value = supplier.get();
    }
    catch (Throwable thrown) {
      expected.thrown = thrown;
    }
    return expected;
  }

  /**
   * get returns the expeted value, unless an error was thrown.
   */
  public T get() {
    if (erred()) {
      throw new RuntimeException("do proper error handling!", thrown);
    } else {
      return value;
    }
  }

  /**
   * error returns the thrown error, if there was any
   */
  public Throwable error() {
    return thrown;
  }

  /**
   * erred tells us wheter an error was thrown.
   */
  public boolean erred() {
    return thrown != null;
  }

  /**
   * caught is a convinence function which checks if the the potentially thrown
   * error matches the excepted class
   */
  public <T extends Throwable> boolean caught(Class<T> excepted) {
    if (erred()) {
      return excepted.isAssignableFrom(thrown.getClass());
    }
    else {
      return false;
    }
  }

  private Expected() {}

  private T value;
  private Throwable thrown;
}
