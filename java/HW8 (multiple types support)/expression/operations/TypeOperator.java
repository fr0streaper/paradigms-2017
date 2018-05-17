package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.EvaluationException;
import expression.exceptions.OverflowException;
import expression.exceptions.ParsingException;

public interface TypeOperator<T> {
  public T parseNumber(String number) throws Exception;

  public T add(T x, T y) throws OverflowException;

  public T subtract(T x, T y) throws OverflowException;

  public T multiply(T x, T y) throws OverflowException;

  public T divide(T x, T y) throws EvaluationException;

  public T min(T x, T y) throws EvaluationException;

  public T max(T x, T y) throws EvaluationException;

  public T count(T x) throws EvaluationException;

  public T negate(T x) throws OverflowException;
}
