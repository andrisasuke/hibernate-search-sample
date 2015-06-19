package com.srccodes.example.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

/**
 * Test App
 */
public class App {

    private static void doIndex() throws InterruptedException {
        Session session = HibernateUtil.getSession();

        FullTextSession fullTextSession = Search.getFullTextSession(session);
        fullTextSession.createIndexer().startAndWait();

        fullTextSession.close();
    }

    private static List<Contact> search(String queryString) {

        Session session = HibernateUtil.getSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Contact.class).get();

        org.apache.lucene.search.Query luceneQuery = queryBuilder
                .keyword().onFields("name").matching(queryString).createQuery();
        //.must(qb.keyword().onField("category").matching(category).createQuery()).createQuery();

        // wrap Lucene query in a javax.persistence.Query
        org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Contact.class);

        List<Contact> contactList = fullTextQuery.list();

        fullTextSession.close();

        return contactList;
    }

    private static void displayContactTableData() {
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            // Fetching saved data
            List<Contact> contactList = session.createQuery("from Contact").list();

            for (Contact contact : contactList) {
                System.out.println(contact);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n\n******Data stored in Contact table******\n");
        displayContactTableData();

        // Create an initial Lucene index for the data already present in the database
        doIndex();

        String searchText = "paolo coelho";
        List<Contact> result = search(searchText);
        System.out.println("\n\n>>>>>>Record found for '" + searchText + "' count : " + result.size());

        for (Contact contact : result) {
            System.out.println(contact);
        }

        System.out.println("===============================================");


        String searchText1 = "mr coelho paolo is his name";
        List<Contact> result1 = search(searchText1);
        System.out.println("\n\n>>>>>>Record found for '" + searchText1 + "' count : " + result1.size());

        for (Contact contact : result) {
            System.out.println(contact);
        }

        System.out.println("===============================================");

        String searchText2 = "who is mister coelho paolo ?";
        List<Contact> result2 = search(searchText2);
        System.out.println("\n\n>>>>>>Record found for '" + searchText2 + "' count : " + result2.size());

        for (Contact contact : result) {
            System.out.println(contact);
        }

        System.out.println("===============================================");

        String searchText3 = "famous writer paolo coelho";
        List<Contact> result3 = search(searchText3);
        System.out.println("\n\n>>>>>>Record found for '" + searchText3 + "' count : " + result3.size());

        for (Contact contact : result) {
            System.out.println(contact);
        }

        System.out.println("===============================================");

    }
}
