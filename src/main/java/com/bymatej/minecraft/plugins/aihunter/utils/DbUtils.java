package com.bymatej.minecraft.plugins.aihunter.utils;

import com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterData;
import com.bymatej.minecraft.plugins.aihunter.entities.hunter.Hunter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

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
//        return new Configuration().configure("hibernate.cfg.xml")
        return new Configuration().configure()
                .addAnnotatedClass(Hunter.class)
                .addPackage("com.bymatej.minecraft.plugins.aihunter")
                .buildSessionFactory();
    }


}
