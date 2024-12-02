/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    // For differentiate tests, check:
    // - Differentiation of constants (should return 0)
    // - Differentiation of single variables (should return 1 or 0 for other variables)
    // - Differentiation of sums (should apply the sum rule)
    // - Differentiation of products (should apply the product rule)
    
    // For simplify tests, check:
    // - Simplification with single constants and variables
    // - Simplification with expressions where variables have known values
    // - Simplification of expressions with multiple additions and multiplications

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Test differentiation of a constant
    @Test
    public void testDifferentiateConstant() {
        Expression constant = new Constant(5);
        Expression result = Commands.differentiate(constant.toString(), "x");
        assertEquals(new Constant(0), result);
    }

    // Test differentiation of a variable
    @Test
    public void testDifferentiateVariable() {
        Expression variable = new Variable("x");
        Expression result = Commands.differentiate(variable.toString(), "x");
        assertEquals(new Constant(1), result);
    }

    // Test differentiation of a variable with respect to a different variable
    @Test
    public void testDifferentiateDifferentVariable() {
        Expression variable = new Variable("x");
        Expression result = Commands.differentiate(variable.toString(), "y");
        assertEquals(new Constant(0), result);
    }

    // Test differentiation of a sum
    @Test
    public void testDifferentiateSum() {
        Expression sum = new Sum(new Variable("x"), new Constant(3));
        Expression result = Commands.differentiate(sum.toString(), "x");
        assertEquals(new Sum(new Constant(1), new Constant(0)), result);
    }

    // Test differentiation of a product
    @Test
    public void testDifferentiateProduct() {
        Expression product = new Product(new Variable("x"), new Constant(2));
        Expression result = Commands.differentiate(product.toString(), "x");
        assertEquals(new Product(new Constant(1), new Constant(2)), result);
    }

    // Test simplification of a single constant
    @Test
    public void testSimplifyConstant() {
        Expression constant = new Constant(10);
        Expression result = Commands.simplify(constant.toString(), new HashMap<>());
        assertEquals(constant, result);
    }

    // Test simplification of a variable with environment
    @Test
    public void testSimplifyVariableWithEnvironment() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("x", 5.0);
        Expression variable = new Variable("x");
        Expression result = Commands.simplify(variable.toString(), environment);
        assertEquals(new Constant(5.0), result);
    }

    // Test simplification of an expression with constants and variables
    @Test
    public void testSimplifyExpressionWithEnvironment() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("x", 2.0);
        Expression sum = new Sum(new Variable("x"), new Constant(3));
        Expression result = Commands.simplify(sum.toString(), environment);
        assertEquals(new Constant(5.0), result);
    }

    // Test simplification with no matching variables in the environment
    @Test
    public void testSimplifyWithNoMatchingVariables() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("y", 4.0);
        Expression variable = new Variable("x");
        Expression result = Commands.simplify(variable.toString(), environment);
        assertEquals(variable, result);
    }

    // Test simplifying a product with known values
    @Test
    public void testSimplifyProduct() {
        Map<String, Double> environment = new HashMap<>();
        environment.put("x", 2.0);
        environment.put("y", 3.0);
        Expression product = new Product(new Variable("x"), new Variable("y"));
        Expression result = Commands.simplify(product.toString(), environment);
        assertEquals(new Constant(6.0), result);
    }
}
