package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.jpa_test;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.AbstractBaseDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.AbstractBaseEntity;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.ProviderResourcesFilesPath;
import com.google.common.base.Stopwatch;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Created by rrequena on 21/07/16.
 */

public class MainRunner {

    public static void main(String[] args) {

        try {

            Stopwatch timer = Stopwatch.createStarted();
            PointDao pointDao = new PointDao();

            // Store 1000 Point objects in the database:
            for (int i = 0; i < 1000; i++) {
                Point p = new Point(i, i);
                pointDao.save(p);
            }

            System.out.println("Total Points: " + pointDao.count());

            // Find the average X value:
            EntityManager entityManager = pointDao.getConnection();
            Query q2 = pointDao.getConnection().createQuery("SELECT AVG(p.x) FROM Point p");
            System.out.println("Average X: " + q2.getSingleResult());

            // Retrieve all the Point objects from the database:
            List<Point> results = pointDao.list();
            for (Point p : results) {
                System.out.println(p);
            }


            System.out.println("Load entity whit id 500: " +pointDao.findById(new Long(500)));

            // Close the database connection:
            entityManager.close();
            DatabaseManager.closeDataBase();

            System.out.println("Method took: " + timer.stop());

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    static class PointDao<Point> extends AbstractBaseDao{

        /**
         * Constructor
         */
        public PointDao() {
            super(MainRunner.Point.class);
        }
    }


    @Entity
    static class Point extends AbstractBaseEntity<Long> implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue
        private Long id;

        private int x;
        private int y;

        public Point() {
        }

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Long getId() {
            return id;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return String.format("id = %d (%d, %d)", this.id, this.x, this.y);
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }

}
