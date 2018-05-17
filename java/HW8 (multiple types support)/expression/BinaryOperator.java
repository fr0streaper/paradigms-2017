package expression;

import expression.exceptions.EvaluationException;
import expression.operations.TypeOperator;

import java.text.ParseException;

public abstract class BinaryOperator<T> implements TripleExpression<T> {
  public final TripleExpression<T> expression1, expression2;
  public final TypeOperator<T> operator;

  public BinaryOperator(TripleExpression<T> first, TripleExpression<T> second, TypeOperator<T> op) {
    expression1 = first;
    expression2 = second;
    operator = op;
  }

  public abstract T apply(T x, T y) throws EvaluationException;

  public T evaluate(T x, T y, T z) throws EvaluationException {
    return apply(expression1.evaluate(x, y, z), expression2.evaluate(x, y, z));
  }
}