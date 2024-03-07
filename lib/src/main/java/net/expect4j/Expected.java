package net.expect4j;

/**
 * Expected contains either the promised value of type `T` or some exception.
 *
 * https://en.cppreference.com/w/cpp/utility/expected
 * https://doc.rust-lang.org/std/result/
 *
 * @param <T> The type of the expected value.
 */
public final class Expected<T> {

  /**
   * from executes function and provides an `Expected<T>` which either holds
   * a value or any error that might have been thrown.
   *
   * @param function
   * @return
   */
  public static <T> Expected<T> from(WithResult<T> function) {
    try {
      var value = function.call();
      return Expected.fulfill(value);
    }
    catch (Exception thrown) {
      return Expected.disappoint(thrown);
    }
  }

  /**
   * from executes function and provides an `Expected<Void>`. Unlike an
   * `Expected<T>`, it holds no value and can only be used to check if the
   * provided function erred.
   *
   * @param function
   * @return
   */
  public static Expected<Void> from(WithoutResult function) {
    try {
      function.call();
      return Expected.fulfill(null);
    }
    catch (Exception thrown) {
      return Expected.disappoint(thrown);
    }
  }

  /**
   * disappoint creates an `Expected<T>` which has erred with the provided
   * err.
   */
  public static <T> Expected<T> disappoint(Exception err) {
    var expected = new Expected<T>();
    expected.err = err;
    return expected;
  }

  /**
   * fulfill creates an `Expected<T>` which hasn't erred and holds the provided
   * value.
   */
  public static <T> Expected<T> fulfill(T value) {
    var expected = new Expected<T>();
    expected.value = value;
    return expected;
  }

  /**
   * get returns the expected value, unless an error was thrown.
   */
  public T get() {
    if (erred()) {
      throw new RuntimeException("do proper error handling!", err);
    } else {
      return value;
    }
  }

  /**
   * error returns the thrown error, if there was any
   */
  public Exception error() {
    return err;
  }

  /**
   * erred tells us whether an error was thrown.
   */
  public boolean erred() {
    return err != null;
  }

  /**
   * caught is a convenience function which checks if the potentially thrown
   * error matches the excepted class
   */
  public <T extends Exception> boolean caught(Class<T> excepted) {
    if (erred()) {
      return excepted.isAssignableFrom(err.getClass());
    }
    else {
      return false;
    }
  }

  private Expected() {}

  private T value;
  private Exception err;

  @FunctionalInterface
  public interface WithResult<T> {
    T call() throws Exception;
  }

  @FunctionalInterface
  public interface WithoutResult {
    void call() throws Exception;
  }
}
