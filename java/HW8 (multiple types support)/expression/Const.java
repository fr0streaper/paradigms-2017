package expression;

public class Const<T> implements TripleExpression<T> {
  public final T value;

  public Const(T x) {
    value = x;
  }

  public T evaluate(T x, T y, T z) {
    return value;
  }
}