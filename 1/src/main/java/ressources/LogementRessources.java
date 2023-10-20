package ressources;

import entities.Logement;
import metiers.LogementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("logements")

public class LogementRessources {
    public static LogementBusiness logementMetier = new LogementBusiness();

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    //@Consumes("application/xml")
    public Response addLogement(Logement l) {
     if(logementMetier.addLogement(l))
         return  Response.status(Status.CREATED).build();
     return  Response.status(Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogements(@QueryParam("delegation") String delegation,@QueryParam("reference") String reference) {
        List<Logement> liste=new ArrayList<Logement>();
        if(reference == null && delegation != null) {
            liste = logementMetier.getLogementsByDeleguation(delegation);

        } else if(delegation == null && reference !=null ) {
           liste =logementMetier.getLogementsListeByref(Integer.parseInt(reference));
        } else {
            liste = logementMetier.getLogements() ;
        }

           if(liste.size()==0)
               return  Response.status(Status.NOT_FOUND).build();
        return  Response.status(Status.OK).entity(liste).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    @Path("{reference}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateLogement(Logement updatedLogement,@PathParam("reference") int reference) {


        if (logementMetier.updateLogement(reference,updatedLogement)) {
            return Response.status(Status.OK).entity("logement updated").build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

        @DELETE
        @Path("{reference}")
        @Produces(MediaType.TEXT_PLAIN)
        public  Response deleteLogement(@PathParam("reference") int reference){
           if(logementMetier.deleteLogement(reference))
                    return Response.status(Status.OK).entity("logement deleted").build();


            return Response.status(Status.NOT_FOUND).build();

        }

    @GET
    @Path("deleguation/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogements1(@PathParam("value") String delegation) {
        List<Logement> liste=new ArrayList<Logement>();
        if( delegation != null) {
            liste = logementMetier.getLogementsByDeleguation(delegation);

        }else {
            liste = logementMetier.getLogements() ;
        }

        if(liste.size()==0)
            return  Response.status(Status.NOT_FOUND).build();
        return Response.status(Status.OK)
                .header("Access-Control-Allow-Origin", "*")  // Autorise n'importe quelle origine (à des fins de test uniquement)
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .entity(liste)
                .build();
    }

}
