package lab11_csp.active_object_time_mod.method_requests;

public interface MethodRequest {
    boolean guard();
    void execute();
}
