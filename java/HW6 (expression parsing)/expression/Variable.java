package expression;

public class Variable implements GenericExpression {
  public final char name;

  public Variable(char x) {
    name = x;
  }

  public int evaluate(int x, int y, int z) {
    switch (name) {
      case 'x': return x;
      case 'y': return y;
      case 'z': return z;
    }

    System.out.println("Error: invalid variable name; please use {x, y, z}");
    return 0;
  }
}