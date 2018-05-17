package expression.exceptions;

import expression.*;

import java.text.ParseException;
import java.util.EnumSet;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class ExpressionParser implements Parser {
  private String expression, name;
  private int ind, value, balance;

  private enum Token {LB, RB, MINUS, SUB, ADD, DIV, MUL, CONST, VAR, ERR, END, POW, LOG, POWF, LOGF}

  private Token token;

  private void checkOperation() throws MissingOperatorException {
    if (token == Token.CONST || token == Token.VAR || token == Token.RB) {
      throw new MissingOperatorException(expression, ind);
    }
  }

  private void checkOperand() throws MissingOperandException {
    if (token != Token.CONST && token != Token.VAR && token != Token.RB) {
      throw new MissingOperandException(expression, ind);
    }
  }

  private void skipWhitespace() {
    //System.out.println("ok; called skipWhitespace; expression == " + expression + "; ind == " + ind);
    while (ind < expression.length() && Character.isWhitespace(expression.charAt(ind))) {
      ind++;
      //System.out.println("ok: ind == " + ind);
    }
  }

  private void getToken() throws Exception {
    skipWhitespace();
    if (ind >= expression.length()) {
      token = Token.END;
      return;
    }

    char curr = expression.charAt(ind);
    switch (curr) {
      case '(':
        if (ind > 0) {
          checkOperation();
        }
        balance++;
        token = Token.LB;
        break;
      case ')':
        balance--;
        if (balance < 0) {
          throw new MissingOpenBracketException(expression, ind);
        }
        token = Token.RB;
        break;
      case '+':
        checkOperand();
        token = Token.ADD;
        break;
      case '-':
        if (token == Token.CONST || token == Token.VAR || token == Token.RB) {
          token = Token.SUB;
        } else {
          if (ind + 1 >= expression.length()) {
            throw new MissingOperandException(expression, ind + 1);
          } else if (Character.isDigit(expression.charAt(ind + 1))) {
            int left = ++ind;
            while (ind < expression.length() && Character.isDigit(expression.charAt(ind))) {
              ind++;
            }

            token = Token.CONST;
            try {
              value = Integer.parseInt("-" + expression.substring(left, ind));
            } catch (Exception e) {
              throw new OverflowException();
            }
            ind--;
          } else {
            token = Token.MINUS;
          }
        }
        break;
      case '*':
        checkOperand();
        if (ind + 1 < expression.length() && expression.charAt(ind + 1) == '*') {
          ind++;
          token = Token.POW;
        } else {
          token = Token.MUL;
        }
        break;
      case '/':
        checkOperand();
        if (ind + 1 < expression.length() && expression.charAt(ind + 1) == '/') {
          ind++;
          token = Token.LOG;
        } else {
          token = Token.DIV;
        }
        break;
      default:
        if (Character.isDigit(curr)) {
          int left = ind;
          while (ind < expression.length() && Character.isDigit(expression.charAt(ind))) {
            ind++;
          }

          if (left != 0) {
            checkOperation();
          }
          token = Token.CONST;
          try {
            value = Integer.parseInt(expression.substring(left, ind));
          } catch (Exception e) {
            throw new OverflowException();
          }
          ind--;
        } else if (ind + 5 <= expression.length() && expression.substring(ind, ind + 5).equals("pow10")) {
          ind += 4;
          token = Token.POWF;
        } else if (ind + 5 <= expression.length() && expression.substring(ind, ind + 5).equals("log10")) {
          ind += 4;
          token = Token.LOGF;
        } else {
          int left = ind;
          while (ind < expression.length() && Character.isLetter(expression.charAt(ind))) {
            ind++;
          }

          if (left != 0) {
            checkOperation();
          }
          token = Token.VAR;
          name = expression.substring(left, ind);
          if (!(name.equals("x") || name.equals("y") || name.equals("z"))) {
            throw new InvalidVariableException(expression, ind);
          }
          ind--;
        }
    }
    ind++;
  }

  private GenericExpression priority1() throws Exception {
    getToken();

    switch (token) {
      case CONST:
        getToken();
        return new Const(value);
      case VAR:
        getToken();
        return new Variable(name);
      case MINUS:
        return new CheckedNegate(priority1());
      case LB:
        GenericExpression res = priority3();
        if (token != Token.RB) {
          throw new MissingCloseBracketException(expression, ind);
        }
        getToken();
        return res;
      case RB:
        throw new MissingOpenBracketException(expression, ind);
      case POWF:
        return new CheckedPow(new Const(10), priority1());
      case LOGF:
        return new CheckedLog(priority1(), new Const(10));
      default:
        throw new UnknownParsingException(expression, ind);
    }
  }

  private GenericExpression priority_powlog() throws Exception {
    GenericExpression expr = priority1();

    for (; ; ) {
      switch (token) {
        case POW:
          expr = new CheckedPow(expr, priority1());
          break;
        case LOG:
          expr = new CheckedLog(expr, priority1());
          break;
        default:
          return expr;
      }
    }
  }

  private GenericExpression priority2() throws Exception {
    GenericExpression expr = priority_powlog();

    for (; ; ) {
      switch (token) {
        case MUL:
          expr = new CheckedMultiply(expr, priority_powlog());
          break;
        case DIV:
          expr = new CheckedDivide(expr, priority_powlog());
          break;
        default:
          return expr;
      }
    }
  }

  private GenericExpression priority3() throws Exception {
    GenericExpression expr = priority2();

    for (; ; ) {
      switch (token) {
        case ADD:
          expr = new CheckedAdd(expr, priority2());
          break;
        case SUB:
          expr = new CheckedSubtract(expr, priority2());
          break;
        default:
          return expr;
      }
    }
  }

  public TripleExpression parse(String expression) throws Exception {
    this.expression = expression;
    ind = 0;
    token = Token.ERR;
    balance = 0;
    return priority3();
  }

  public static void main(String args[]) throws Exception {
    ExpressionParser parser = new ExpressionParser();
    System.out.println(parser.parse(Long.toString(1L << 32)).evaluate(1, 1, 1));
  }
}