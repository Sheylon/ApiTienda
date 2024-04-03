package com.pweb.tiendaonline.repositories;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pweb.tiendaonline.AbstractIntegrationDBTest;
import com.pweb.tiendaonline.entities.Cliente;

class ClienteRepositoryTest extends AbstractIntegrationDBTest {

    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteRepositoryTest(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    private Cliente firstClient;
    private Cliente secondClient;
    private Cliente thirdClient;


    void initMockClients(){
        firstClient = Cliente.builder()
                .nombre("Juan Perez")
                .email("juanperez@gmail.com")
                .direccion("Carrera 10 #20-30")
                .build();
        secondClient = Cliente.builder()
                .nombre("Maria Rodriguez")
                .email("mariarodriguez@gmail.com")
                .direccion("Calle 30 #15-25")
                .build();
        thirdClient = Cliente.builder()
                .nombre("Carlos Sanchez")
                .email("carlossanchez@gmail.com")
                .direccion("Avenida 5 #45-10")
                .build();

        clienteRepository.save(firstClient);
        clienteRepository.save(secondClient);
        clienteRepository.save(thirdClient);

        clienteRepository.flush();
    }


    @BeforeEach
    void setUp(){
        clienteRepository.deleteAll();
    }

    @SuppressWarnings("null")
    @Test
    public void ClienteRepository_SaveClient_ReturnSavedClient(){
        Cliente cliente = Cliente.builder()
                .nombre("Ana García")
                .email("anagarcia@gmail.com")
                .direccion("Calle 20 #10-15")
                .build();

        Cliente savedClient = clienteRepository.save(cliente);

        Assertions.assertThat(savedClient).isNotNull();
        Assertions.assertThat(savedClient.getId()).isGreaterThan(0);
        Assertions.assertThat(savedClient.getNombre()).isEqualTo("Ana García");
    }


    @SuppressWarnings("null")
    @Test
    public void ClienteRepository_SaveAll_ReturnMoreThanOneUsser(){

        List<Cliente> clientList = clienteRepository.findAll();
        clienteRepository.saveAll(clientList);
        
        Assertions.assertThat(clientList).isNotNull();
        Assertions.assertThat(clientList.size()).isEqualTo(0);
        Assertions.assertThat(clientList).hasSize(0);
    }

    @SuppressWarnings("null")
    @Test
    public void ClienteRepository_FindById_ReturnIfItIsNotNull(){
        
        Long firstClientId = firstClient.getId();
        Cliente client = clienteRepository.findById(firstClientId).get();

        Assertions.assertThat(client).isNotNull();
        Assertions.assertThat(client.getEmail()).isEqualTo("carlossanchez@gmail.com");
    }

    @SuppressWarnings("null")
    @Test
    public void ClienteRepository_UpdateClient_ReturnClientNotNull(){

        Long secondClientId = secondClient.getId();
        Optional<Cliente> currentClient = clienteRepository.findById(secondClientId);

        Assertions.assertThat(currentClient).isPresent();

        Cliente updatedClient = currentClient.get();
        updatedClient.setEmail("jose@gmail.com");
        updatedClient.setDireccion("Calle29i#21-D1");

        Cliente savedClient = clienteRepository.save(updatedClient);

        Assertions.assertThat(savedClient.getNombre()).isNotNull();
        Assertions.assertThat(savedClient.getEmail()).isNotNull();
        Assertions.assertThat(savedClient.getEmail()).isEqualTo("jose@gmail.com");
        Assertions.assertThat(savedClient.getDireccion()).isEqualTo("Calle29i#21-D1");
    }


    @SuppressWarnings("null")
    @Test
    public void ClienteRepository_DeleteClient_ReturnClientIsEmpty(){

        Long thirdClientId = thirdClient.getId();

        clienteRepository.deleteById(thirdClientId);
        
        Optional<Cliente> returnedClient = clienteRepository.findById(thirdClientId);
        Assertions.assertThat(returnedClient).isEmpty();
        Assertions.assertThat(returnedClient).isNotPresent();
    }

    @Test
    public void ClienteRepository_findClientByEmail_ReturnIsNotEmpty(){

        Cliente foundClient =  clienteRepository.findClienteByEmail("juanperez@gmail.com");

        Assertions.assertThat(foundClient).isNotNull();
        
        Assertions.assertThat(foundClient.getNombre()).isEqualTo("Juan Perez");
    }

    @Test
    public void ClienteRepository_findClientByDirection_ReturnIsNotNull(){

        List<Cliente> clientList = clienteRepository.findClienteByDireccionContainingIgnoreCase("Calle29i#21-D1");

        Long idFirstClient = clientList.get(0).getId();

        Assertions.assertThat(clientList).isNotEmpty();
        Assertions.assertThat(idFirstClient).isEqualTo(firstClient.getId());
    }

    @Test
    public void ClienteRepository_findClientByNombreIfStartsWith_severalAssertions(){

        List<Cliente> clientList = clienteRepository.findClienteByNombreStartsWith("Carlos Sanchez");

        Assertions.assertThat(clientList.get(0).getId()).isEqualTo(secondClient.getId());
        Assertions.assertThat(clientList).isNotEmpty();
        Assertions.assertThat(clientList.size()).isEqualTo(1);
        Assertions.assertThat(clientList).first().hasFieldOrPropertyWithValue("nombre", "Carlos Sanchez");
    }
    
}
