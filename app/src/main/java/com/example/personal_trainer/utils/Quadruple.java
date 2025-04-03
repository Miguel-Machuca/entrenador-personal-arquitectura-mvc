package com.example.personal_trainer.utils;

/**
 * Una tupla de cuatro elementos, similar a Pair pero con cuatro valores.
 *
 * @param <A> Tipo del primer elemento
 * @param <B> Tipo del segundo elemento
 * @param <C> Tipo del tercer elemento
 * @param <D> Tipo del cuarto elemento
 */
public final class Quadruple<A, B, C, D> {
    private final A first;
    private final B second;
    private final C third;
    private final D fourth;


    public Quadruple(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    /**
     * Metodo factory para crear una nueva Quadruple.
     *
     * @param a Primer elemento
     * @param b Segundo elemento
     * @param c Tercer elemento
     * @param d Cuarto elemento
     * @return Nueva instancia de Quadruple
     */
    public static <A, B, C, D> Quadruple<A, B, C, D> of(A a, B b, C c, D d) {
        return new Quadruple<>(a, b, c, d);
    }

    // Getters
    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }

    public D getFourth() {
        return fourth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quadruple<?, ?, ?, ?> quadruple = (Quadruple<?, ?, ?, ?>) o;

        if (!first.equals(quadruple.first)) return false;
        if (!second.equals(quadruple.second)) return false;
        if (!third.equals(quadruple.third)) return false;
        return fourth.equals(quadruple.fourth);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        result = 31 * result + third.hashCode();
        result = 31 * result + fourth.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Quadruple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                ", fourth=" + fourth +
                '}';
    }
}