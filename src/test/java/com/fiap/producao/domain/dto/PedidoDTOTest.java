package com.fiap.producao.domain.dto;

import com.fiap.producao.domain.entity.StatusPedido; // Garante o import do Enum
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoDTOTest {

    @Test
    void deveCriarPedidoDTOCorretamente() {
        // Arrange
        String id = "123";
        StatusPedido status = StatusPedido.RECEBIDO; // Usa um valor válido do Enum

        // Act
        PedidoDTO dto = new PedidoDTO();
        dto.setId(id);
        dto.setStatus(status);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(status, dto.getStatus());

        // Testa o toString() ou outros métodos gerados pelo Lombok (@Data) se houver
        assertNotNull(dto.toString());
    }
}