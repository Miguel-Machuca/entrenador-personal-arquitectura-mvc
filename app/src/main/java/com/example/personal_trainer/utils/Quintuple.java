package com.example.personal_trainer.utils;

/**
 * Una tupla de cinco elementos, similar a Pair pero con cinco valores.
 *
 * @param <A> Tipo del primer elemento
 * @param <B> Tipo del segundo elemento
 * @param <C> Tipo del tercer elemento
 * @param <D> Tipo del cuarto elemento
 * @param <E> Tipo del quinto elemento
 */
public final class Quintuple<A, B, C, D, E> {
    private final A first;
    private final B second;
    private final C third;
    private final D fourth;
    private final E fifth;

    public Quintuple(A first, B second, C third, D fourth, E fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
    }

    /**
     * Metodo factory para crear una nueva Quintuple.
     *
     * @param a Primer elemento
     * @param b Segundo elemento
     * @param c Tercer elemento
     * @param d Cuarto elemento
     * @param e Quinto elemento
     * @return Nueva instancia de Quintuple
     */
    public static <A, B, C, D, E> Quintuple<A, B, C, D, E> of(A a, B b, C c, D d, E e) {
        return new Quintuple<>(a, b, c, d, e);
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

    public E getFifth() {
        return fifth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quintuple<?, ?, ?, ?, ?> quintuple = (Quintuple<?, ?, ?, ?, ?>) o;

        if (!first.equals(quintuple.first)) return false;
        if (!second.equals(quintuple.second)) return false;
        if (!third.equals(quintuple.third)) return false;
        if (!fourth.equals(quintuple.fourth)) return false;
        return fifth.equals(quintuple.fifth);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        result = 31 * result + third.hashCode();
        result = 31 * result + fourth.hashCode();
        result = 31 * result + fifth.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Quintuple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                ", fourth=" + fourth +
                ", fifth=" + fifth +
                '}';
    }
}