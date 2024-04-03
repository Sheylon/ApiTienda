package com.pweb.tiendaonline.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pweb.tiendaonline.AbstractIntegrationDBTest;
import com.pweb.tiendaonline.entities.Pago;
import com.pweb.tiendaonline.entities.PagoMetodo;
import com.pweb.tiendaonline.entities.Pedido;
import com.pweb.tiendaonline.entities.PedidoStatus;

class PagoRepositoryTest extends AbstractIntegrationDBTest {
    
    private PagoRepository pagoRepository;
    private PedidoRepository pedidoRepository;

    @Autowired
    public PagoRepositoryTest(PagoRepository pagoRepository, 
                              PedidoRepository pedidoRepository){
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    private PedidoStatus pendienteStatus = PedidoStatus.PENDIENTE;
    private PedidoStatus enviadoStatus = PedidoStatus.ENVIADO;
    private Pedido firstOrder;
    private Pedido secondOrder;
    private Pedido thirdOrder;
    @SuppressWarnings("null")
    void initMockOrders(){
        
        firstOrder = Pedido.builder()
                    .fechaPedido(LocalDateTime.of(2023, 12, 24, 13, 30, 0))
                    .status(pendienteStatus)
                    .build();
        secondOrder = Pedido.builder()
                    .fechaPedido(LocalDateTime.of(2024, 3, 10, 12, 50, 0))
                    .status(enviadoStatus)
                    .build();
        thirdOrder = Pedido.builder()
                    .fechaPedido(LocalDateTime.of(2024, 1, 10, 12, 50, 0))
                    .status(enviadoStatus)
                    .build();
        
        pedidoRepository.save(firstOrder);
        pedidoRepository.save(secondOrder);
        pedidoRepository.save(thirdOrder);

        pedidoRepository.flush();
    }

    private PagoMetodo cashMethod = PagoMetodo.EFECTIVO;
    private PagoMetodo nequiMethod = PagoMetodo.NEQUI;
    private PagoMetodo daviplataMethod = PagoMetodo.DAVIPLATA;
    private Pago firstPayment;
    private Pago secondPayment;
    private Pago thirdPayment;
    private Pago fourthPayemnt;
    @SuppressWarnings("null")
    void initMockPayments(){

        firstPayment = Pago.builder()
                    .fechaPago(LocalDateTime.of(2023, 12, 24, 13, 30, 0))
                    .metodoPago(cashMethod)
                    .totalPago(200.000)
                    .pedido(firstOrder)
                    .build();
        secondPayment = Pago.builder()
                    .fechaPago(LocalDateTime.of(2024, 2, 24, 10, 30, 0))
                    .metodoPago(nequiMethod)
                    .totalPago(50.000)
                    .pedido(secondOrder)
                    .build();
        thirdPayment = Pago.builder()
                    .fechaPago(LocalDateTime.of(2023, 12, 24, 13, 30, 0))
                    .metodoPago(daviplataMethod)
                    .totalPago(600.000)
                    .pedido(thirdOrder)
                    .build();
        fourthPayemnt = Pago.builder()
                    .fechaPago(LocalDateTime.of(2023, 12, 24, 13, 30, 0))
                    .metodoPago(nequiMethod)
                    .pedido(firstOrder)
                    .totalPago(100.000)
                    .build();

        pagoRepository.save(firstPayment);
        pagoRepository.save(secondPayment);
        pagoRepository.save(thirdPayment);
        pagoRepository.save(fourthPayemnt);

        pagoRepository.flush();
    }

    @BeforeEach
    void setUp(){
        pagoRepository.deleteAll();
        pedidoRepository.deleteAll();
    }

    @SuppressWarnings("null")
    @Test
    public void PagoRepositoryTest_SaveAll_ReturnSavedPayment(){

        PagoMetodo cashMethod = PagoMetodo.EFECTIVO;

        Pago pago = Pago.builder()
                    .fechaPago(LocalDateTime.of(2023, 12, 24, 13, 30, 0))
                    .metodoPago(cashMethod)
                    .totalPago(200.000)
                    .build();
        Pago savedPayment = pagoRepository.save(pago);

        Assertions.assertThat(savedPayment).isNotNull();
        Assertions.assertThat(savedPayment.getId()).isGreaterThan(0);
        Assertions.assertThat(savedPayment.getTotalPago()).isEqualTo(200.000);
    }

    @SuppressWarnings("null")
    @Test
    public void PagoRepositoryTest_SaveAll_ReturnMoreThanOnePayment(){

        List<Pago> paymentList = pagoRepository.findAll();
        pagoRepository.saveAll(paymentList);
        
        Assertions.assertThat(paymentList).isNotNull();
        Assertions.assertThat(paymentList.size()).isEqualTo(2);
        Assertions.assertThat(paymentList).hasSize(2);
    }

    @SuppressWarnings("null")
    @Test
    public void PagoRepositoryTest_FindById_ReturnIsNotNull(){

        Long firstPaymentId = firstPayment.getId();
        Pago savedPayment = pagoRepository.findById(firstPaymentId).get();

        Assertions.assertThat(savedPayment).isNotNull();
        Assertions.assertThat(savedPayment.getMetodoPago()).isEqualTo(cashMethod);
    }

  /*  @Test
    public void PagoRepositoryTest_UpdatePayment_ReturnIsNotNull(){
        
        Long secondPaymentId = secondPayment.getId();
        Pago savedPayment = pagoRepository.findPagoById(secondPaymentId);
        savedPayment.setTotalPago(300.000);
        savedPayment.setFechaPago(LocalDateTime.now());

        Pago updatedPayment = pagoRepository.save(savedPayment);

        Assertions.assertThat(updatedPayment.getTotalPago()).isNotNull();
        Assertions.assertThat(updatedPayment.getTotalPago()).isNotEqualTo(200.000);
        Assertions.assertThat(updatedPayment.getFechaPago()).isNotNull();
        Assertions.assertThat(updatedPayment.getFechaPago()).isNotEqualTo(LocalDateTime.of(2024, 1, 05, 9, 15, 0));
        Assertions.assertThat(updatedPayment.getTotalPago()).isEqualTo(300.000);
    }*/

    @SuppressWarnings("null")
    @Test
    public void PagoRepositoryTest_DeletePayment_ReturnPaymentIsEmpty(){

        Long thirdPaymentId = thirdPayment.getId();
        pagoRepository.deleteById(thirdPaymentId);
        Optional<Pago> returnedPayment = pagoRepository.findById(thirdPaymentId);

        Assertions.assertThat(returnedPayment).isEmpty();
    }

    @Test
    public void PagoRepositoryTest_findPagoByFechaPagoBetween_ReturnIsEmpty(){

        LocalDateTime fechaInicial = LocalDateTime.of(2020, 10, 10, 10, 10);
        LocalDateTime fechaFinal = LocalDateTime.now();

        List<Pago> paymentList = pagoRepository.findPagoByFechaPagoBetween(fechaInicial, fechaFinal);

        Assertions.assertThat(paymentList.size()).isEqualTo(3);
        Assertions.assertThat(paymentList).isNotEmpty();
        Assertions.assertThat(paymentList).first().hasFieldOrPropertyWithValue("metodoPago", PagoMetodo.EFECTIVO);
    }

    @Test
    public void PagoRepositoryTest_findPagoByPedidoIdAndMetodoPago_ReturnIsEmpty(){

        Long firstOrderId = firstOrder.getId();
        List<Pago> paymentList = pagoRepository.findPagoByPedidoIdAndMetodoPago(firstOrderId, nequiMethod);

        Assertions.assertThat(paymentList).isNotEmpty();
        Assertions.assertThat(paymentList.size()).isGreaterThan(0);
        Assertions.assertThat(paymentList.contains(fourthPayemnt)).isTrue();
    }

}
