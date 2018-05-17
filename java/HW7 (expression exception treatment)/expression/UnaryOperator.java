package expression;

import expression.exceptions.EvaluationException;

import java.text.ParseException;

public abstract class UnaryOperator implements GenericExpression {
  GenericExpression expression;

  public UnaryOperator(GenericExpression x) {
    expression = x;
  }

  public abstract void check(int x) throws EvaluationException;
  public abstract int apply(int x) throws EvaluationException;

  public int evaluate(int x, int y, int z) throws EvaluationException {
    return apply(expression.evaluate(x, y, z));
  }
}