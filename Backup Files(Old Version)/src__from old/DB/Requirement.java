package DB;
// Generated Jun 19, 2015 5:06:37 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Requirement generated by hbm2java
 */
public class Requirement implements java.io.Serializable {

    private Integer idrequirement;
    private Byte version;
    private String requirementID;
    private String category;
    private String writer;
    private String requirementText;
    private Short coverage;
    private String comment;
    private String iadt;
    private Set testSteps = new HashSet(0);

    public Requirement() {
    }

    public Requirement(String idRequirement, String category, String writer, String requirementText, Short coverage, String comment, Set testSteps, String iadt) {
        this.requirementID = idRequirement;
        this.category = category;
        this.writer = writer;
        this.requirementText = requirementText;
        this.coverage = coverage;
        this.comment = comment;
        this.testSteps = testSteps;
        this.iadt = iadt;
    }

    public Requirement(Requirement req) {
        this.category = req.getCategory();
        this.writer = req.getWriter();
        this.requirementText = req.getRequirementText();
        this.coverage = req.getCoverage();
        this.comment = req.getComment();
        this.testSteps = req.getTestSteps();
        this.iadt = req.getIadt();
    }

    public Integer getIdrequirement() {
        return this.idrequirement;
    }

    public void setIdrequirement(Integer idrequirement) {
        this.idrequirement = idrequirement;
    }

    public Byte getVersion() {
        return this.version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRequirementID() {
        return this.requirementID;
    }

    public void setRequirementID(String name) {
        this.requirementID = name;
    }

    public String getWriter() {
        return this.writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getRequirementText() {
        return this.requirementText;
    }

    public void setRequirementText(String requirementText) {
        this.requirementText = requirementText;
    }

    public Short getCoverage() {
        return this.coverage;
    }

    public void setCoverage(Short coverage) {
        this.coverage = coverage;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set getTestSteps() {
        return this.testSteps;
    }

    public void setTestSteps(Set testSteps) {
        this.testSteps = testSteps;
    }

    /**
     * @return the iadt
     */
    public String getIadt() {
        return iadt;
    }

    /**
     * @param iadt the iadt to set
     */
    public void setIadt(String iadt) {
        this.iadt = iadt;
    }

}