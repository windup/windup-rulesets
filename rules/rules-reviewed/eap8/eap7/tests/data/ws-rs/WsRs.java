import jakarta.ws.rs.core.Context;

@Context
public class WsRs<T> {
    private T element;

    public WsRs(T element) {
        this.element = element;
    }

    public T get() {
        return element;
    }
}
