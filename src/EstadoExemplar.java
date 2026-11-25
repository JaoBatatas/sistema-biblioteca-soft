public interface EstadoExemplar {
    void emprestar(Exemplar exemplar);
    void devolver(Exemplar exemplar);
    void reservar(Exemplar exemplar);
    void liberar();
}