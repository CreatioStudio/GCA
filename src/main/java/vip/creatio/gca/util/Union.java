package vip.creatio.gca.util;

import java.util.function.Consumer;

// something like union in C lang
@SuppressWarnings("unchecked")
public class Union<T> {

    private T union;

    private Union(T union) {
        this.union = union;
    }

    public static <S> Union<S> create() {
        return new Union<>(null);
    }

    public static <S> Union<S> of(S union) {
        return new Union<>(union);
    }

    public <S> S get() {
        return (S) union;
    }

    public <S> S get(Class<S> cls) {
        if (cls.isAssignableFrom(union.getClass())) {
            return (S) union;
        } else {
            throw new ClassCastException("Union " + union + " cannot be cast to " + cls.getName());
        }
    }

    public <S> S get(Consumer<Object> checker) {
        checker.accept(union);
        return (S) union;
    }

    public void set(Object union) {
        this.union = (T) union;
    }

    public <S> void set(S union, Consumer<S> checker) {
        if (union.getClass() != union.getClass()) {
            checker.accept(union);
        }
        this.union = (T) union;
    }
}
