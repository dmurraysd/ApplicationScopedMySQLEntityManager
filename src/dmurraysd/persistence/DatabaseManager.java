/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dmurraysd.persistence;

import dmurraysd.databaseEntities.User;
import dmurraysd.enums.LoginStatus;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;
import javax.swing.JOptionPane;

/**
 *
 * @author David_killa
 */


public class DatabaseManager 
{
    @PersistenceUnit
    private EntityManagerFactory factory;
    private EntityManager entityManager;
    private User m_user;
    
    /**
     * Initializes entity manager factory to create entity managers to a persistence unit 
     */
    public DatabaseManager() {
        factory = Persistence.createEntityManagerFactory("MySQLDatabaseAppPU");
    }

    /**
     *
     * @param username
     * @param password
     * @return number denoting results from search in entity table Login
     */
    public LoginStatus vailidate(String username, String password) {
        this.search(username);
        LoginStatus temp = LoginStatus.SUCCESS;
        if(this.m_user == null)
            temp = LoginStatus.USERNAME_FAILURE;
        else if(!this.m_user.getPassword().equals(password))
            temp = LoginStatus.PASSWORD_FAILURE;
        return temp;
    }

    /**
     *
     * @param name
     */
    public void search(String name) {
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            this.m_user = entityManager.find(User.class, name);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            String error = "Check log";
            if(ex instanceof PersistenceException)error = "Database Connectivity";
            JOptionPane.showMessageDialog(null, "Contact administrator - system error - " + error);
            System.exit(0);
        } finally {
            entityManager.close();
        }
    }

    /**
     *
     */
    public void close() {
        
        if(factory.isOpen())factory.close();
    }
}
