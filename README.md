# expected4j

An `Expected` type for Java. Taking inspirations from [C++] and [Rust].

## Why?

Just to see if it was possible. 

Also, having a different way of dealing
with `Exceptions` than using `try/catch` opens up a different perspective
to errors.

## Examples

Functions can communicate there is an expected result, or some error.

```java
public Expected<double> sqrt(double v) {
  if (v < 0) {
    return Expected.disappoint(new IllegalArgumentException("there is no square-root defined for negative numbers"))
  } else {
    return Expected.fulfill(sqrt(v))
  } 
}
```

Existing code can be wrapped.

```java
var expected = Expected.from(() -> mightThrowTamperTantrums());
if (expected.erred()) {
  // log if you must...
  return;
}
var value = expected.get();
// do something with `value`
```

In the case of multiple different `Exceptions`, it is possible to determine the
specific type

```java
var expected = Expected.from(() -> mightThrowTamperTantrums());
if (expected.caught(NullpointerException.class)) {
  // https://www.infoq.com/presentations/Null-References-The-Billion-Dollar-Mistake-Tony-Hoare/ 
}
else if (expected.caught(IndexOutOfBoundsException.class)) { /**/ }
else if (expected.erred()) { /* any other exception */ }
else {
  var value = expected.get();
  // do something with `value`
}
```

[C++]: https://en.cppreference.com/w/cpp/utility/expected
[Rust]: https://doc.rust-lang.org/std/result/