package expression;

import expression.exceptions.EvaluationException;

public class Variable<T> implements TripleExpression<T> {
  public final String name;

  public Variable(String x) {
    name = x;
  }

  public T evaluate(T x, T y, T z) {
    switch (name) {
      case "x": return x;
      case "y": return y;
      case "z": return z;
    }

    return null;
  }
}