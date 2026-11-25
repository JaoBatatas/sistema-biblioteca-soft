public class Exemplar {
    private int id;
    private String codigo;
    private EstadoExemplar estadoAtual;

    public Exemplar(int id, String codigo) {
        this.id = id;
        this.codigo = codigo;
        this.estadoAtual = new DisponivelState();
    }

    public void devolver() {
        estadoAtual.devolver(this);
    }

    public void reservar() {
        estadoAtual.reservar(this);
    }

    public void emprestar() {
        estadoAtual.emprestar(this);
    }

    public void setEstado(EstadoExemplar estado) {
        this.estadoAtual = estado;
    }

    public boolean estaDisponivel() {
        return estadoAtual instanceof DisponivelState;
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public EstadoExemplar getEstadoAtual() {
        return estadoAtual;
    }
}