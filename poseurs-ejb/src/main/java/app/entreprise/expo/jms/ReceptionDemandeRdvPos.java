/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.entreprise.expo.jms;

import app.entreprise.menuiserieshared.entities.Affaire;
import app.entreprise.menuiserieshared.entities.RdvCommercial;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author Josselin & Elise :D
 */
public class ReceptionDemandeRdvPos {
       
    public ReceptionDemandeRdvPos() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage om = (ObjectMessage) message;
                Affaire affaire = (Affaire) om.getObject();
                System.out.println("Reçu dans le service commercial: " + affaire.toString());
                
                // Creation d'un rdv commercial
                System.out.println("Choix automatique d'une date de rdv : 02/12/2021 à 10h - Commercial 1");
                RdvCommercial rdvCommercial = new RdvCommercial();
                rdvCommercial.setIdRdv(1);
                rdvCommercial.setDateRdv("02/12/2021");
                rdvCommercial.setHeureRdv("10h");
                rdvCommercial.setIdCommercial(1);
                affaire.setRdvCommercial(rdvCommercial);
                
                // TODO Enregistrement BD
                
                // Envoi de la confirmation du rdv
                EnvoiConfirmationRdvPos envoiRdvPos = new EnvoiConfirmationRdvPos();
                envoiRdvPos.send(affaire,"ConfirmationRdvFile");
                
            } else if (message != null) {
                System.out.println("Received non object message");
            } else {
                System.out.println("???");
            }
        } catch(Exception ex){
            System.out.println("Erreur lors de la réception : ");
        }
    }
    
}