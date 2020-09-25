package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {
    private static PersonDTO aPersonDTO = new PersonDTO("Josef", "Marc", "12345678"); 

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    //An alternative way to get the EntityManagerFactory, whithout having to type the details all over the code
    //EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    private static final PersonFacade pf = PersonFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("all")
    public String allPersons() {      
            PersonsDTO personsDTO = pf.getAllPersons();
            return GSON.toJson(personsDTO);
        
    }

    @GET
    @Path("id/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonById(@PathParam("id") int id) throws PersonNotFoundException {    
            PersonFacade mf = PersonFacade.getFacadeExample(EMF);
            PersonDTO personDTO = pf.getPerson(id);
            return GSON.toJson(personDTO);
        
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPerson(String person) throws MissingInputException {
        aPersonDTO = GSON.fromJson(person, PersonDTO.class);
        PersonDTO personDTO = pf.addPerson(aPersonDTO.getfName(), aPersonDTO.getlName(), aPersonDTO.getPhone()); 
        
        return GSON.toJson(personDTO); 

        //aPersonDTO = GSON.toJson(pf.addPerson(aPersonDTO.getfName(), aPersonDTO.getlName(), aPersonDTO.getPhone()));
        
    }
    
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePerson(@PathParam("id") int id) throws PersonNotFoundException{
        PersonDTO pd = pf.deletePerson(id);
        
        return "Deleted: " + GSON.toJson(pd);
    }
    
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String updatePerson(@PathParam("id") int id,  String person) throws PersonNotFoundException, MissingInputException {
        PersonDTO pDTO = GSON.fromJson(person, PersonDTO.class);
        Person p = new Person(pDTO.getfName(), pDTO.getlName(), pDTO.getPhone());
        p.setId(id);
        PersonDTO pNew = new PersonDTO(p); 
        pf.editPerson(pNew);
        return GSON.toJson(pNew);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("populate")
    public String populateDB() {
        try {
            pf.populateDB();
            return "{\"msg\":\"2 persons added\"}";
        } catch (Exception e) {
            String errorString = "The populate method could not be run. Nothing has been inserted into the database";
            return GSON.toJson(errorString);
        }
    }

}
