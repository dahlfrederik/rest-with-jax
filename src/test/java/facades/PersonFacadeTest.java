package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import utils.EMF_Creator;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2, p3; 
    private static Address a1, a2; 

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = PersonFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("Thor","Christensen", "45454545");
        p2 = new Person("Frederik","Dahl", "30303030");
        p3 = new Person("Josef", "Marc", "12345678"); 
        a1 = new Address("Tagensvej 154", "2200","København NV"); 
        a2 = new Address("Frederiksbergvej 1", "2000","Frederiksberg"); 
        p1.setAddress(a1);
        p2.setAddress(a2);
        
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testGetAllPersons() {        
        PersonsDTO result  = facade.getAllPersons(); 
        assertEquals(2, result.getAll().size(), "Expects two rows in the database");
    }
    
    @Test
    public void testGetPerson() throws PersonNotFoundException {
        PersonDTO person = facade.getPerson(p1.getId());
        assertEquals("Thor", person.getfName(), "Expects to find Thor");
    }

    @Test
    public void testAddPerson() throws MissingInputException{
        System.out.println("TESTING SIZE BEFORE TESTING ADD METHOD ....");
        assertEquals(2, facade.getAllPersons().getAll().size(), "Expects two rows in the database");
        facade.addPerson("Josef", "Marc", "12345678","Glostrupvej", "2600","Glostrup"); 
        System.out.println("TESTING SIZE AFTER ADD METHOD");
        assertEquals(3, facade.getAllPersons().getAll().size(), "Expects three rows in the database");
        
    }
    
   @Test
    public void testEditPerson() throws Exception {
        System.out.println("editPerson");
        PersonDTO p = new PersonDTO(p1);
        PersonDTO expResult = new PersonDTO(p1);
        expResult.setfName("Thomas");
        p.setfName("Thomas");
        PersonDTO result = facade.editPerson(p);
        assertEquals(expResult.getfName(), result.getfName());
    }
        
    @Test
    public void testEditPersonNotFoundException() {
    Exception exception = assertThrows(PersonNotFoundException.class, () -> {
        PersonDTO p4DTO = new PersonDTO(p2); 
        p4DTO.setId(8); 
        facade.editPerson(p4DTO); 
    });
    }
    
    @Test
    public void testEditPersonMissingInput() {
    Exception exception = assertThrows(MissingInputException.class, () -> {
        PersonDTO p4DTO = new PersonDTO(p2);
        p4DTO.setfName("");
        p4DTO.setlName("");
        facade.editPerson(p4DTO); 
    });
    }
}
