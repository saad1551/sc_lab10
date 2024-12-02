/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    // For the Expression ADT, the tests will cover:
    // - Creation of constants and variables
    // - Parsing strings to create expressions
    // - Converting expressions back to strings (toString)
    // - Equality checks (equals)
    // - HashCode consistency

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Test creation of a constant
    @Test
    public void testConstantCreation() {
        Expression constant = new Constant(5);
        assertEquals(new Constant(5), constant);
        assertEquals("5", constant.toString());
    }

    // Test creation of a variable
    @Test
    public void testVariableCreation() {
        Expression variable = new Variable("x");
        assertEquals(new Variable("x"), variable);
        assertEquals("x", variable.toString());
    }

    // Test parsing of a simple expression
    @Test
    public void testParseConstant() {
        Expression parsed = Expression.parse("5");
        assertEquals(new Constant(5), parsed);
    }

    @Test
    public void testParseVariable() {
        Expression parsed = Expression.parse("x");
        assertEquals(new Variable("x"), parsed);
    }

    @Test
    public void testParseSum() {
        Expression parsed = Expression.parse("3 + x");
        assertEquals(new Sum(new Constant(3), new Variable("x")), parsed);
    }

    @Test
    public void testParseProduct() {
        Expression parsed = Expression.parse("x * 2");
        assertEquals(new Product(new Variable("x"), new Constant(2)), parsed);
    }

    @Test
    public void testParseNestedExpression() {
        Expression parsed = Expression.parse("(x + 3) * 2");
        assertEquals(new Product(new Sum(new Variable("x"), new Constant(3)), new Constant(2)), parsed);
    }

    // Test equality
    @Test
    public void testEquals() {
        Expression e1 = new Sum(new Constant(3), new Variable("x"));
        Expression e2 = new Sum(new Constant(3), new Variable("x"));
        assertTrue(e1.equals(e2));
        assertFalse(e1.equals(new Sum(new Constant(4), new Variable("x"))));
        assertFalse(e1.equals(new Variable("x")));
    }

    // Test hashCode consistency with equals
    @Test
    public void testHashCode() {
        Expression e1 = new Sum(new Constant(3), new Variable("x"));
        Expression e2 = new Sum(new Constant(3), new Variable("x"));
        assertTrue(e1.equals(e2));
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    // Test toString consistency
    @Test
    public void testToString() {
        Expression e = new Product(new Variable("x"), new Constant(2));
        assertEquals("x * 2", e.toString());
    }

    // Test parsing and toString consistency
    @Test
    public void testParseToString() {
        String exprString = "3 + x * (2 + y)";
        Expression parsed = Expression.parse(exprString);
        assertEquals(exprString, parsed.toString());
        assertEquals(parsed, Expression.parse(parsed.toString()));
    }
}
