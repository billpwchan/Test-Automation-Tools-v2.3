package DB;
// Generated Jun 19, 2015 5:06:37 PM by Hibernate Tools 4.3.1



/**
 * TestCampaignTestCase generated by hbm2java
 */
public class TestCampaignTestCase  implements java.io.Serializable {


     private TestCampaignTestCaseId id;
     private TestCampaign testCampaign;
     private TestCase testCase;

    /**
     *
     */
    public TestCampaignTestCase() {
    }

    /**
     *
     * @param id
     * @param testCampaign
     * @param testCase
     */
    public TestCampaignTestCase(TestCampaignTestCaseId id, TestCampaign testCampaign, TestCase testCase) {
       this.id = id;
       this.testCampaign = testCampaign;
       this.testCase = testCase;
    }
   
    /**
     *
     * @return
     */
    public TestCampaignTestCaseId getId() {
        return this.id;
    }
    
    /**
     *
     * @param id
     */
    public void setId(TestCampaignTestCaseId id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public TestCampaign getTestCampaign() {
        return this.testCampaign;
    }
    
    /**
     *
     * @param testCampaign
     */
    public void setTestCampaign(TestCampaign testCampaign) {
        this.testCampaign = testCampaign;
    }

    /**
     *
     * @return
     */
    public TestCase getTestCase() {
        return this.testCase;
    }
    
    /**
     *
     * @param testCase
     */
    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }




}

