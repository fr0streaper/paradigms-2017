package expression;

import expression.exceptions.EvaluationException;

import java.text.ParseException;

public abstract class BinaryOperator implements GenericExpression {
  public final GenericExpression expression1, expression2;

  public BinaryOperator(GenericExpression first, GenericExpression second) {
    expression1 = first;
    expression2 = second;
  }

  public abstract int apply(int x, int y) throws EvaluationException;
  public abstract void check(int x, int y) throws EvaluationException;

  public int evaluate(int x, int y, int z) throws EvaluationException {
    return apply(expression1.evaluate(x, y, z), expression2.evaluate(x, y, z));
  }
}