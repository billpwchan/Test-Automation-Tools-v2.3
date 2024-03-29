///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package DBcontroller;
//

import DB.*;
import controller.util.CommonFunctions;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author tmartinez
 */
public class TestCampaignDB {

    public TestCampaignDB() {

    }

    /**
     *
     * @return
     */
    public ArrayList<TestCampaign> getAllCampaigns() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        Query qry = session.createQuery("from TestCampaign");
        List l = qry.list();
        ArrayList<TestCampaign> campaigns = (ArrayList) l;
        session.getTransaction().commit();
        session.close();
        return campaigns;
    }
//

    /**
     *
     * @param testCampaign
     * @param testCases
     * @return
     */
    public TestCampaign CreateCampaign(TestCampaign testCampaign, ArrayList<TestCase> testCases) {
        String reportLogMsg = "";
        TestCase testCaseToAdd = new TestCase();
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.save(testCampaign);
        reportLogMsg += "After Creating Test Campaign :" + Objects.requireNonNull(testCampaign).getReference() + System.lineSeparator();
        StringBuilder reportLogMsgBuilder = new StringBuilder(reportLogMsg);
        for (int i = 0; i < testCases.size(); i++) {
            TestCampaignTestCase tctc = new TestCampaignTestCase();
            TestCampaignTestCaseId tctcId = new TestCampaignTestCaseId();
            testCaseToAdd = (TestCase) session.get(TestCase.class, testCases.get(i).getIdtestCase());
            tctc.setTestCampaign(testCampaign);
            tctc.setTestCase(testCaseToAdd);
            tctcId.setCaseOrder((byte) i);
            tctcId.setTestCampaignIdtestCampaign(testCampaign.getIdtestCampaign());
            tctcId.setTestCaseIdtestCase(testCaseToAdd.getIdtestCase());
            tctc.setId(tctcId);
            testCampaign.getTestCampaignTestCases().add(tctc);
            session.save(testCampaign);
            reportLogMsgBuilder.append("\tCase ").append(i + 1).append(": ").append(testCases.get(i).getTestCaseTitle())
                    .append(" (").append(testCases.get(i).getIdtestCase()).append(')').append(System.lineSeparator());
        }
        session.beginTransaction().commit();
        session.close();
        CommonFunctions.reportLog.info(reportLogMsgBuilder.toString());
        CommonFunctions.reportLog.info("Successfully saved test campaign: " + Objects.requireNonNull(testCampaign).getReference());
        return testCampaign;
    }

    /**
     *
     * @return @throws ParseException
     */
    public ArrayList<Iterations> getBaselinedCampaignsTree() throws ParseException {
        ArrayList<Iterations> baselinedCampaigns = new ArrayList<>();
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("select IT from Iterations IT group by IT.testCampaign");
        List l = qry.list();
        ArrayList<Iterations> iterations = (ArrayList) l;
        for (Iterations iteration : iterations) {
            Criteria query = session.createCriteria(Iterations.class);
            query.add(Restrictions.eq("testCampaign.idtestCampaign", iteration.getTestCampaign().getIdtestCampaign()));
            query.setProjection(Projections.max("date"));
            String maxDate = (String) query.uniqueResult();
            iteration.setDate(maxDate);
            iteration.setType("campaign");
            Hibernate.initialize(iteration.getTestCampaign());
        }
        session.close();
        return iterations;
    }

    /**
     *
     * @param campaignID
     * @return
     */
    public TestCampaign getTestCampaignFromID(int campaignID) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        TestCampaign testCampaign = (TestCampaign) session.get(TestCampaign.class, campaignID);
        session.close();
        return testCampaign;
    }

    /**
     *
     * @param campaignName
     * @return
     */
    public long checkCampaignExistence(String campaignName) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query query = session.createQuery("select count(*) from TestCampaign as TC where TC.reference=:campaignName");
        query.setString("campaignName", campaignName);
        Long count = (Long) query.uniqueResult();
        session.close();
        return count;
    }

    /**
     *
     * @param campaign
     */
    public void deleteCampaignNotExecuted(TestCampaign campaign) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query query = session.createQuery("delete from TestCampaignTestCase TCTC where TCTC.id.testCampaignIdtestCampaign=:campaignID");
        query.setInteger("campaignID", campaign.getIdtestCampaign());
        query.executeUpdate();
        session.delete(campaign);
        session.beginTransaction().commit();
        session.close();
    }

    /**
     *
     * @param testCase
     * @return
     */
    public ArrayList<TestCampaign> getCampaignsFromCases(TestCase testCase) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        ArrayList<TestCampaign> TestCampaigns = new ArrayList<>();
        Query qry = session.createQuery("select tc from TestCampaign as tc left join tc.testCampaignTestCases as tctc where tctc.testCase.idtestCase=:idCase");
        qry.setParameter("idCase", testCase.getIdtestCase());
        TestCampaigns = (ArrayList) qry.list();
        session.close();
        return TestCampaigns;
    }

}
