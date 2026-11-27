public class EmprestadoState implements EstadoExemplar {

    @Override
    public void emprestar(Exemplar exemplar) {
        throw new IllegalStateException("Exemplar já se encontra emprestado.");
    }

    @Override
    public void reservar(Exemplar exemplar) {
        throw new IllegalStateException("Não é possível reservar um exemplar emprestado antes da devolução.");
    }

    @Override
    public void liberar() {
        throw new IllegalStateException("Exemplar emprestado precisa ser devolvido antes de ser liberado.");
    }

    @Override
    public void devolver(Exemplar exemplar) {
        exemplar.setEstado(new DisponivelState());
    }
}