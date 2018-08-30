package DB;
// Generated Jul 7, 2015 10:16:27 AM by Hibernate Tools 4.3.1

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Script generated by hbm2java
 */
public class Script implements java.io.Serializable {

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private int idScript;
    private String name;
    private String desciption;
    private Integer scriptVersion;
    private String creationDate;
    private String editionDate;
    private Byte isStimuli;
    private Byte isMacro;
    private Set scriptExecutionses = new HashSet(0);
    private Set macrosForScriptIdScript = new HashSet(0);
    private Set testStepHasScripts = new HashSet(0);
    private Set macrosForScriptIdScript1 = new HashSet(0);
    private Set scriptHasParameterses = new HashSet(0);
    private String callName;

    /**
     *
     */
    public Script() {
    }

    /**
     *
     * @param idScript
     */
    public Script(int idScript) {
        this.idScript = idScript;
    }

    /**
     *
     * @param script
     */
    public Script(Script script) {
        this.name = script.getName();
        this.desciption = script.getDesciption();
        this.scriptVersion = script.getScriptVersion();
        this.creationDate = script.getCreationDate();
        this.editionDate = script.getEditionDate();
        this.isStimuli = script.getIsStimuli();
        this.isMacro = script.getIsMacro();
        this.scriptExecutionses = script.getScriptExecutionses();
        this.macrosForScriptIdScript = script.getMacrosForScriptIdScript();
        this.macrosForScriptIdScript1 = script.getMacrosForScriptIdScript1();
        this.testStepHasScripts = script.getTestStepHasScripts();
        this.scriptHasParameterses = script.getScriptHasParameterses();
//        HashSet<Script> newSet = new HashSet<>();
//        for (Object sc :  script.getScriptExecutionses()) {
//            newSet.add((Script) sc);
//        }
//        this.scriptExecutionses = newSet;
//
//        HashSet<Macro> newSetMacro = new HashSet<>();
//        for (Object sc : script.getMacrosForScriptIdScript()) {
//            newSetMacro.add((Macro) sc);
//        }
//        this.macrosForScriptIdScript = newSetMacro;
//
//        newSet = new HashSet<>();
//        for (Object sc : script.getTestStepHasScripts()) {
//            newSet.add((Script) sc);
//        }
//        this.testStepHasScripts = newSet;
//
//        newSet = new HashSet<>();
//        for (Object sc : script.getMacrosForScriptIdScript1()) {
//            newSet.add((Script) sc);
//        }
//        this.macrosForScriptIdScript1 = newSet;
//
//        newSet = new HashSet<>();
//        for (Object sc : script.getScriptHasParameterses()) {
//            newSet.add((Script) sc);
//        }
//        this.scriptHasParameterses = newSet;
    }

    /**
     *
     * @param idScript
     * @param name
     * @param desciption
     * @param scriptVersion
     * @param creationDate
     * @param editionDate
     * @param isStimuli
     * @param isMacro
     * @param scriptExecutionses
     * @param macrosForScriptIdScript
     * @param testStepHasScripts
     * @param macrosForScriptIdScript1
     * @param scriptHasParameterses
     * @throws ParseException
     */
    public Script(int idScript, String name, String desciption, Integer scriptVersion, String creationDate, String editionDate, Byte isStimuli, Byte isMacro, Set scriptExecutionses, Set macrosForScriptIdScript, Set testStepHasScripts, Set macrosForScriptIdScript1, Set scriptHasParameterses) throws ParseException {
        this.idScript = idScript;
        this.name = name;
        this.desciption = desciption;
        this.scriptVersion = scriptVersion;
        this.creationDate = creationDate;
        this.editionDate = editionDate;
        this.isStimuli = isStimuli;
        this.isMacro = isMacro;
        this.scriptExecutionses = scriptExecutionses;
        this.macrosForScriptIdScript = macrosForScriptIdScript;
        this.testStepHasScripts = testStepHasScripts;
        this.macrosForScriptIdScript1 = macrosForScriptIdScript1;
        this.scriptHasParameterses = scriptHasParameterses;
    }

    /**
     *
     * @return
     */
    public int getIdScript() {
        return this.idScript;
    }

    /**
     *
     * @param idScript
     */
    public void setIdScript(int idScript) {
        this.idScript = idScript;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getDesciption() {
        return this.desciption;
    }

    /**
     *
     * @param desciption
     */
    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    /**
     *
     * @return
     */
    public Integer getScriptVersion() {
        return this.scriptVersion;
    }

    /**
     *
     * @param scriptVersion
     */
    public void setScriptVersion(Integer scriptVersion) {
        this.scriptVersion = scriptVersion;
    }

    /**
     *
     * @return
     */
    public String getCreationDate() {
        return this.creationDate;
    }

    /**
     *
     * @param creationDate
     * @throws ParseException
     */
    public void setCreationDate(String creationDate) throws ParseException {
        this.creationDate = creationDate;
    }

    /**
     *
     * @return
     */
    public String getEditionDate() {
        return this.editionDate;
    }

    /**
     *
     * @param editionDate
     * @throws ParseException
     */
    public void setEditionDate(String editionDate) throws ParseException {
        if (editionDate != null) {
            this.editionDate = editionDate;
        } else {
            this.editionDate = null;
        }
    }

    /**
     *
     * @return
     */
    public Byte getIsStimuli() {
        return this.isStimuli;
    }

    /**
     *
     * @param isStimuli
     */
    public void setIsStimuli(Byte isStimuli) {
        this.isStimuli = isStimuli;
    }

    /**
     *
     * @return
     */
    public Byte getIsMacro() {
        return this.isMacro;
    }

    /**
     *
     * @param isMacro
     */
    public void setIsMacro(Byte isMacro) {
        this.isMacro = isMacro;
    }

    /**
     *
     * @return
     */
    public Set getScriptExecutionses() {
        return this.scriptExecutionses;
    }

    /**
     *
     * @param scriptExecutionses
     */
    public void setScriptExecutionses(Set scriptExecutionses) {
        this.scriptExecutionses = scriptExecutionses;
    }

    /**
     *
     * @return
     */
    public Set getMacrosForScriptIdScript() {
        return this.macrosForScriptIdScript;
    }

    /**
     *
     * @param macrosForScriptIdScript
     */
    public void setMacrosForScriptIdScript(Set macrosForScriptIdScript) {
        this.macrosForScriptIdScript = macrosForScriptIdScript;
    }

    /**
     *
     * @return
     */
    public Set getTestStepHasScripts() {
        return this.testStepHasScripts;
    }

    /**
     *
     * @param testStepHasScripts
     */
    public void setTestStepHasScripts(Set testStepHasScripts) {
        this.testStepHasScripts = testStepHasScripts;
    }

    /**
     *
     * @return
     */
    public Set getMacrosForScriptIdScript1() {
        return this.macrosForScriptIdScript1;
    }

    /**
     *
     * @param macrosForScriptIdScript1
     */
    public void setMacrosForScriptIdScript1(Set macrosForScriptIdScript1) {
        this.macrosForScriptIdScript1 = macrosForScriptIdScript1;
    }

    /**
     *
     * @return
     */
    public Set getScriptHasParameterses() {
        return this.scriptHasParameterses;
    }

    /**
     *
     * @param scriptHasParameterses
     */
    public void setScriptHasParameterses(Set scriptHasParameterses) {
        this.scriptHasParameterses = scriptHasParameterses;
    }

    /**
     * @return the callName
     */
    public String getCallName() {
        return callName;
    }

    /**
     * @param callName the callName to set
     */
    public void setCallName(String callName) {
        this.callName = callName;
    }

}