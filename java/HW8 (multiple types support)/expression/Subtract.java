package expression;

import expression.exceptions.EvaluationException;
import expression.operations.TypeOperator;

public class Subtract<T> extends BinaryOperator<T> {
  public Subtract(TripleExpression<T> first, TripleExpression<T> second, TypeOperator<T> op) {
    super(first, second, op);
  }

  public T apply(T x, T y) throws EvaluationException {
    return operator.subtract(x, y);
  }
}