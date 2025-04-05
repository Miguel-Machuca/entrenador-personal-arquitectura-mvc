package com.example.personal_trainer.utils;

/**
 * Una tupla de tres elementos.
 *
 * @param <A> Tipo del primer elemento
 * @param <B> Tipo del segundo elemento
 * @param <C> Tipo del tercer elemento
 */
public final class Triple<A, B, C> {
    private final A first;
    private final B second;
    private final C third;

    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Metodo factory para crear una nueva Triple.
     *
     * @param a Primer elemento
     * @param b Segundo elemento
     * @param c Tercer elemento
     * @return Nueva instancia de Triple
     */
    public static <A, B, C> Triple<A, B, C> of(A a, B b, C c) {
        return new Triple<>(a, b, c);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;

        if (!first.equals(triple.first)) return false;
        if (!second.equals(triple.second)) return false;
        return third.equals(triple.third);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        result = 31 * result + third.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Triple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}