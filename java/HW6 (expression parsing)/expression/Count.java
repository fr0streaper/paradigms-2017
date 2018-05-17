package expression;

public class Count extends UnaryOperator {
  public Count(GenericExpression x) {
    super(x);
  }

  public int apply(int x) {
    int res = 0;
    
    while (x > 0) {
      if ((x & 1) == 1) {
        res++;
      }
      x >>= 1;
    }

    return res;
  }
}