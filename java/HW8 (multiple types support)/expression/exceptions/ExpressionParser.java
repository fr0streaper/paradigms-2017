package expression.exceptions;

import expression.*;
import expression.operations.DoubleOperator;
import expression.operations.TypeOperator;

import java.text.ParseException;
import java.util.EnumSet;
import java.util.Set;

public class ExpressionParser<T> implements Parser<T> {
  private String expression, name;
  private int ind, balance;
  private T value;
  private TypeOperator<T> operator;

  private enum Token {LB, RB, MINUS, SUB, ADD, DIV, MUL, CONST, VAR, ERR, END, MIN, MAX, CNT}

  private Token token;

  public ExpressionParser(TypeOperator<T> op) {
    operator = op;
  }

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
    while (ind < expression.length() && Character.isWhitespace(expression.charAt(ind))) {
      ind++;
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
            while (ind < expression.length() && (Character.isDigit(expression.charAt(ind)) ||
                expression.charAt(ind) == '.' ||
                expression.charAt(ind) == 'e')) {
              if (expression.charAt(ind) == 'e') {
                ind++;
              }
              ind++;
            }

            token = Token.CONST;
            try {
              value = operator.parseNumber("-" + expression.substring(left, ind));
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
        token = Token.MUL;
        break;
      case '/':
        checkOperand();
        token = Token.DIV;
        break;
      default:
        if (Character.isDigit(curr)) {
          int left = ind;
          while (ind < expression.length() && (Character.isDigit(expression.charAt(ind)) ||
              expression.charAt(ind) == '.' ||
              expression.charAt(ind) == 'e')) {
            if (expression.charAt(ind) == 'e') {
              ind++;
            }
            ind++;
          }

          if (left != 0) {
            checkOperation();
          }
          token = Token.CONST;
          try {
            value = operator.parseNumber(expression.substring(left, ind));
          } catch (Exception e) {
            throw new OverflowException();
          }
          ind--;
        } else if (ind + 5 < expression.length() && expression.substring(ind, ind + 5).equals("count")) {
          token = Token.CNT;
          ind += 4;
        } else if (ind + 3 < expression.length() && expression.substring(ind, ind + 3).equals("min")) {
          token = Token.MIN;
          ind += 2;
        } else if (ind + 3 < expression.length() && expression.substring(ind, ind + 3).equals("max")) {
          token = Token.MAX;
          ind += 2;
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
            throw new ParsingException("Exception: variable name " + name + " is not acceptable");
          }
          ind--;
        }
    }
    ind++;
  }

  private TripleExpression<T> priority1() throws Exception {
    getToken();

    switch (token) {
      case CONST:
        getToken();
        return new Const<T>(value);
      case VAR:
        getToken();
        return new Variable<T>(name);
      case MINUS:
        return new Minus<T>(priority1(), operator);
      case LB:
        TripleExpression<T> res = priority3();
        if (token != Token.RB) {
          throw new MissingCloseBracketException(expression, ind);
        }
        getToken();
        return res;
      case CNT:
        return new Count<T>(priority1(), operator);
      case RB:
        throw new MissingOpenBracketException(expression, ind);
      default:
        throw new ParsingException("Unknown exception at non-space character " + ind);
    }
  }

  private TripleExpression<T> priority2() throws Exception {
    TripleExpression<T> expr = priority1();

    for (; ; ) {
      switch (token) {
        case MUL:
          expr = new Multiply<T>(expr, priority1(), operator);
          break;
        case DIV:
          expr = new Divide<T>(expr, priority1(), operator);
          break;
        default:
          return expr;
      }
    }
  }

  private TripleExpression<T> priority3() throws Exception {
    TripleExpression<T> expr = priority2();

    for (; ; ) {
      switch (token) {
        case ADD:
          expr = new Add<T>(expr, priority2(), operator);
          break;
        case SUB:
          expr = new Subtract<T>(expr, priority2(), operator);
          break;
        default:
          return expr;
      }
    }
  }

  private TripleExpression<T> priority_minmax() throws Exception {
    TripleExpression<T> expr = priority3();

    for (; ; ) {
      switch (token) {
        case MIN:
          expr = new Min<T>(expr, priority3(), operator);
          break;
        case MAX:
          expr = new Max<T>(expr, priority3(), operator);
          break;
        default:
          return expr;
      }
    }
  }

  public TripleExpression<T> parse(String expression) throws Exception {
    this.expression = expression;
    ind = 0;
    token = Token.ERR;
    balance = 0;
    return priority_minmax();
  }

  public static void main(String args[]) {
    ExpressionParser<Double> parser = new ExpressionParser<>(new DoubleOperator());
    try {
      System.out.println(parser.parse("1e5 + 1.4e-5+5").evaluate(0., 0., 0.));
    } catch (Exception e) {
      System.out.println("well that's bad");
    }
  }
}