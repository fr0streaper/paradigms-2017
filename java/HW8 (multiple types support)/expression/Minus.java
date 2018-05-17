package expression;

import expression.exceptions.EvaluationException;
import expression.operations.TypeOperator;

public class Minus<T> extends UnaryOperator<T> {
  public Minus(TripleExpression<T> x, TypeOperator<T> op) {
    super(x, op);
  }

  public T apply(T x) throws EvaluationException {
    return operator.negate(x);
  }
}