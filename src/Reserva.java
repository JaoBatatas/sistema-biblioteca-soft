import java.util.Date;

public class Reserva {
    private int id;
    private Date dataReserva;
    private Cadastro cadastro;
    private ItemAcervo itemAcervo;
    private boolean ativa = true;

    public Reserva(int id, Date dataReserva, Cadastro cadastro, ItemAcervo itemAcervo) {
        this.id = id;
        this.dataReserva = dataReserva;
        this.cadastro = cadastro;
        this.itemAcervo = itemAcervo;
    }

    public void cancelarReserva() {
        this.ativa = false;
        if (itemAcervo != null && cadastro != null) {
            itemAcervo.removerObservador(cadastro);
        }
    }

    public int getId() {
        return id;
    }

    public Date getDataReserva() {
        return dataReserva;
    }

    public Cadastro getCadastro() {
        return cadastro;
    }

    public ItemAcervo getItemAcervo() {
        return itemAcervo;
    }

    public boolean isAtiva() {
        return ativa;
    }
}