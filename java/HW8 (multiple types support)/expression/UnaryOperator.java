package expression;

import expression.exceptions.EvaluationException;
import expression.operations.TypeOperator;

import java.text.ParseException;

public abstract class UnaryOperator<T> implements TripleExpression<T> {
  TripleExpression<T> expression;
  TypeOperator<T> operator;

  public UnaryOperator(TripleExpression<T> x, TypeOperator<T> op) {
    expression = x;
    operator = op;
  }

  public abstract T apply(T x) throws EvaluationException;

  public T evaluate(T x, T y, T z) throws EvaluationException {
    return apply(expression.evaluate(x, y, z));
  }
}