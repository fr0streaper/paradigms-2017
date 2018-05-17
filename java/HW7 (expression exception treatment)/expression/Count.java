package expression;

public class Count extends UnaryOperator {
  public Count(GenericExpression x) {
    super(x);
  }

  public void check(int x) {}
  public int apply(int x) {
    int res = 0;
    
    for (int i = 0; i < 32; i++) {
      if ((x & 1) == 1) {
        res++;
      }
      x >>= 1;
    }

    return res;
  }
}