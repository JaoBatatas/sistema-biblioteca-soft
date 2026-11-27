import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;

public class CorrecoesTest {
    private EmprestimoService service;
    private Cadastro u1, u2, u3;

    @BeforeEach
    public void setUp() {
        service = new EmprestimoService();
        u1 = new Cadastro(1, "U1", new Date(), true, "u1@x", "h", "1", 0, new PoliticaProfessor());
        u2 = new Cadastro(2, "U2", new Date(), true, "u2@x", "h", "2", 0, new PoliticaProfessor());
        u3 = new Cadastro(3, "U3", new Date(), true, "u3@x", "h", "3", 0, new PoliticaProfessor());
        service.registrarCadastro(u1);
        service.registrarCadastro(u2);
        service.registrarCadastro(u3);
    }

    @Test
    public void exemplaresDeItensDiferentesTemIdsUnicos() {
        Livro a = new Livro(1, "A", "A1", "X", new String[]{"c"}, 1, "pt");
        Livro b = new Livro(2, "B", "B1", "Y", new String[]{"c"}, 1, "pt");
        Exemplar ea = a.adicionarExemplar("A1-1");
        Exemplar eb = b.adicionarExemplar("B1-1");
        service.registrarItemAcervo(a);
        service.registrarItemAcervo(b);
        assertNotEquals(ea.getId(), eb.getId());
        service.criarEmprestimo(1, new int[]{ea.getId()});
        assertFalse(ea.estaDisponivel());
        assertTrue(eb.estaDisponivel());
    }

    @Test
    public void emprestimoMultiploNaoDeixaEstadoParcialQuandoFalha() {
        Livro l = new Livro(1, "L", "L1", "X", new String[]{"c"}, 1, "pt");
        Exemplar x = l.adicionarExemplar("L1-A");
        Exemplar y = l.adicionarExemplar("L1-B");
        service.registrarItemAcervo(l);
        y.emprestar();
        assertThrows(IllegalStateException.class,
                () -> service.criarEmprestimo(1, new int[]{x.getId(), y.getId()}));
        assertTrue(x.estaDisponivel());
        assertEquals(0, service.getEmprestimos().size());
    }

    @Test
    public void exemplarReservadoSoPodeSerEmprestadoAoDonoDaReserva() {
        Livro l = new Livro(1, "L", "L1", "X", new String[]{"c"}, 1, "pt");
        Exemplar ex = l.adicionarExemplar("L1-A");
        service.registrarItemAcervo(l);
        Emprestimo e = service.criarEmprestimo(1, new int[]{ex.getId()});
        service.reservarItem(2, 1);
        service.reservarItem(3, 1);
        service.devolverEmprestimo(e.getId());

        assertEquals(1, u2.getNotificacoesRecebidas().size());
        assertEquals(0, u3.getNotificacoesRecebidas().size()); // só o primeiro é avisado
        assertThrows(IllegalStateException.class, () -> service.criarEmprestimo(3, new int[]{ex.getId()}));
        assertNotNull(service.criarEmprestimo(2, new int[]{ex.getId()}));
        assertNotNull(l.proximaReserva()); // reserva do U3 continua na fila
    }

    @Test
    public void reservaCanceladaSaiDaFilaEDesbloqueiaRenovacao() {
        Livro l = new Livro(1, "L", "L1", "X", new String[]{"c"}, 1, "pt");
        Exemplar ex = l.adicionarExemplar("L1-A");
        service.registrarItemAcervo(l);
        Emprestimo e = service.criarEmprestimo(1, new int[]{ex.getId()});
        Reserva r = service.reservarItem(2, 1);
        assertFalse(service.renovarEmprestimo(e.getId()));
        r.cancelarReserva();
        assertNull(l.proximaReserva());
        assertTrue(service.renovarEmprestimo(e.getId()));
    }

    @Test
    public void naoPermiteReservaDuplicadaDoMesmoUsuario() {
        Livro l = new Livro(1, "L", "L1", "X", new String[]{"c"}, 1, "pt");
        Exemplar ex = l.adicionarExemplar("L1-A");
        service.registrarItemAcervo(l);
        service.criarEmprestimo(1, new int[]{ex.getId()});
        service.reservarItem(2, 1);
        assertThrows(IllegalStateException.class, () -> service.reservarItem(2, 1));
    }

    @Test
    public void relatorioAtrasadosNaoIncluiEmprestimoJaDevolvido() {
        Acervo acervo = new Acervo();
        Livro l = new Livro(1, "L", "L1", "X", new String[]{"c"}, 1, "pt");
        Exemplar ex = l.adicionarExemplar("L1-A");
        acervo.registrarItem(l);
        service.registrarItemAcervo(l);

        Emprestimo e = service.criarEmprestimo(1, new int[]{ex.getId()});
        e.setDataVencimento(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3)));
        acervo.registrarEmprestimo(e);

        assertEquals(1, acervo.relatorioAtrasados().length);
        service.devolverEmprestimo(e.getId());
        assertEquals(0, acervo.relatorioAtrasados().length);
    }

    @Test
    public void naoPermiteRenovarComMultaPendenteNemEmprestarParaInativo() {
        Livro l = new Livro(1, "L", "L1", "X", new String[]{"c"}, 1, "pt");
        Exemplar ex1 = l.adicionarExemplar("L1-A");
        Exemplar ex2 = l.adicionarExemplar("L1-B");
        service.registrarItemAcervo(l);

        Emprestimo e1 = service.criarEmprestimo(1, new int[]{ex1.getId()});
        Emprestimo e2 = service.criarEmprestimo(1, new int[]{ex2.getId()});
        e1.setDataVencimento(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)));
        service.devolverEmprestimo(e1.getId()); // gera multa pendente para u1

        assertFalse(service.renovarEmprestimo(e2.getId()));

        u2.setAtivo(false);
        assertThrows(IllegalArgumentException.class, () -> service.criarEmprestimo(2, new int[]{ex1.getId()}));
    }
}
