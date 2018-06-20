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

    public Script() {
    }

    public Script(int idScript) {
        this.idScript = idScript;
    }

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

    public int getIdScript() {
        return this.idScript;
    }

    public void setIdScript(int idScript) {
        this.idScript = idScript;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesciption() {
        return this.desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Integer getScriptVersion() {
        return this.scriptVersion;
    }

    public void setScriptVersion(Integer scriptVersion) {
        this.scriptVersion = scriptVersion;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(String creationDate) throws ParseException {
        this.creationDate = creationDate;
    }

    public String getEditionDate() {
        return this.editionDate;
    }

    public void setEditionDate(String editionDate) throws ParseException {
        if (editionDate != null) {
            this.editionDate = editionDate;
        } else {
            this.editionDate = null;
        }
    }

    public Byte getIsStimuli() {
        return this.isStimuli;
    }

    public void setIsStimuli(Byte isStimuli) {
        this.isStimuli = isStimuli;
    }

    public Byte getIsMacro() {
        return this.isMacro;
    }

    public void setIsMacro(Byte isMacro) {
        this.isMacro = isMacro;
    }

    public Set getScriptExecutionses() {
        return this.scriptExecutionses;
    }

    public void setScriptExecutionses(Set scriptExecutionses) {
        this.scriptExecutionses = scriptExecutionses;
    }

    public Set getMacrosForScriptIdScript() {
        return this.macrosForScriptIdScript;
    }

    public void setMacrosForScriptIdScript(Set macrosForScriptIdScript) {
        this.macrosForScriptIdScript = macrosForScriptIdScript;
    }

    public Set getTestStepHasScripts() {
        return this.testStepHasScripts;
    }

    public void setTestStepHasScripts(Set testStepHasScripts) {
        this.testStepHasScripts = testStepHasScripts;
    }

    public Set getMacrosForScriptIdScript1() {
        return this.macrosForScriptIdScript1;
    }

    public void setMacrosForScriptIdScript1(Set macrosForScriptIdScript1) {
        this.macrosForScriptIdScript1 = macrosForScriptIdScript1;
    }

    public Set getScriptHasParameterses() {
        return this.scriptHasParameterses;
    }

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
