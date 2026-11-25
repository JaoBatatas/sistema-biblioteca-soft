public class DisponivelState implements EstadoExemplar {

    @Override
    public void emprestar(Exemplar exemplar) {
        exemplar.setEstado(new EmprestadoState());
    }

    @Override
    public void devolver(Exemplar exemplar) {
        throw new IllegalStateException("Exemplar já está disponível na biblioteca.");
    }

    @Override
    public void reservar(Exemplar exemplar) {
        exemplar.setEstado(new ReservadoState(exemplar));
    }

    @Override
    public void liberar() {
        throw new IllegalStateException("Exemplar já está liberado e disponível.");
    }
}