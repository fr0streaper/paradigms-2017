package expression;

import expression.exceptions.EvaluationException;
import expression.operations.TypeOperator;

public class Add<T> extends BinaryOperator<T> {
  public Add(TripleExpression<T> first, TripleExpression<T> second, TypeOperator<T> op) {
    super(first, second, op);
  }

  public T apply(T x, T y) throws EvaluationException {
    return operator.add(x, y);
  }
}