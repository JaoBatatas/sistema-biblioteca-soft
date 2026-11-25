import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class ItemAcervo {
    private int id;
    private String titulo;
    private String codigo;
    private String autor;
    private String[] categorias;

    private List<Exemplar> exemplares = new ArrayList<>();
    private List<Observador> observadores = new ArrayList<>();
    private Queue<Reserva> filaReservas = new LinkedList<>();
    private int proximoIdExemplar = 1;

    protected ItemAcervo() {
    }

    protected ItemAcervo(int id, String titulo, String codigo, String autor, String[] categorias) {
        this.id = id;
        this.titulo = titulo;
        this.codigo = codigo;
        this.autor = autor;
        this.categorias = categorias;
    }

    public Reserva proximaReserva() {
        return filaReservas.peek();
    }

    public int quantidadeExemplaresDisponiveis() {
        int disponiveis = 0;
        for (Exemplar e : exemplares) {
            if (e.estaDisponivel()) {
                disponiveis++;
            }
        }
        return disponiveis;
    }

    public Exemplar adicionarExemplar(String codigo) {
        Exemplar exemplar = new Exemplar(proximoIdExemplar++, codigo);
        exemplares.add(exemplar);
        return exemplar;
    }

    public void removerExemplar(int id) {
        exemplares.removeIf(e -> e.getId() == id);
    }

    public void adicionarObservador(Observador o) {
        if (o != null && !observadores.contains(o)) {
            observadores.add(o);
        }
    }

    public void removerObservador(Observador o) {
        observadores.remove(o);
    }

    public void notificarObservadores() {
        String mensagem = "O item '" + titulo + "' (código: " + codigo + ") possui exemplar disponível.";
        for (Observador o : new ArrayList<>(observadores)) {
            o.atualizar(mensagem);
        }
    }

    public void adicionarReservaNaFila(Reserva reserva) {
        if (reserva != null) {
            filaReservas.offer(reserva);
        }
    }

    public Reserva consumirProximaReserva() {
        return filaReservas.poll();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String[] getCategorias() {
        return categorias;
    }

    public void setCategorias(String[] categorias) {
        this.categorias = categorias;
    }

    public List<Exemplar> getExemplares() {
        return exemplares;
    }
}