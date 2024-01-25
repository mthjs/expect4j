package net.expect4j;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpectedTest {

  @Test
  void understands_primitives() {
    var expectation = Expected.from(() -> true);
    assertFalse(expectation.erred());
    assertNull(expectation.error());
    assertFalse(expectation.caught(Throwable.class));
    assertTrue(expectation.get());
  }

  @Test
  void understands_complexer_types() {
    var expectation = Expected.from(() -> Optional.of(true));
    assertFalse(expectation.erred());
    assertNull(expectation.error());
    assertFalse(expectation.caught(Throwable.class));
    assertEquals(expectation.get().getClass(), Optional.class);
    assertTrue(expectation.get().get());
  }

  @Test
  void throws_when_attempting_to_get_the_value_of_a_failed_expectation() {
    var expectation = Expected.from(() -> { throw new RuntimeException("failure!"); });
    assertTrue(expectation.erred());
    assertNotNull(expectation.error());
    assertTrue(expectation.caught(RuntimeException.class));
    assertThrows(RuntimeException.class, () -> expectation.get());
  }

  @Test
  void can_deal_with_checked_exceptions() {
    var expectation = Expected.from(() -> { throw new IOException(); });
    assertTrue(expectation.erred());
    assertNotNull(expectation.error());
    assertTrue(expectation.caught(IOException.class));
    assertThrows(RuntimeException.class, () -> expectation.get());
  }

  @Test
  void allows_to_infer_the_error_type_of_failed_expectations() {
    var expectation = Expected.from(() -> { throw new SpecializedError(); });
    assertTrue(expectation.erred());
    assertNotNull(expectation.error());
    assertTrue(expectation.caught(SpecializedError.class));
    assertThrows(RuntimeException.class, () -> expectation.get());
  }

  @SuppressWarnings("serial")
  class SpecializedError extends RuntimeException {
    SpecializedError() {
      super("special");
    }
  }
}
