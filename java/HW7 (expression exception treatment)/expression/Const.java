package expression; 

public class Const implements GenericExpression {
  public final Number value;

  public Const(Number x) {
    value = x;
  }

  public int evaluate(int x, int y, int z) {
    return value.intValue();
  }
}