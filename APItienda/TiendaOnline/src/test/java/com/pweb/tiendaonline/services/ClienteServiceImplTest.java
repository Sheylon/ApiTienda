package com.pweb.tiendaonline.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.pweb.tiendaonline.dtos.cliente.ClienteDto;
import com.pweb.tiendaonline.dtos.cliente.ClienteToSaveDto;
import com.pweb.tiendaonline.entities.Cliente;
import com.pweb.tiendaonline.repositories.ClienteRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;



@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {
    
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente firstClient;
    private Cliente secondClient;
    private Cliente thirdClient;
    private Cliente fourthClient;
    @BeforeEach
    void setUp(){
        firstClient = Cliente.builder()
                .id(1l)
                .nombre("Juan Perez")
                .email("juanperez@example.com")
                .direccion("Calle Principal #123")
                .build();
        secondClient = Cliente.builder()
                .id(2l)
                .nombre("María García")
                .email("mariagarcia@example.com")
                .direccion("Carrera 5 #45-67")
                .build();
        thirdClient = Cliente.builder()
                .id(3l)
                .nombre("Pedro Ramirez")
                .email("pedroramirez@example.com")
                .direccion("Avenida Central #789")
                .build();
        fourthClient = Cliente.builder()
                .id(4l)
                .nombre("Luisa Martínez")
                .email("luisamartinez@example.com")
                .direccion("Calle 10 #20-30")
                .build();
    }


    @SuppressWarnings("null")
    @Test
    void givenClient_WhenSaveClient_ThenReturnClient(){
        firstClient = Cliente.builder()
                .id(1l)
                .nombre("Juan Perez")
                .email("juanperez@example.com")
                .direccion("Calle Principal #123")
                .build();
        given(clienteRepository.save(any())).willReturn(firstClient);

        ClienteToSaveDto clientToSave = new ClienteToSaveDto(
                1l,
                "Juan Perez",
                "juanperez@example.com",
                "Calle Principal #123");

        ClienteDto dtoClient = clienteService.saveCliente(clientToSave);

        assertThat(dtoClient).isNotNull();
        assertThat(dtoClient.id()).isEqualTo(1l);
    }


    @Test
    void clientServiceImpl_findClienteById_ThenReturnsClient(){

        when(clienteRepository.findById(3l)).thenReturn(Optional.ofNullable(thirdClient));

        ClienteDto savedClient = clienteService.findClienteById(3l);

        Assertions.assertThat(savedClient).isNotNull();
        Assertions.assertThat(savedClient.nombre()).isEqualTo("Pedro Ramirez");
    }


    @SuppressWarnings("null")
    @Test
    void clientServiceImpl_updateClientById_ThenReturnClient(){

        ClienteToSaveDto clientToSave = new ClienteToSaveDto(
                1l,
                "Juan Perez",
                "juanperez@example.com",
                "Calle Principal #123");
        when(clienteRepository.findById(2l)).thenReturn(Optional.ofNullable(secondClient));
        when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(secondClient);

        ClienteDto savedClient = clienteService.updateCliente(2l, clientToSave);

        Assertions.assertThat(savedClient).isNotNull();
        Assertions.assertThat(savedClient.id()).isEqualTo(2l);
    }


    @SuppressWarnings("unlikely-arg-type")
    @Test
    void clientServiceImpl_findAllClientes_ThenReturnClients(){

        List<Cliente> clients = clienteRepository.findAll();

        when(clienteRepository.findAll()).thenReturn(clients);

        List<ClienteDto> foundClients = clienteService.findAllClientes();

        Assertions.assertThat(foundClients.size()).isGreaterThan(0);
        Assertions.assertThat(foundClients.contains(firstClient)).isTrue();
        Assertions.assertThat(foundClients).isEqualTo(3);
    }

    @Test
    void clientServiceImpl_DeleteClient_ThenReturnClient(){

        given(clienteRepository.findById(2l)).willReturn(Optional.ofNullable(secondClient));
        doNothing().when(clienteRepository).deleteById(2l);

        ClienteDto deletedClient = clienteService.findClienteById(2l);

        assertAll(() -> clienteRepository.deleteById(2l));
        assertThat(deletedClient).isNull();
    }

    @Test
    void clientServiceImpl_findClienteByEmail_ThenReturnClient(){

        given(clienteRepository.findClienteByEmail("juanperez@example.com")).willReturn(firstClient);

        ClienteDto savedClient = clienteService.findClienteByEmail("juanperez@example.com");

        Assertions.assertThat(savedClient).isNotNull();
        Assertions.assertThat(savedClient.nombre()).isEqualTo("Juan Perez");
    }


    @SuppressWarnings("unlikely-arg-type")
    @Test
    void clientServiceImpl_findClienteByDireccion_ThenReturnClient(){

        List<Cliente> clients = clienteRepository.findClienteByDireccionContainingIgnoreCase(thirdClient.getDireccion());

        given(clienteRepository.findClienteByDireccionContainingIgnoreCase("Calle29h#15-G4")).willReturn(clients);

        List<ClienteDto> foundClients = clienteService.findClienteByDireccionContainingIgnoreCase("Calle29h#15-G4");

        Assertions.assertThat(foundClients).isNotEmpty();
        assertThat(foundClients.size()).isEqualTo(2);
        assertThat(foundClients.contains(fourthClient)).isTrue();
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void clientServiceImpl_findClienteByNombreStartsWith_ThenReturnClient(){

        List<Cliente> clients = clienteRepository.findClienteByNombreStartsWith("Juan");

        given(clienteRepository.findClienteByNombreStartsWith("Juan")).willReturn(clients);

        List<ClienteDto> foundClients = clienteService.findClienteByNombreStartsWith("Juan");

        assertThat(foundClients.size()).isGreaterThan(0);
        assertThat(foundClients.contains(fourthClient)).isTrue();
        assertThat(foundClients.get(0).email()).isEqualTo("juanperez@example.com");
    }

}
