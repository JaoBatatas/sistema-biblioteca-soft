import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EmprestimoService {
    private List<Emprestimo> emprestimos = new ArrayList<>();
    private Map<Integer, Cadastro> cadastros = new HashMap<>();
    private Map<Integer, ItemAcervo> itensAcervo = new HashMap<>();
    private Map<Integer, Exemplar> exemplaresPorId = new HashMap<>();
    private NotificacaoFactory notificacaoFactory = new EmailNotificacaoFactory();

    private int proximoIdEmprestimo = 1;
    private int proximoIdMulta = 1;
    private int proximoIdReserva = 1;

    public void registrarCadastro(Cadastro cadastro) {
        if (cadastro != null) {
            cadastros.put(cadastro.getId(), cadastro);
        }
    }

    public void registrarItemAcervo(ItemAcervo item) {
        if (item != null) {
            itensAcervo.put(item.getId(), item);
            for (Exemplar e : item.getExemplares()) {
                exemplaresPorId.put(e.getId(), e);
            }
        }
    }

    public void setNotificacaoFactory(NotificacaoFactory notificacaoFactory) {
        this.notificacaoFactory = notificacaoFactory;
    }

    public Emprestimo criarEmprestimo(int usuarioID, int[] exemplaresIDs) {
        Cadastro usuario = cadastros.get(usuarioID);
        if (usuario == null || !usuario.isAtivo()) {
            throw new IllegalArgumentException("Usuário inválido ou inativo.");
        }
        if (usuario.possuiPendencia()) {
            throw new IllegalStateException("Usuário possui pendências (multa ou atraso).");
        }
        if (exemplaresIDs == null || exemplaresIDs.length == 0) {
            throw new IllegalArgumentException("Nenhum exemplar informado.");
        }

        PoliticaEmprestimo politica = usuario.getPolitica();
        long ativos = usuario.getEmprestimos().stream()
                .filter(e -> e.getStatus() == StatusEmprestimo.ATIVO)
                .count();
        if (ativos + exemplaresIDs.length > politica.obterLimiteEmprestimo()) {
            throw new IllegalStateException("Limite de empréstimos excedido para a política do usuário.");
        }

        Date agora = new Date();
        Date vencimento = new Date(agora.getTime() + TimeUnit.DAYS.toMillis(politica.obterPrazoMaximo()));
        Emprestimo ultimoCriado = null;

        for (int exemplarId : exemplaresIDs) {
            Exemplar exemplar = exemplaresPorId.get(exemplarId);
            if (exemplar == null || !exemplar.estaDisponivel()) {
                throw new IllegalStateException("Exemplar indisponível: " + exemplarId);
            }
            exemplar.emprestar();
            Emprestimo emprestimo = new Emprestimo(proximoIdEmprestimo++, agora, vencimento, usuario, exemplar);
            emprestimos.add(emprestimo);
            usuario.adicionarEmprestimo(emprestimo);
            ultimoCriado = emprestimo;
        }

        return ultimoCriado;
    }

    public Multa devolverEmprestimo(int emprestimoID) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoID);
        if (emprestimo == null || emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            throw new IllegalArgumentException("Empréstimo inválido ou já devolvido.");
        }

        emprestimo.setDataDevolucao(new Date());
        int diasAtraso = emprestimo.calcularAtraso();
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        Exemplar exemplar = emprestimo.getExemplar();
        exemplar.devolver();

        ItemAcervo itemPai = encontrarItemPorExemplar(exemplar);
        if (itemPai != null && itemPai.proximaReserva() != null) {
            exemplar.reservar();
            itemPai.notificarObservadores();
            Reserva atendida = itemPai.consumirProximaReserva();
            if (atendida != null && atendida.getCadastro() != null) {
                itemPai.removerObservador(atendida.getCadastro());
            }
        }

        if (diasAtraso > 0) {
            double valor = emprestimo.getCadastro().getPolitica().calcularMulta(diasAtraso);
            Multa multa = new Multa(proximoIdMulta++, valor, "PENDENTE");
            emprestimo.setMulta(multa);
            return multa;
        }
        return null;
    }

    public boolean pagarMulta(int emprestimoID) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoID);
        if (emprestimo == null || emprestimo.getMulta() == null) {
            return false;
        }
        emprestimo.getMulta().setStatus("PAGA");
        return true;
    }

    public void enviarVencimentosEAtrasos() {
        Date agora = new Date();
        long doisDiasMillis = TimeUnit.DAYS.toMillis(2);

        for (Emprestimo emp : emprestimos) {
            if (emp.getStatus() == StatusEmprestimo.DEVOLVIDO) {
                continue;
            }
            int atraso = emp.calcularAtraso();
            if (atraso > 0) {
                emp.setStatus(StatusEmprestimo.ATRASADO);
                Notificacao notif = notificacaoFactory.criarNotificacao(
                        "Empréstimo em Atraso",
                        "Seu empréstimo #" + emp.getId() + " está atrasado há " + atraso + " dia(s)."
                );
                notif.enviar(emp.getCadastro());
            } else {
                long restante = emp.getDataVencimento().getTime() - agora.getTime();
                if (restante > 0 && restante <= doisDiasMillis) {
                    Notificacao notif = notificacaoFactory.criarNotificacao(
                            "Vencimento Próximo",
                            "Seu empréstimo #" + emp.getId() + " vence em breve."
                    );
                    notif.enviar(emp.getCadastro());
                }
            }
        }
    }

    public boolean renovarEmprestimo(int emprestimoID) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoID);
        if (emprestimo == null) {
            return false;
        }
        ItemAcervo itemPai = encontrarItemPorExemplar(emprestimo.getExemplar());
        if (itemPai != null && itemPai.proximaReserva() != null) {
            return false;
        }
        return emprestimo.renovar(emprestimo.getCadastro().getPolitica());
    }

    public Reserva reservarItem(int cadastro, int item) {
        Cadastro usuario = cadastros.get(cadastro);
        ItemAcervo itemAcervo = itensAcervo.get(item);
        if (usuario == null || itemAcervo == null) {
            throw new IllegalArgumentException("Usuário ou item do acervo não encontrado.");
        }
        if (itemAcervo.quantidadeExemplaresDisponiveis() > 0) {
            throw new IllegalStateException("Há exemplares disponíveis; não é necessário reservar.");
        }
        Reserva reserva = new Reserva(proximoIdReserva++, new Date(), usuario, itemAcervo);
        itemAcervo.adicionarReservaNaFila(reserva);
        itemAcervo.adicionarObservador(usuario);
        return reserva;
    }

    private Emprestimo buscarEmprestimoPorId(int id) {
        return emprestimos.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private ItemAcervo encontrarItemPorExemplar(Exemplar exemplar) {
        for (ItemAcervo item : itensAcervo.values()) {
            if (item.getExemplares().contains(exemplar)) {
                return item;
            }
        }
        return null;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
}