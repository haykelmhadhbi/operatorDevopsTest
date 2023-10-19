package tn.esprit.devops_project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.services.OperatorServiceImpl;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional

class OperatorServiceImplTestJ {
    @Autowired
    OperatorRepository operatorRepository;
    @Autowired
    OperatorServiceImpl operatorService;

    @Test
    void retrieveAllOperators() {
        // Créez quelques opérateurs fictifs et ajoutez-les à la base de données
        Operator operator1 = new Operator(2L,"haykel", "mhadhbi", "password1");
        Operator operator2 = new Operator(4L,"fatma", "nouinoui", "password2");

        operatorRepository.save(operator1);
        operatorRepository.save(operator2);
        List<Operator> operators = operatorService.retrieveAllOperators();
        assertNotNull(operators);
        assertTrue(operators.size() >= 2);


        assertEquals("haykel", operators.get(0).getFname());
        assertEquals("fatma", operators.get(1).getFname());
    }

    @Test
    void addOperator() {
        // Créez un nouvel opérateur
        Operator newOperator = new Operator(5L,"haykel", "mhadhbi", "password");
        Operator savedOperator = operatorService.addOperator(newOperator);

        assertNotNull(savedOperator);
        assertNotNull(savedOperator.getIdOperateur());
        assertEquals("haykel", savedOperator.getFname());
        assertEquals("mhadhbi", savedOperator.getLname());
        assertEquals("password", savedOperator.getPassword());
        Operator operatorFromDatabase = operatorRepository.findById(savedOperator.getIdOperateur()).orElse(null);//Vérifier que l'opérateur ajouté existe dans la base de données après l'ajout :
        assertNotNull(operatorFromDatabase);

        long initialOperatorCount = operatorRepository.count();
        operatorService.addOperator(new Operator(5L,"haykel", "mhadhbi", "password"));//Vérifier que le nombre total d'opérateurs dans la base de données a augmenté d'un après ajout
        long updatedOperatorCount = operatorRepository.count();
        assertEquals(initialOperatorCount + 1, updatedOperatorCount);


    }

    @Test
    void deleteOperator() {
        Operator newOperator = new Operator(5L,"haykel", "mhadhbi", "password");
        Operator savedOperator = operatorService.addOperator(newOperator);

        Long operatorId = savedOperator.getIdOperateur();

        operatorService.deleteOperator(operatorId);

        Operator deletedOperator = operatorRepository.findById(operatorId).orElse(null);
        assertNull(deletedOperator);
        assertFalse(operatorRepository.existsById(operatorId));


    }
    @Test
    void updateOperator() {
        Operator newOperator = new Operator(5L,"haykel", "mhadhbi", "password");
        Operator savedOperator = operatorService.addOperator(newOperator);

        Long operatorId = savedOperator.getIdOperateur();

        savedOperator.setFname("fatma");
        savedOperator.setLname("nouioui");
        savedOperator.setPassword("newpassword");

        Operator updatedOperator = operatorService.updateOperator(savedOperator);

        assertEquals("fatma", updatedOperator.getFname());
        assertEquals("nouioui", updatedOperator.getLname());
        assertEquals("newpassword", updatedOperator.getPassword());

        assertEquals(operatorId, updatedOperator.getIdOperateur());

        Operator updatedOperatorFromDatabase = operatorRepository.findById(operatorId).orElse(null);
        assertNotNull(updatedOperatorFromDatabase);
        assertEquals("fatma", updatedOperatorFromDatabase.getFname());
        assertEquals("nouioui", updatedOperatorFromDatabase.getLname());
        assertEquals("newpassword", updatedOperatorFromDatabase.getPassword());

    }
    @Test
    void retrieveOperator() {
        Operator newOperator = new Operator(5L,"haykel", "mhadhbi", "password");
        Operator savedOperator = operatorService.addOperator(newOperator);

        Long operatorId = savedOperator.getIdOperateur();

        Operator retrievedOperator = operatorService.retrieveOperator(operatorId);

        assertNotNull(retrievedOperator);

        assertEquals("haykel", retrievedOperator.getFname());
        assertEquals("mhadhbi", retrievedOperator.getLname());
        assertEquals("password", retrievedOperator.getPassword());

        assertEquals(operatorId, retrievedOperator.getIdOperateur());
        Long nonExistentOperatorId = 9999L;
        assertThrows(NullPointerException.class, () -> operatorService.retrieveOperator(nonExistentOperatorId));



    }
}