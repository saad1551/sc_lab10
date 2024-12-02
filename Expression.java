/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {

    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        throw new RuntimeException("unimplemented");
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    /**
     * Differentiate this expression with respect to the given variable.
     * @param variable the variable to differentiate by
     * @return a new Expression that represents the derivative
     */
    public Expression differentiate(String variable);
    
    /**
     * Simplify this expression using the given environment, mapping variables to their values.
     * @param environment a map of variables to their values
     * @return a new Expression that represents the simplified form of the original expression
     */
    public Expression simplify(Map<String, Double> environment);
    
    /**
     * Evaluate this expression given a mapping of variables to their numeric values.
     * @param environment a map of variable names to values
     * @return the numeric result of evaluating this expression
     * @throws IllegalArgumentException if a variable in the expression is not in the environment
     */
    public double evaluate(Map<String, Double> environment);
}

/**
 * A concrete implementation representing a constant number in the expression.
 */
class Constant implements Expression {
    private final double value;

    public Constant(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object thatObject) {
        if (this == thatObject) return true;
        if (thatObject == null || getClass() != thatObject.getClass()) return false;
        Constant constant = (Constant) thatObject;
        return Double.compare(constant.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public Expression differentiate(String variable) {
        return new Constant(0); // Derivative of a constant is 0
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        return this; // Constants do not change in simplification
    }

    @Override
    public double evaluate(Map<String, Double> environment) {
        return value;
    }
}

/**
 * A concrete implementation representing a variable in the expression.
 */
class Variable implements Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object thatObject) {
        if (this == thatObject) return true;
        if (thatObject == null || getClass() != thatObject.getClass()) return false;
        Variable variable = (Variable) thatObject;
        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public Expression differentiate(String variable) {
        if (this.name.equals(variable)) {
            return new Constant(1); // Derivative of variable with respect to itself is 1
        } else {
            return new Constant(0); // Derivative of variable with respect to another variable is 0
        }
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        if (environment.containsKey(name)) {
            return new Constant(environment.get(name));
        }
        return this; // If variable not in environment, return as is
    }

    @Override
    public double evaluate(Map<String, Double> environment) {
        if (!environment.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " not found in environment.");
        }
        return environment.get(name);
    }
}

/**
 * A concrete implementation representing the sum of two expressions.
 */
class Sum implements Expression {
    private final Expression left;
    private final Expression right;

    public Sum(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " + " + right.toString();
    }

    @Override
    public boolean equals(Object thatObject) {
        if (this == thatObject) return true;
        if (thatObject == null || getClass() != thatObject.getClass()) return false;
        Sum sum = (Sum) thatObject;
        return left.equals(sum.left) && right.equals(sum.right);
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

    @Override
    public Expression differentiate(String variable) {
        return new Sum(left.differentiate(variable), right.differentiate(variable));
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        Expression simplifiedLeft = left.simplify(environment);
        Expression simplifiedRight = right.simplify(environment);
        if (simplifiedLeft instanceof Constant && simplifiedRight instanceof Constant) {
            return new Constant(((Constant) simplifiedLeft).evaluate(environment) + ((Constant) simplifiedRight).evaluate(environment));
        }
        return new Sum(simplifiedLeft, simplifiedRight);
    }

    @Override
    public double evaluate(Map<String, Double> environment) {
        return left.evaluate(environment) + right.evaluate(environment);
    }
}

/**
 * A concrete implementation representing the product of two expressions.
 */
class Product implements Expression {
    private final Expression left;
    private final Expression right;

    public Product(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " * " + right.toString();
    }

    @Override
    public boolean equals(Object thatObject) {
        if (this == thatObject) return true;
        if (thatObject == null || getClass() != thatObject.getClass()) return false;
        Product product = (Product) thatObject;
        return left.equals(product.left) && right.equals(product.right);
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

    @Override
    public Expression differentiate(String variable) {
        // Apply the product rule: (u * v)' = u' * v + u * v'
        return new Sum(
            new Product(left.differentiate(variable), right),
            new Product(left, right.differentiate(variable))
        );
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        Expression simplifiedLeft = left.simplify(environment);
        Expression simplifiedRight = right.simplify(environment);
        if (simplifiedLeft instanceof Constant && simplifiedRight instanceof Constant) {
            return new Constant(((Constant) simplifiedLeft).evaluate(environment) * ((Constant) simplifiedRight).evaluate(environment));
        }
        return new Product(simplifiedLeft, simplifiedRight);
    }

    @Override
    public double evaluate(Map<String, Double> environment) {
        return left.evaluate(environment) * right.evaluate(environment);
    }
}
