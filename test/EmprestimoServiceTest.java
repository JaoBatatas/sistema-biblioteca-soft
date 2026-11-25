import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmprestimoServiceTest {

    private EmprestimoService service;
    private Cadastro aluno;
    private Cadastro professor;
    private Livro livro;
    private Exemplar exemplar1;
    private Exemplar exemplar2;

    @BeforeEach
    public void setUp() {
        service = new EmprestimoService();
        aluno = new Cadastro(1, "João Paulo", new Date(), true, "joao@udesc.br", "hash123", "47999990001", 0, new PoliticaAluno());
        professor = new Cadastro(2, "Prof. Carlos", new Date(), true, "carlos@udesc.br", "hash456", "47999990002", 0, new PoliticaProfessor());

        livro = new Livro(10, "Padrões de Projeto", "GOF-01", "Erich Gamma", new String[]{"Arquitetura"}, 395, "Português");
        exemplar1 = livro.adicionarExemplar("GOF-01-EX1");
        exemplar2 = livro.adicionarExemplar("GOF-01-EX2");

        service.registrarCadastro(aluno);
        service.registrarCadastro(professor);
        service.registrarItemAcervo(livro);
    }

    @Test
    public void deveCriarEmprestimoEAtualizarDisponibilidadeDoExemplar() {
        Emprestimo emp = service.criarEmprestimo(aluno.getId(), new int[]{exemplar1.getId()});

        assertNotNull(emp);
        assertEquals(StatusEmprestimo.ATIVO, emp.getStatus());
        assertFalse(exemplar1.estaDisponivel());
        assertEquals(1, livro.quantidadeExemplaresDisponiveis());
    }

    @Test
    public void deveBloquearEmprestimoQuandoExcederLimiteDaPolitica() {
        Exemplar ex3 = livro.adicionarExemplar("GOF-01-EX3");
        Exemplar ex4 = livro.adicionarExemplar("GOF-01-EX4");
        service.registrarItemAcervo(livro);

        service.criarEmprestimo(aluno.getId(), new int[]{exemplar1.getId(), exemplar2.getId(), ex3.getId()});

        assertThrows(IllegalStateException.class, () -> {
            service.criarEmprestimo(aluno.getId(), new int[]{ex4.getId()});
        });
    }

    @Test
    public void deveCalcularMultaNaDevolucaoEBloquearNovoEmprestimoAtePagamento() {
        Emprestimo emp = service.criarEmprestimo(aluno.getId(), new int[]{exemplar1.getId()});
        emp.setDataVencimento(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(4)));

        Multa multa = service.devolverEmprestimo(emp.getId());

        assertNotNull(multa);
        assertEquals(8.0, multa.getValor()); // 4 dias * R$ 2,00 (PoliticaAluno)
        assertEquals("PENDENTE", multa.getStatus());
        assertTrue(aluno.possuiPendencia());
        assertTrue(exemplar1.estaDisponivel());

        assertThrows(IllegalStateException.class, () -> {
            service.criarEmprestimo(aluno.getId(), new int[]{exemplar2.getId()});
        });

        assertTrue(service.pagarMulta(emp.getId()));
        assertFalse(aluno.possuiPendencia());
        assertNotNull(service.criarEmprestimo(aluno.getId(), new int[]{exemplar2.getId()}));
    }

    @Test
    public void deveReservarItemIndisponivelENotificarObservadorNaDevolucao() {
        assertThrows(IllegalStateException.class, () -> service.reservarItem(professor.getId(), livro.getId()));

        Emprestimo emp = service.criarEmprestimo(aluno.getId(), new int[]{exemplar1.getId(), exemplar2.getId()});
        assertEquals(0, livro.quantidadeExemplaresDisponiveis());

        Reserva reserva = service.reservarItem(professor.getId(), livro.getId());
        assertNotNull(reserva);
        assertEquals(reserva, livro.proximaReserva());

        service.devolverEmprestimo(emp.getId());

        assertTrue(exemplar2.getEstadoAtual() instanceof ReservadoState);
        assertEquals(1, professor.getNotificacoesRecebidas().size());
        assertTrue(professor.getNotificacoesRecebidas().get(0).contains("Padrões de Projeto"));
    }

    @Test
    public void deveRespeitarLimiteDeRenovacoesEBloquearSeHouverReserva() {
        Emprestimo emp = service.criarEmprestimo(aluno.getId(), new int[]{exemplar1.getId()});

        assertTrue(service.renovarEmprestimo(emp.getId()));
        assertTrue(service.renovarEmprestimo(emp.getId()));
        assertFalse(service.renovarEmprestimo(emp.getId())); // Máximo de 2 renovações para Aluno

        Emprestimo empProf = service.criarEmprestimo(professor.getId(), new int[]{exemplar2.getId()});
        service.reservarItem(aluno.getId(), livro.getId());

        assertFalse(service.renovarEmprestimo(empProf.getId())); // Bloqueado pois há reserva na fila
    }

    @Test
    public void deveDispararNotificacoesDeVencimentoProximoEAtrasoPorEmailESms() {
        Emprestimo empProximo = service.criarEmprestimo(aluno.getId(), new int[]{exemplar1.getId()});
        empProximo.setDataVencimento(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)));

        service.setNotificacaoFactory(new EmailNotificacaoFactory());
        service.enviarVencimentosEAtrasos();

        assertEquals(1, aluno.getNotificacoesRecebidas().size());
        assertTrue(aluno.getNotificacoesRecebidas().get(0).startsWith("[E-MAIL"));

        Emprestimo empAtrasado = service.criarEmprestimo(professor.getId(), new int[]{exemplar2.getId()});
        empAtrasado.setDataVencimento(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)));

        service.setNotificacaoFactory(new SmsNotificacaoFactory());
        service.enviarVencimentosEAtrasos();

        assertEquals(StatusEmprestimo.ATRASADO, empAtrasado.getStatus());
        assertEquals(1, professor.getNotificacoesRecebidas().size());
        assertTrue(professor.getNotificacoesRecebidas().get(0).startsWith("[SMS"));
    }
}