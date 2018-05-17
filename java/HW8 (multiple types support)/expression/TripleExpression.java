package expression;

import expression.exceptions.EvaluationException;

public interface TripleExpression<T> {
  T evaluate(T x, T y, T z) throws EvaluationException;
}