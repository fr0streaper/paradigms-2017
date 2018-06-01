package expression.parser;

import expression.*;

public class ExpressionParser implements Parser {
  private String expression, name;
  private int ind, value;

  private enum Token {LB, RB, MINUS, SUB, ADD, DIV, MUL, CONST, VAR, ERR, END, AND, OR, XOR, NOT, CNT}

  private Token token;

  private void getToken() {
    if (ind >= expression.length()) {
      token = Token.END;
      return;
    }

    char curr = expression.charAt(ind);
    switch (curr) {
      case '(':
        token = Token.LB;
        break;
      case ')':
        token = Token.RB;
        break;
      case '+':
        token = Token.ADD;
        break;
      case '-':
        if (token == Token.CONST || token == Token.VAR || token == Token.RB) {
          token = Token.SUB;
        } else {
          token = Token.MINUS;
        }
        break;
      case '*':
        token = Token.MUL;
        break;
      case '/':
        token = Token.DIV;
        break;
      case '&':
        token = Token.AND;
        break;
      case '|':
        token = Token.OR;
        break;
      case '^':
        token = Token.XOR;
        break;
      case '~':
        token = Token.NOT;
        break;
      default:
        if (Character.isDigit(curr)) {
          int left = ind;
          while (ind < expression.length() && Character.isDigit(expression.charAt(ind))) {
            ind++;
          }

          token = Token.CONST;
          value = Integer.parseUnsignedInt(expression.substring(left, ind));
          ind--;
        } else if (ind + 5 <= expression.length() && expression.substring(ind, ind + 5).equals("count")) {
          token = Token.CNT;
          ind += 4;
        } else {
          int left = ind;
          while (ind < expression.length() && Character.isLetter(expression.charAt(ind))) {
            ind++;
          }

          token = Token.VAR;
          name = expression.substring(left, ind);
          ind--;
        }
    }
    ind++;
  }

  private GenericExpression priority1() {
    getToken();

    switch (token) {
      case CONST:
        getToken();
        return new Const(value);
      case VAR:
        getToken();
        return new Variable(name);
      case MINUS:
        return new Minus(priority1());
      case LB:
        GenericExpression res = priority_or();
        getToken();
        return res;
      case NOT:
        return new Not(priority1());
      case CNT:
        return new Count(priority1());
      default:
        System.out.println("Primary expression missing; interpreting as 0");
        return new Const(0);
    }
  }

  private GenericExpression priority2() {
    GenericExpression expr = priority1();

    for (; ; ) {
      switch (token) {
        case MUL:
          expr = new Multiply(expr, priority1());
          break;
        case DIV:
          expr = new Divide(expr, priority1());
          break;
        default:
          return expr;
      }
    }
  }

  private GenericExpression priority3() {
    GenericExpression expr = priority2();

    for (; ; ) {
      switch (token) {
        case ADD:
          expr = new Add(expr, priority2());
          break;
        case SUB:
          expr = new Subtract(expr, priority2());
          break;
        default:
          return expr;
      }
    }
  }

  private GenericExpression priority_and() {
    GenericExpression expr = priority3();

    for (; ; ) {
      switch (token) {
        case AND:
          expr = new And(expr, priority3());
          break;
        default:
          return expr;
      }
    }
  }

  private GenericExpression priority_xor() {
    GenericExpression expr = priority_and();

    for (; ; ) {
      switch (token) {
        case XOR:
          expr = new Xor(expr, priority_and());
          break;
        default:
          return expr;
      }
    }
  }

  private GenericExpression priority_or() {
    GenericExpression expr = priority_xor();

    for (; ; ) {
      switch (token) {
        case OR:
          expr = new Or(expr, priority_xor());
          break;
        default:
          return expr;
      }
    }
  }

  public TripleExpression parse(String expression) {
    this.expression = expression.replaceAll("\\s+", "");
    ind = 0;
    token = Token.ERR;
    return priority_or();
  }
}