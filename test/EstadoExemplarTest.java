import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EstadoExemplarTest {

    @Test
    public void deveRealizarTransicoesValidasDeEstadoDoExemplar() {
        Exemplar exemplar = new Exemplar(1, "EX-001");
        assertTrue(exemplar.estaDisponivel());
        assertTrue(exemplar.getEstadoAtual() instanceof DisponivelState);

        exemplar.emprestar();
        assertFalse(exemplar.estaDisponivel());
        assertTrue(exemplar.getEstadoAtual() instanceof EmprestadoState);

        exemplar.devolver();
        assertTrue(exemplar.estaDisponivel());
        assertTrue(exemplar.getEstadoAtual() instanceof DisponivelState);

        exemplar.reservar();
        assertFalse(exemplar.estaDisponivel());
        assertTrue(exemplar.getEstadoAtual() instanceof ReservadoState);

        exemplar.getEstadoAtual().liberar();
        assertTrue(exemplar.estaDisponivel());
    }

    @Test
    public void deveLancarExcecaoEmTransicoesInvalidas() {
        Exemplar exemplar = new Exemplar(2, "EX-002");

        assertThrows(IllegalStateException.class, exemplar::devolver);
        assertThrows(IllegalStateException.class, () -> exemplar.getEstadoAtual().liberar());

        exemplar.emprestar();
        assertThrows(IllegalStateException.class, exemplar::emprestar);
        assertThrows(IllegalStateException.class, () -> exemplar.getEstadoAtual().liberar());
    }
}