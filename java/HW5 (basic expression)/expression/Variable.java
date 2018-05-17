package expression;

public strictfp class Variable implements GenericExpression {
  public final String name;

  public Variable(String x) {
    name = x;
  }

  public int evaluate(int x) {
    return x;
  }

  public double evaluate(double x) {
    return x;
  }

  public int evaluate(int x, int y, int z) {
    switch (name) {
      case "x": return x;
      case "y": return y;
      case "z": return z;
    }

    System.out.println("Error: invalid variable name; please use {x, y, z}");
    return 0;
  }
}