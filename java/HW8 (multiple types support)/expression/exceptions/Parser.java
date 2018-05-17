package expression.exceptions;

import expression.TripleExpression;

import java.text.ParseException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser<T> {
    TripleExpression<T> parse(String expression) throws Exception;
}
