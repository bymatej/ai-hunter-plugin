package com.bymatej.minecraft.plugins.aihunter.utils;

import com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterData;
import com.bymatej.minecraft.plugins.aihunter.entities.hunter.Hunter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import static com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterConverter.dataToEntity;
import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static java.nio.file.Files.deleteIfExists;
import static java.util.logging.Level.SEVERE;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.hibernate.cfg.Environment.*;

public class DbUtils {

    public static void createHunter(HunterData hunterData) {
        Transaction tx = null;
        try (Session session = getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Hunter hunter = dataToEntity(hunterData);
            session.save(hunter);
            tx.commit();
            log("Hunter stored successfully.");
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }

            log(SEVERE, "Error saving hunter in DB", e);
        }
    }

    public static void deleteHunter(HunterData hunterData) {
        Transaction tx = null;
        try (Session session = getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Hunter hunter = dataToEntity(hunterData);
            session.remove(hunter);
            tx.commit();
            log("Hunter deleted successfully.");
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }

            log(SEVERE, "Error removing hunter from DB", e);
        }
    }

    public static void updateHunterCoordinates(HunterData hunterData) {
        Transaction tx = null;
        try (Session session = getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Hunter hunterToUpdate = getHunterByName(hunterData);
            Hunter updatedHunter = dataToEntity(hunterData);
            hunterToUpdate.setDeathLocationX(updatedHunter.getDeathLocationX());
            hunterToUpdate.setDeathLocationY(updatedHunter.getDeathLocationY());
            hunterToUpdate.setDeathLocationZ(updatedHunter.getDeathLocationZ());
            session.update(hunterToUpdate);
            tx.commit();
            log("Hunter coordinates updated successfully.");
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }

            log(SEVERE, "Error updating hunter coordinates in DB", e);
        }
    }

    public static Hunter getHunterByName(HunterData hunterData) {
        Hunter hunter = null;
        Transaction tx = null;
        try (Session session = getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Hunter> criteria = builder.createQuery(Hunter.class);
            Root<Hunter> root = criteria.from(Hunter.class);
            criteria.select(root).where(builder.equal(root.get("name"), hunterData.getName()));
            Query<Hunter> query = session.createQuery(criteria);
            query.setFirstResult(0);
            query.setMaxResults(1);
            List<Hunter> results = query.getResultList();

            if (isNotEmpty(results)) {
                hunter = results.get(0);
            }
            tx.commit();
            log("Hunter retrieved by name successfully.");
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }

            log(SEVERE, "Error updating hunter coordinates in DB", e);
        }

        return hunter;
    }

    /**
     * There should be only one Hunter. This gets the first element from the hunters table.
     */
    public static Hunter getHunter() {
        Hunter hunter = null;
        Transaction tx = null;
        try (Session session = getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Hunter> criteria = builder.createQuery(Hunter.class);
            Root<Hunter> root = criteria.from(Hunter.class);
            criteria.select(root);
            Query<Hunter> query = session.createQuery(criteria);
            query.setFirstResult(0);
            query.setMaxResults(1);
            List<Hunter> results = query.getResultList();

            if (isNotEmpty(results)) {
                hunter = results.get(0);
            }
            tx.commit();
            log("Hunter retrieved successfully.");
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }

            log(SEVERE, "Error updating hunter coordinates in DB", e);
        }

        return hunter;
    }

    public static void killDb() {
        dropTableHunter();
        deleteDbFile();
    }

    private static void dropTableHunter() {
        Transaction tx = null;
        try (Session session = getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery("DROP TABLE Hunter").executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }

            log(SEVERE, "Error updating hunter coordinates in DB", e);
        }
    }

    /**
     * This won't be needed for in-memory db. This will be deleted!
     */
    @Deprecated
    private static void deleteDbFile() {
        // Mac path
        Path path1 = FileSystems.getDefault().getPath("/Users/matej/projects/private/minecraft/spigot-server-1.16.5/db/hunter.mv.db");
        Path path2 = FileSystems.getDefault().getPath("/Users/matej/projects/private/minecraft/spigot-server-1.16.5/db/hunter.trace.db");
        // Linux path
        Path path3 = FileSystems.getDefault().getPath("/home/matej/projects/spigot-build-tools/test-mc-server-1.16.5/db/hunter.mv.db");
        Path path4 = FileSystems.getDefault().getPath("/home/matej/projects/spigot-build-tools/test-mc-server-1.16.5/db/hunter.trace.db");
        try {
            deleteIfExists(path1);
            deleteIfExists(path2);
            deleteIfExists(path3);
            deleteIfExists(path4);
        } catch (IOException e) {
            log(SEVERE, "Error deleting DB file", e);
        }
    }

    /**
     * This is sort of a hack because JAX-B would not get loaded into classpath inside Plugin.
     * <p>
     * I've tried to set the following gradle dependencies:
     * implementation 'jakarta.xml.bind:jakarta.xml.bind-api:3.0.1'
     * implementation 'com.sun.xml.bind:jaxb-impl:3.0.1'
     * I had to build the fat jar (using the buildJar job defined in build.gradle) to get the actual exception.
     * I even created a Main class with a main method, and I ran the plugin as a "console application". I did not get
     * the exception running it this way. I also did not get the exception when I created another application with the
     * same dependencies and configuration for Hibernate.
     * The only time I get the exception was when getFactory is called from within the Minecraft server that has this
     * plugin loaded.
     * <p>
     * That's why I opted for this approach so that I don't have to read the "hibernate.cfg.xml" file and unmarshal it.
     * No XML, no issues. :)
     *
     * @return properties
     */
    private static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(Hunter.class)
                .setProperty(DIALECT, H2Dialect.class.getName())
                .setProperty(DRIVER, org.h2.Driver.class.getName())
                .setProperty(URL, "jdbc:h2:/home/matej/projects/spigot-build-tools/test-mc-server-1.16.5/db/hunter;DB_CLOSE_ON_EXIT=FALSE;FILE_LOCK=NO")
//                                        .setProperty(URL, "jdbc:h2:/Users/matej/projects/private/minecraft/spigot-server-1.16.5/db/hunter;DB_CLOSE_ON_EXIT=FALSE;FILE_LOCK=NO")
                .setProperty(USER, "sa")
                .setProperty(PASS, "")
                .setProperty(CURRENT_SESSION_CONTEXT_CLASS, "thread")
                .setProperty(HBM2DDL_AUTO, "update");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

}
