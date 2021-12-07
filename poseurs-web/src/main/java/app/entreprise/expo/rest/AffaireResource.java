/**
 * Copyright © ${project.inceptionYear} MIAGE de Toulouse (cedric.teyssie@miage.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.entreprise.expo.rest;

import app.entreprise.menuiserieshared.entities.Affaire;
import app.entreprise.menuiserieshared.exceptions.AffaireInconnueException;
import app.entreprise.services.ServicesAffaire;
import app.entreprise.services.ServicesAffaireLocal;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * Ressource REST d'un titre boursier. L'URL est située immédiatement en dessous de la bourse : /bourse/mnenonique où mnenonique est la
 * mnenonique considérée.
 *
 * @see BourseResource
 *
 * @author Cédric Teyssié  <cedric.teyssie@irit.fr>, IRIT-SIERA, Université Paul Sabatier
 * @version 1.1, 11 oct. 2019
 * @since 0.1, 3 oct. 2016
 */
@Path("affaires/{id}")
@RequestScoped
public class AffaireResource {

    /**
     * EJB de l'exposition des services de la bourse.
     */
     @EJB(beanName="ServicesAffaire")
    ServicesAffaireLocal servicesAffaire = lookupServicesAffaireLocal();
    /**
     * Contexte Web de l'application. Permet de connaitre si besoin son URI.
     */
    @Context
    private UriInfo context;
    /**
     * Convertisseur JSON
     */
    private Gson gson;

    /**
     * Constructeur d'une ressource Titre.
     */
    public AffaireResource() {
        this.gson = new Gson();
    }

    /**
     * Récupère une ressourcfe titre et la retourne en JSON.
     *
     * @param id mnemonique de la ressource recherchée. Issu de l'URL.
     *
     * @return Code 200 + JSON de la ressource si elle est trouvée en base. Code 404 uniquement sinon.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson(@PathParam("id") String id) {
        try {
            return Response.ok(this.servicesAffaire.getAffaire(Integer.parseInt(id))).build();
        } catch (AffaireInconnueException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    /**
     * Met à jour le cours d'un titre boursier
     *
     * @param id      mnemonique de la ressource recherchée. Issu de l'URL.
     * @param content titre boursier au format JSON qui doit être mis à jour
     *
     * @return Retourne 200 + JSON du titre mis à jour.
     * <p>
     * 404 si le titre est absent de la base.<p>
     * 400 si le titre est mal formulé. Ex: mnemonique de l'URL différente de celle fournie dans l'objet du corps de la requete
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putJson(@PathParam("id") String id, String content) {
        // vérif id == "content.id" pour éviter un trou de sécu
        // où id serait /= du mnemo du titre dans le content
        Affaire affaire = this.gson.fromJson(content, Affaire.class);
        if (!id.equals(affaire.getIdAffaire())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            return Response.ok(this.servicesAffaire.majAffaire(content)).build();
        } catch (AffaireInconnueException ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Supprime un titre boursier. Cette méthode est idempotente.
     *
     * @param id mnemonique de la ressource à retirer. Issu de l'URL.
     *
     * @return Code 200 que le titre vienne d'être supprimmé ou qu'il ait déjà supprimé avant.
     */
    @DELETE
    public Response removeJson(@PathParam("id") String id) {
        System.out.println("delete");
        try {
            this.servicesAffaire.supprimerAffaire(Integer.parseInt(id));
        } catch (AffaireInconnueException ex) {
            Logger.getLogger(AffaireResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }

    /**
     * Méthode de recherche JNDI de l'EJB exposant les services de la bourse.
     *
     * @return EJB exposant le service s'il est trouvée. Null sinon
     */
    private ServicesAffaireLocal lookupServicesAffaireLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            // TODO changer lookup
            return (ServicesAffaireLocal) c.lookup("java:global/ServiceSca-ear/ServiceSca-ejb-1.0-SNAPSHOT/ServicesAffaire!app.entreprise.services.ServicesAffaireLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
