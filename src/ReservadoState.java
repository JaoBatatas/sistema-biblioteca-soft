public class ReservadoState implements EstadoExemplar {
    private Exemplar exemplar;

    public ReservadoState() {
    }

    public ReservadoState(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    @Override
    public void emprestar(Exemplar exemplar) {
        exemplar.setEstado(new EmprestadoState());
    }

    @Override
    public void devolver(Exemplar exemplar) {
        exemplar.setEstado(new DisponivelState());
    }

    @Override
    public void reservar(Exemplar exemplar) {
        throw new IllegalStateException("Exemplar já está reservado.");
    }

    @Override
    public void liberar() {
        if (this.exemplar != null) {
            this.exemplar.setEstado(new DisponivelState());
        }
    }
}