package com.pweb.tiendaonline.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pweb.tiendaonline.dtos.cliente.ClienteDto;
import com.pweb.tiendaonline.dtos.cliente.ClienteToSaveDto;
import com.pweb.tiendaonline.dtos.detalleEnvio.DetalleEnvioToSaveDto;
import com.pweb.tiendaonline.dtos.itemPedido.ItemPedidoDto;
import com.pweb.tiendaonline.dtos.pedido.PedidoDto;
import com.pweb.tiendaonline.dtos.pedido.PedidoToSaveDto;
import com.pweb.tiendaonline.entities.DetalleEnvio;
import com.pweb.tiendaonline.entities.Pedido;
import com.pweb.tiendaonline.entities.PedidoStatus;
import com.pweb.tiendaonline.repositories.ClienteRepository;
import com.pweb.tiendaonline.repositories.DetalleEnvioRepository;
import com.pweb.tiendaonline.repositories.ItemPedidoRepository;
import com.pweb.tiendaonline.repositories.PedidoRepository;

@ExtendWith(MockitoExtension.class)
public class DetalleEnvioServiceImplTest {
    
    @Mock
    private DetalleEnvioRepository detalleEnvioRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private DetalleEnvioServiceImpl detalleEnvioService;

    private PedidoStatus pendienteStatus = PedidoStatus.PENDIENTE;
    private PedidoStatus enviadoStatus = PedidoStatus.ENVIADO;
    private Pedido firstOrder;
    private Pedido secondOrder;
    private Pedido thirdOrder;
    @BeforeEach
    void setUpOrder(){
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
    }

    private DetalleEnvio firstShippingDetail;
    private DetalleEnvio secondShippingDetail;
    private DetalleEnvio thirdShippingDetail;
    @BeforeEach
    void setUpShippingDetail(){
        firstShippingDetail = DetalleEnvio.builder()
                            .id(1l)
                            .direccion("Calle29i#21-D1")
                            .numeroGuia("SEV9876543210")
                            .transportadora("Servientrega")
                            .pedido(firstOrder)
                            .build();
        secondShippingDetail = DetalleEnvio.builder()
                            .id(2l)
                            .direccion("Calle29i#21-D1")
                            .numeroGuia("IRV1234567890")
                            .transportadora("Inter Rapidísimo")
                            .pedido(secondOrder)
                            .build();
        thirdShippingDetail = DetalleEnvio.builder()
                            .id(3l)
                            .direccion("Calle#2-21D_Barrio: 21 de septiembre")
                            .numeroGuia("SEV2468135790")
                            .transportadora("Servientrega")
                            .pedido(thirdOrder)
                            .build();
    }

    
    private ItemPedidoDto itemPedidoDto;
    @BeforeEach
    void setUpOrderItem(){
        itemPedidoDto = new ItemPedidoDto(
            1l, 
            3, 
            20.000);

    }

    private DetalleEnvioToSaveDto firstShippingDetailToSaveDto;
    private DetalleEnvioToSaveDto secondShippingDetailToSaveDto;
    private DetalleEnvioToSaveDto thirdShippingDetailToSaveDto;
    private PedidoDto firstOrderToSaveDto;
    @BeforeEach
    void setUpShippingDetailToSaveDto(){
        firstShippingDetailToSaveDto = new DetalleEnvioToSaveDto(
            1l,
            "Calle29i#21-D1",
                "Envia",
            "SEV9876543210",
            null);
        secondShippingDetail = DetalleEnvio.builder()
                            .id(2l)
                            .direccion("Calle29i#21-D1")
                            .numeroGuia("IRV1234567890")
                            .transportadora("Inter Rapidísimo")
                            .pedido(secondOrder)
                            .build();
        thirdShippingDetail = DetalleEnvio.builder()
                            .id(3l)
                            .direccion("Calle#2-21D_Barrio: 21 de septiembre")
                            .numeroGuia("SEV2468135790")
                            .transportadora("Servientrega")
                            .pedido(thirdOrder)
                            .build();
    }

    @Test
    void shippingDetailService_whenSaveShippingDetail_ThenReturnShippingDetail(){
        firstShippingDetail = DetalleEnvio.builder()
                            .id(1l)
                            .direccion("Calle29i#21-D1")
                            .numeroGuia("SEV9876543210")
                            .transportadora("Servientrega")
                            .build();
        given(detalleEnvioRepository.save(any())).willReturn(firstShippingDetail);
    }
}
