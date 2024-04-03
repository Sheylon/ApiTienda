package com.pweb.tiendaonline.services;

import org.assertj.core.api.Assertions;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pweb.tiendaonline.dtos.pedido.PedidoDto;
import com.pweb.tiendaonline.entities.Cliente;
import com.pweb.tiendaonline.entities.Pago;
import com.pweb.tiendaonline.entities.PagoMetodo;
import com.pweb.tiendaonline.entities.Pedido;
import com.pweb.tiendaonline.entities.PedidoStatus;
import com.pweb.tiendaonline.repositories.PedidoRepository;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {
    
    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido order;
    private Pago payment;
    private Cliente client;
    private PagoMetodo cashMethod = PagoMetodo.EFECTIVO;
    private PedidoStatus sentState = PedidoStatus.ENVIADO;
    @BeforeEach
    void setUp(){
        order = Pedido.builder()
                .id(1l)   
                .status(sentState)
                .pago(payment)
                .fechaPedido(LocalDateTime.of(2023, 12, 23, 14, 20, 0))
                .cliente(client)
                .build();
        payment = Pago.builder()
               .id(2l)
               .metodoPago(cashMethod)
               .fechaPago(LocalDateTime.of(2024, 2, 28, 13, 30, 0))
               .totalPago(100.000)
               .build();
        Client = Cliente.builder()
                .id(3l)
                .nombre("Pedro Ramirez")
                .email("pedroramirez@example.com")
                .direccion("Avenida Central #789")
                .build();
    }

    @Test
    void pedidoServiceImpl_whenFindOrderById_ThenReturnOrder(){
        
        when(pedidoRepository.findById(1l)).thenReturn(Optional.ofNullable(order));
        
        PedidoDto savedOrder = pedidoService.findPedidoById(1l);

        Assertions.assertThat(savedOrder).isNotNull();
        Assertions.assertThat(savedOrder.status()).isEqualTo(sentState);
    }

    @Test
    void pedidoServiceImpl_whenFindAllOrders_ThenReturnOrderList(){
        
        List<Pedido> orders = pedidoRepository.findAll();
        when(pedidoRepository.findAll()).thenReturn(orders);

        List<PedidoDto> foundOrders = pedidoService.findAllPedidos();

        Assertions.assertThat(foundOrders.size()).isGreaterThan(0);
        Assertions.assertThat(foundOrders.contains(order)).isTrue();
        Assertions.assertThat(foundOrders).isEqualTo(1);
    }

    @Test
    void pedidoServiceImpl_whenDeleteOrder_ThenReturnDeletedOrderIsEmpty(){
        
        given(pedidoRepository.findById(1l)).willReturn(Optional.ofNullable(order));
        doNothing().when(pedidoRepository).deleteById(1l);

        PedidoDto deletedOrder = pedidoService.findPedidoById(1l);

        assertAll(() -> pedidoRepository.deleteById(1l));
        assertThat(deletedOrder).isNull();
    }

    @Test
    void pedidoServiceImpl_whenfindPedidoByFechaPedidoBetween_ThenReturnOrderList(){
        
        LocalDateTime fechaInicio = LocalDateTime.of(2023, 12, 1, 12, 0, 0);
        LocalDateTime fechaFinal = LocalDateTime.now();

        List<Pedido> orders = pedidoRepository.findPedidoByFechaPedidoBetween(fechaInicio, fechaFinal);
        given(pedidoRepository.findPedidoByFechaPedidoBetween(fechaInicio, fechaFinal)).willReturn(orders);

        List<PedidoDto> foundOrders = pedidoService.findPedidoByFechaPedidoBetween(fechaInicio, fechaFinal);

        Assertions.assertThat(foundOrders).isNotEmpty();
        assertThat(foundOrders.size()).isEqualTo(2);
        assertThat(foundOrders.contains(order)).isTrue();

    }

    @Test
    void pedidoServiceImpl_whenfindPedidoByClienteAndStatus_ThenReturnOrderList(){
        
        List<Pedido> orders = pedidoRepository.findPedidoByClienteAndStatus(3l, sentState);

        when(pedidoRepository.findPedidoByClienteAndStatus(3l, sentState)).thenReturn(orders);

        List<PedidoDto> foundOrders = pedidoService.findPedidoByClienteAndStatus(3l, sentState);

        Assertions.assertThat(foundOrders).isNotEmpty();
        assertThat(foundOrders.size()).isEqualTo(1);
        assertThat(foundOrders.contains(order)).isTrue();
    }

    @Test
    void pedidoServiceImpl_whenfindPedidoByClienteWithItems_ThenReturnOrderList(){
        
        List<Pedido> orders = pedidoRepository.findPedidoByClienteWithItems(3l);

        when(pedidoRepository.findPedidoByClienteWithItems(3l)).thenReturn(orders);

        List<PedidoDto> foundOrders = pedidoService.findPedidoByClienteWithItems(3l);

        Assertions.assertThat(foundOrders).isNotEmpty();
        assertThat(foundOrders.size()).isEqualTo(1);
        assertThat(foundOrders.contains(order)).isTrue();
    }
}
