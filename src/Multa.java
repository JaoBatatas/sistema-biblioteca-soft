public class Multa {
    private int id;
    private double valor;
    private String status;

    public Multa(int id, double valor, String status) {
        this.id = id;
        this.valor = valor;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public double getValor() {
        return valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}