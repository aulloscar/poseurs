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

import app.entreprise.menuiserieshared.exceptions.AffaireExistanteException;
import app.entreprise.services.ServicesAffaire;
import app.entreprise.services.ServicesAffaireLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * Ressource REST de la bourse. Le fragment d'URL exposé est /bourse.
 *
 *
 * @author Cédric Teyssié  <cedric.teyssie@irit.fr>, IRIT-SIERA, Université Paul Sabatier
 * @version 1.1, 11 oct. 2019
 * @since 0.1, 3 oct. 2016
 */
@Path("affaires")
@RequestScoped
public class MenuiserieResource {

    /**
     * EJB de l'exposition des services de la bourse.
     */
    @EJB(beanName="ServicesAffaire")
    private ServicesAffaireLocal servicesAffaire = lookupServicesAffaireLocal();

    /**
     * Contexte Web de l'application. Permet de connaitre si besoin son URI.
     */
    @Context
    private UriInfo context;

    /**
     * Constructeur d'une ressource Bourse.
     */
    public MenuiserieResource() {
    }

    /**
     * Retourne la liste en JSON des mnenomiques des titres contenus en base.
     *
     * @return Retourne 200 + Liste JSON des mnemoniques si la base n'est pas vide. 404 sinon.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        try {
            return Response.ok(this.servicesAffaire.getListeAffaires()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Ajoute un titre boursier à la liste des titres contenus en base.
     *
     * @param content titre boursier au format JSON à ajouter
     *
     * @return Retourne 200 + Liste JSON des mnemoniques si la base n'est pas vide. 404 sinon.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putJson(String content) {
        try {

            return Response.ok(this.servicesAffaire.ajouterAffaire(content)).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Méthode de recherche JNDI de l'EJB exposant les services de la bourse.
     *
     * @return EJB exposant le service s'il est trouvée. Null sinon
     */
    private ServicesAffaireLocal lookupServicesAffaireLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            //TODO changer ce qu'il y a dans le lookup
            return (ServicesAffaireLocal) c.lookup("java:global/ServiceSca-ear/ServiceSca-ejb-1.0-SNAPSHOT/ServicesAffaire!app.entreprise.services.ServicesAffaireLocal");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
