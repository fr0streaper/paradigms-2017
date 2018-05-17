package expression.generic;

import expression.TripleExpression;
import expression.exceptions.ExpressionParser;
import expression.exceptions.UndefinedModeException;
import expression.operations.*;

public class GenericTabulator implements Tabulator {
  private TypeOperator<?> getOperator(String mode) throws UndefinedModeException {
    switch (mode) {
      case "i":
        return new IntegerOperator();
      case "bi":
        return new BigIntegerOperator();
      case "d":
        return new DoubleOperator();
      case "u":
        return new UncheckedIntegerOperator();
      case "l":
        return new UncheckedLongOperator();
      case "s":
        return new UncheckedShortOperator();
      default:
        throw new UndefinedModeException(mode);
    }
  }

  public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws UndefinedModeException {
    return getTable(getOperator(mode), expression, x1, x2, y1, y2, z1, z2);
  }

  private <T> Object[][][] getTable(TypeOperator<T> operator, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
    Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
    ExpressionParser<T> parser = new ExpressionParser<>(operator);
    TripleExpression<T> expr;
    try {
      expr = parser.parse(expression);
    } catch (Exception e) {
      return result;
    }

    for (int i = 0; i < x2 - x1 + 1; i++) {
      for (int j = 0; j < y2 - y1 + 1; j++) {
        for (int k = 0; k < z2 - z1 + 1; k++) {
          try {
            result[i][j][k] = expr.evaluate(operator.parseNumber(Integer.toString(i + x1)),
                operator.parseNumber(Integer.toString(j + y1)),
                operator.parseNumber(Integer.toString(k + z1)));
          } catch (Exception e) {

            result[i][j][k] = null;
          }
        }
      }
    }

    return result;
  }
}
