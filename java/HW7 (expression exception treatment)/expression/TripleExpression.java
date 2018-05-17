package expression;

import expression.exceptions.EvaluationException;

public interface TripleExpression {
  int evaluate(int x, int y, int z) throws EvaluationException;
}