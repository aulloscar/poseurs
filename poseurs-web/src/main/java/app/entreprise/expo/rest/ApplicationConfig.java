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

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * ApplicationConfig. Servlet hébergeant l'exposition des Ressources REST
 *
 * @author Cédric Teyssié  <cedric.teyssie@irit.fr>, IRIT-SIERA, Université Paul Sabatier
 * @version 0.1, 11 oct. 2019
 * @since 0.1, 11 oct. 2019
 */
// miagebourse-web
// fr.miage.toulouse.m2.eai.miagebourse.expo.rest
// ApplicationConfig.java
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically populated with all resources defined in the project. If required,
     * comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        //resources.add(app.entreprise.expo.rest.AffaireResource.class);
        //resources.add(app.entreprise.expo.rest.MenuiserieResource.class);
    }

}
