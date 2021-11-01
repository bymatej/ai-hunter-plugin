package com.bymatej.minecraft.plugins.aihunter.utils;

import com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterData;
import com.bymatej.minecraft.plugins.aihunter.entities.hunter.Hunter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterConverter.entityToData;
import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static java.util.logging.Level.SEVERE;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class DbUtils {

    public static void createHunter(HunterData hunterData) {
        System.out.println("1");
        Transaction tx = null;
        try (Session session = getFactory().openSession()) {
            System.out.println("2");
            tx = session.beginTransaction();
            System.out.println("3");
            Hunter hunter = entityToData(hunterData);
            System.out.println("4");
            session.save(hunter);
            System.out.println("5");
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
        try (Session session = getFactory().openSession()) {
            tx = session.beginTransaction();
            Hunter hunter = entityToData(hunterData);
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
        try (Session session = getFactory().openSession()) {
            tx = session.beginTransaction();
            Hunter hunter = entityToData(hunterData);
            session.update(hunter);
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
        try (Session session = getFactory().openSession()) {
            tx = session.beginTransaction();
            String hql = String.format("FROM hunter h WHERE h.name = '%s'", hunterData.getName());
            Query query = session.createQuery(hql);
            List results = query.list();
            if (isNotEmpty(results)) {
                hunter = (Hunter) results.get(0);
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


    private static SessionFactory getFactory() {
        return getSessionFactory();
//        return new Configuration().configure()
//                .addProperties(getHibernateProperties())
//                .addAnnotatedClass(Hunter.class)
//                .addPackage("com.bymatej.minecraft.plugins.aihunter")
//                .buildSessionFactory();
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
     * That's why I opted for this approach so that I don't have to read the "hibernate.cfg.xml" file and umarshal it.
     * No XML, no issues. :)
     *
     * @return properties
     */
    private static Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("connection.url", "jdbc:h2:/home/matej/projects/spigot-build-tools/test-mc-server-1.16.5/db/hunter");
        properties.setProperty("connection.driver_class", "org.h2.Driver");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("current_session_context_class", "thread");
        properties.setProperty("hbm2ddl.auto", "update");

        return properties;
    }

    public static SessionFactory getSessionFactory() {
        SessionFactory sessionFactory = null;
        StandardServiceRegistry registry = null;
            try {

                // Create registry builder
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Map<String, String> settings = new HashMap<>();
                settings.put(Environment.DRIVER, "org.h2.Driver");
                settings.put(Environment.URL, "jdbc:h2:/home/matej/projects/spigot-build-tools/test-mc-server-1.16.5/db/hunter");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");

                // Apply settings
                registryBuilder.applySettings(settings);

                // Create registry
                registry = registryBuilder.build();

                // Create MetadataSources
                MetadataSources sources = new MetadataSources(registry);

                // Create Metadata
                Metadata metadata = sources.getMetadataBuilder().build();

                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }

        return sessionFactory;
    }

}
