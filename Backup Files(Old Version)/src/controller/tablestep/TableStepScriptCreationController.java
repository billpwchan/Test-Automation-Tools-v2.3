/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tablestep;

import DB.*;
import DBcontroller.RequirementDB;
import DBcontroller.ScriptDB;
import controller.popup.PopUPRequirementSelectionController;
import controller.tabtestcase.TabTestCaseEditController;
import controller.tabtestcase.TabTestCaseNewController;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Main;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TableStepScriptCreationController implements Initializable {

    private static ArrayList<Requirement> allReq = new ArrayList<>();
    /**
     *
     */
    private static int excelRank;
    private final String defaultColor = " #CCFFCC";
    private final ObservableList<Node> workingCollection = FXCollections.observableArrayList();
    private final List<Node> tampCollection = new ArrayList<>();
    private final ObservableList<StepLineTableStepController> collectionControllerStep = FXCollections.observableArrayList();
    private final HashSet<Script> scriptStimuli = new HashSet<>(0);
    private final HashSet<Script> scriptCheck = new HashSet<>(0);
    @FXML
    private AnchorPane mainAnchorPan;
    @FXML
    private ScrollPane scrollPan;
    @FXML
    private VBox vBox;
    @FXML
    private GridPane gridPanInAnchor;
    @FXML
    private Label labelReadyVerif;
    @FXML
    private Label labelReadyAction;
    private StepLineTableStepController controllerStepLine;
    private ScriptLineTableStepController controllerScriptLine;
    private int stepID = 1;
    private StepLineTableStepController selectedTestStepController;
    private TabTestCaseNewController controllerNewCase;
    private TabTestCaseEditController controllerEditCase;
    private Stage dialogStage;
    private boolean popUpOpen = false;
    private HeaderTableStepController controllerHeader;
    private TestCase globalTestCase;

    /**
     * @return
     */
    public static int getExcelRank() {
        return excelRank;
    }

    /**
     * @param rank
     */
    public static void setExcelRank(int rank) {
        excelRank = rank;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //workingCollection.addAll(vBox.getChildren());
        this.scrollPan.widthProperty()
                .addListener((ObservableValue<? extends Number> arg0, Number arg1, Number arg2) -> {
                            double newValue = arg2.doubleValue() - 15;
                            vBox.setPrefWidth(newValue);
                            updateAllChildren(newValue);
                        }
                );
    }

    /**
     * @return
     */
    public HeaderTableStepController getControllerHeader() {
        return this.controllerHeader;
    }

    /**
     * @param headerController
     */
    public void setControllerHeader(HeaderTableStepController headerController) {
        this.controllerHeader = headerController;
    }

    /*
     Bind each script and step AnchorPane to the width of the scrollPane.
     */
    private void updateAllChildren(double value) {
        for (int i = 0; i < vBox.getChildren().size(); i++) {
            ((AnchorPane) vBox.getChildren().get(i)).setPrefWidth(value);
        }
    }

    /**
     * Update the current selected step (color blue) and put it in the variable
     * selected step.
     *
     * @param selectedStep
     */
    public void updateCurrentStep(StepLineTableStepController selectedStep) {
        if (this.selectedTestStepController != null) {
            this.selectedTestStepController.hideArea();
        }

        if (selectedStep != null) {
            this.selectedTestStepController = selectedStep;
            this.selectedTestStepController.showArea();
            if (controllerNewCase != null) {
                controllerNewCase.setStateButtonScript(false);
            }
            if (controllerEditCase != null) {
                controllerEditCase.setStateButtonScript(false);
            }
        }
    }

    /**
     * Get the current selected step (color blue) and put it in the variable
     * selected step.
     */
    public StepLineTableStepController getCurrentStep() {
        return this.selectedTestStepController;
    }

    /**
     * Add a script to the current selected step.
     *
     * @see selectedTestStepController
     */
    public void addScriptToSelectStep() {
        if (this.selectedTestStepController != null) {
            addScriptToStep(this.selectedTestStepController);
            controllerScriptLine.setScriptCreation(scriptStimuli, scriptCheck);
        }

    }

    /**
     * Add a script to the step given in parameters. Need to update the Vbox
     * (working collection) and the object controllerStepLine as well
     *
     * @param controllerStepLine step to add the script to.
     * @see controllerStepLine.addScript
     */
    public void addScriptToStep(StepLineTableStepController controllerStepLine) {

        if (controllerStepLine != null) {
            int VboxIDSelectedStep = workingCollection.indexOf(controllerStepLine.getAnchorPane()) + controllerStepLine.getNumberOdScript();
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane scriptPane = new AnchorPane();
            try {
                try (InputStream scriptPaneStream = getClass().getResource("/assets/view/stepcreation/scriptLineTableStep.fxml").openStream()) {
                    scriptPane = fxmlLoader.load(scriptPaneStream);
                    controllerScriptLine = (ScriptLineTableStepController) fxmlLoader.getController();
                }
                scriptPane.setPrefWidth(vBox.getPrefWidth());
            } catch (IOException ex) {
                Logger.getLogger(TableStepScriptCreationController.class
                        .getName()).error("", ex);
            }
            controllerStepLine.addScript(controllerScriptLine, 1);
            if (workingCollection.isEmpty()) {   //To prevent IndexOutOfBoundException
                workingCollection.add(scriptPane);
            } else {
                workingCollection.add(VboxIDSelectedStep + 1, scriptPane);
            }
            displayVbox();

        }
    }

    /**
     * Method to move the step and its child(script) in one way or another
     * (depending on the value of the boolean).
     *
     * @param indexToMove          index in the vbox of the test step to move
     * @param wayToMove            boolean if true go up else go down.
     * @param numberOfObjectToMove Number of child (Script) to move.
     */
    public void moveStep(StepLineTableStepController indexToMove, boolean wayToMove, int numberOfObjectToMove) {
        int indexOfStep = workingCollection.indexOf(indexToMove.getAnchorPane());
        if (wayToMove) {
            if (collectionControllerStep.indexOf(indexToMove) - 1 >= 0) {
                int indexControllerSelected = collectionControllerStep.indexOf(indexToMove);
                StepLineTableStepController targetedController = collectionControllerStep.get(collectionControllerStep.indexOf(indexToMove) - 1);
                int indexOfTargetedStep = workingCollection.indexOf(targetedController.getAnchorPane());
                Collections.swap(collectionControllerStep, indexControllerSelected, indexControllerSelected - 1);

                for (int i = indexOfStep; i < (indexOfStep + indexToMove.numberOfScript) + 1; i++) {
                    tampCollection.add(workingCollection.get(i));
                }
                workingCollection.remove(indexOfStep, indexOfStep + indexToMove.numberOfScript + 1);
                workingCollection.addAll(indexOfTargetedStep, tampCollection);
                tampCollection.clear();
                updateStepId(0);
                vBox.getChildren().setAll(workingCollection);
            }
        } else {
            if (collectionControllerStep.indexOf(indexToMove) + 1 < collectionControllerStep.size()) {
                StepLineTableStepController targetedController = collectionControllerStep.get(collectionControllerStep.indexOf(indexToMove) + 1);
                int indexControllerSelected = collectionControllerStep.indexOf(indexToMove);    //Selected step(step only) index (0-based)

                int indexOfTargetedStep = workingCollection.indexOf(targetedController.getAnchorPane());    //Selected Step(Step & script) Index (0-based)

                Collections.swap(collectionControllerStep, indexControllerSelected, indexControllerSelected + 1);
                for (int i = indexOfStep; i < (indexOfStep + indexToMove.numberOfScript) + 1; i++) {
                    tampCollection.add(workingCollection.get(i));
                }

                workingCollection.remove(indexOfStep, indexOfStep + indexToMove.numberOfScript + 1);
                //Problematic statement. Need to consider the case when moving a 2 steps with 1 script to the end of the test case
                //AddAll functions is using 1 based. If add to index 1, then it will be after the first element
                //Potential Solution: Add default script at first.
                workingCollection.addAll(indexOfTargetedStep + targetedController.numberOfScript - tampCollection.size() + 1, tampCollection);  //Will cause IndexOutOfBoundException
                tampCollection.clear();
                updateStepId(0);
                vBox.getChildren().setAll(workingCollection);
            }

        }
    }

    /**
     * Update the id of each test step starting from the Id given in parameters
     *
     * @param startingID position of the test steps to start to modify the id
     *                   from.
     */
    private void updateStepId(int startingID) {
        for (int i = startingID; i < collectionControllerStep.size(); i++) {
            collectionControllerStep.get(i).setID(i + 1);
        }
    }

    /**
     * Method to add a test step in the vBox given the object test step.
     *
     * @param step
     */
    public void addStepinVbox(TestStep step) {

        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane stepPane;
            try (InputStream streamViewStep = getClass().getResource("/assets/view/stepcreation/stepLineTableStep.fxml").openStream()) {
                stepPane = fxmlLoader.load(streamViewStep);
                controllerStepLine = (StepLineTableStepController) fxmlLoader.getController();
            }
            controllerStepLine.initControllerTable(this);
            controllerStepLine.constructInformation(stepID);

            //Always add to end. Adding in the middle will cause exception (Due to AddAll function)
            collectionControllerStep.add(controllerStepLine);
            controllerStepLine.setStepView(step);
            workingCollection.add(stepPane);

            stepPane.setPrefWidth(vBox.getPrefWidth());

        } catch (IOException ex) {
            Logger.getLogger(TableStepScriptCreationController.class
                    .getName()).error("", ex);
        }
        stepID++;
        displayVbox();
    }

    /**
     * Display the Vbox.
     */
    private void displayVbox() {
        vBox.getChildren().setAll(workingCollection);
    }

    /**
     * Swap the script given in parameters in the vBox, depending on the value
     * of the boolean b
     *
     * @param b     way to swap, if true go up, else go down.
     * @param aThis script to swap in the vbox.
     */
    void swapScript(boolean b, ScriptLineTableStepController aThis) {
        int indexScriptInVbox = workingCollection.indexOf(aThis.getPane());
        if (b) {
            Collections.swap(workingCollection, indexScriptInVbox, indexScriptInVbox - 1);
        } else {
            Collections.swap(workingCollection, indexScriptInVbox, indexScriptInVbox + 1);
        }
        vBox.getChildren().setAll(workingCollection);
    }

    /**
     * @param width
     * @param height
     */
    public void setAnchorSize(double width, double height) {
        this.mainAnchorPan.setPrefSize(width, height);
    }

    /**
     * Delete the selected test step and the corresponding collection of script.
     * Update the id and the vBox also
     *
     * @return
     */
    public boolean deleteSelectedStep() {
        boolean hasBeenDeleted = false;
        if (this.selectedTestStepController != null) {
            this.selectedTestStepController.collectionControllerScript.stream().forEach((collectionControllerScript) -> {
                deleteStep(collectionControllerScript);
            });
            workingCollection.remove(this.selectedTestStepController.getAnchorPane());
            this.collectionControllerStep.remove(this.selectedTestStepController);
            this.selectedTestStepController = null;
            stepID--;
            updateStepId(0);
            displayVbox();
            hasBeenDeleted = true;
        }
        if (controllerNewCase != null) {
            controllerNewCase.setStateButtonScript(true);
        }
        if (controllerEditCase != null) {
            controllerEditCase.setStateButtonScript(true);
        }

        return hasBeenDeleted;
    }

    /**
     * Delete the step given in parameters
     *
     * @param aThis step to delete
     * @see deleteSelectedStep();
     */
    public void deleteSelectedStep(StepLineTableStepController aThis) {
        if (aThis != null) {
            aThis.collectionControllerScript.stream().forEach((collectionControllerScript) -> {
                deleteStep(collectionControllerScript);
            });
            workingCollection.remove(aThis.getAnchorPane());
            this.collectionControllerStep.remove(aThis);
            this.selectedTestStepController = null;
            stepID--;
            updateStepId(0);
            displayVbox();
            if (controllerNewCase != null) {
                controllerNewCase.setStateButtonScript(true);
            }
            if (controllerEditCase != null) {
                controllerEditCase.setStateButtonScript(true);
            }
        }
    }

    /**
     * Delete the script from the assets.view given in parameters and update the table.
     *
     * @param aThis script to delete.
     */
    public void deleteScript(ScriptLineTableStepController aThis) {
        workingCollection.remove(aThis.getPane());
        displayVbox();

    }

    /**
     * Delete the script given in prameters from the assets.view.
     *
     * @param scriptController
     */
    public void deleteStep(ScriptLineTableStepController scriptController) {
        if (scriptController != null) {
            workingCollection.remove(scriptController.getPane());
            //scriptController = null;
        }
    }

    /**
     * Return the collection of step of this vBox/table test step.
     *
     * @return collectionControllerStep.
     */
    public ObservableList<StepLineTableStepController> getCollectionTestStep() {
        return this.collectionControllerStep;
    }

    /**
     * Clear the model of the table, but does not display it.
     */
    public void clearTable() {
        this.workingCollection.clear();
        this.collectionControllerStep.clear();
        this.stepID = 1;
    }

    /**
     * @param testCase
     */
    public void displayScriptAndStepView(TestCase testCase) {
        globalTestCase = testCase;
        clearTable();
        Iterator<TestStep> itSteps = testCase.getTestSteps().iterator();
        while (itSteps.hasNext()) {
            TestStep testStep = itSteps.next();
            this.addStepinVbox(testStep);
            controllerStepLine.setStepExpandView(testStep);
            ArrayList<TestStepHasScript> gogolito = new ArrayList<>(testStep.getTestStepHasScripts());
            int numberOfScript = gogolito.size();

            for (int j = 0; j < numberOfScript; j++) {
                this.addScriptToStep(this.controllerStepLine);
                TestStepHasScript currentTSHS = gogolito.get(j);
                if (j == numberOfScript - 1) {
                    if (currentTSHS.getScript().getIsStimuli() != 0) {
                        this.controllerScriptLine.setScriptandParamAction(currentTSHS);
                    } else {
                        this.controllerScriptLine.setScriptandParamVerif(currentTSHS);
                    }
                } else {
                    TestStepHasScript nextTSHS = gogolito.get(j + 1);
                    if (currentTSHS.getScript().getIsStimuli() != 0 && nextTSHS.getScript().getIsStimuli() == 0) {
                        this.controllerScriptLine.setScriptandParamAction(currentTSHS);
                        this.controllerScriptLine.setScriptandParamVerif(nextTSHS);
                        j++;
                    } else if (currentTSHS.getScript().getIsStimuli() != 0 && nextTSHS.getScript().getIsStimuli() != 0) {
                        this.controllerScriptLine.setScriptandParamAction(currentTSHS);
                    } else {
                        this.controllerScriptLine.setScriptandParamVerif(currentTSHS);
                    }
                }
            }
            finishDisplayStep();
        }
        displayVbox();
    }

    /**
     * @param testCase
     */
    public void displayScriptAndStepEdit(TestCase testCase) {
        globalTestCase = testCase;
        clearTable();
        Iterator<TestStep> itSteps = testCase.getTestSteps().iterator();

        while (itSteps.hasNext()) {
            TestStep testStep = itSteps.next();
            this.addStepinVbox(testStep);
            //controllerStepLine.setStepView(testStep);
            controllerStepLine.setStepCreation(allReq);
            ArrayList<TestStepHasScript> gogolito = new ArrayList<>(testStep.getTestStepHasScripts());
            int numberOfScript = gogolito.size();

            for (int j = 0; j < numberOfScript; j++) {
                this.addScriptToStep(this.controllerStepLine);
                this.controllerScriptLine.setScriptCreation(scriptStimuli, scriptCheck);
                TestStepHasScript currentTSHS = gogolito.get(j);
                if (j == numberOfScript - 1) {
                    if (currentTSHS.getScript().getIsStimuli() != 0) {
                        this.controllerScriptLine.setScriptandParamActionEdit(currentTSHS);
                    } else {
                        this.controllerScriptLine.setScriptandParamVerifEdit(currentTSHS);
                    }
                } else {
                    TestStepHasScript nextTSHS = gogolito.get(j + 1);
                    if (currentTSHS.getScript().getIsStimuli() != 0 && nextTSHS.getScript().getIsStimuli() == 0) {
                        this.controllerScriptLine.setScriptandParamActionEdit(currentTSHS);
                        this.controllerScriptLine.setScriptandParamVerifEdit(nextTSHS);
                        j++;
                    } else if (currentTSHS.getScript().getIsStimuli() != 0 && nextTSHS.getScript().getIsStimuli() != 0) {
                        this.controllerScriptLine.setScriptandParamActionEdit(currentTSHS);
                    } else {
                        this.controllerScriptLine.setScriptandParamVerifEdit(currentTSHS);
                    }
                }
            }
            finishDisplayStep();
        }
        displayVbox();
    }

    /**
     * @param testCase
     */
    public void displayScriptAndStepBaseline(TestCase testCase) {
        globalTestCase = testCase;
        clearTable();
        Iterator<TestStep> itSteps = testCase.getTestSteps().iterator();

        while (itSteps.hasNext()) {
            TestStep testStep = itSteps.next();
            this.addStepinVbox(testStep);
            //controllerStepLine.setStepView(testStep);
            ArrayList<TestStepHasScript> gogolito = new ArrayList<>(testStep.getTestStepHasScripts());
            int numberOfScript = gogolito.size();

            for (int j = 0; j < numberOfScript; j++) {
                this.addScriptToStep(this.controllerStepLine);
                TestStepHasScript currentTSHS = gogolito.get(j);
                if (j == numberOfScript - 1) {
                    if (currentTSHS.getScript().getIsStimuli() != 0) {
                        this.controllerScriptLine.setScriptandParamActionBaseline(currentTSHS);
                    } else {
                        this.controllerScriptLine.setScriptandParamVerifBaseline(currentTSHS);
                    }
                } else {
                    TestStepHasScript nextTSHS = gogolito.get(j + 1);
                    if (currentTSHS.getScript().getIsStimuli() != 0 && nextTSHS.getScript().getIsStimuli() == 0) {
                        this.controllerScriptLine.setScriptandParamActionBaseline(currentTSHS);
                        this.controllerScriptLine.setScriptandParamVerifBaseline(nextTSHS);
                        j++;
                    } else if (currentTSHS.getScript().getIsStimuli() != 0 && nextTSHS.getScript().getIsStimuli() != 0) {
                        this.controllerScriptLine.setScriptandParamActionBaseline(currentTSHS);
                    } else {
                        this.controllerScriptLine.setScriptandParamVerifBaseline(currentTSHS);
                    }
                }
            }
            finishDisplayStep();

        }
        displayVbox();
    }

    /**
     * @param testCase
     */
    public void displayScriptAndStepExecution(CaseExecutions testCase) {
        clearTable();
        Iterator<StepExecutions> itSteps = testCase.getStepExecutionses().iterator();

        while (itSteps.hasNext()) {
            StepExecutions testStep = itSteps.next();
            this.addStepinVbox(testStep.getTestStep());
            //controllerStepLine.setStepView(testStep.getTestStep());
            controllerStepLine.executionInstanceView(testStep);
            ArrayList<ScriptExecutions> gogolito = new ArrayList<>(testStep.getScriptExecutionses());
            int numberOfScript = gogolito.size();

            for (int j = 0; j < numberOfScript; j++) {
                this.addScriptToStep(this.controllerStepLine);
                ScriptExecutions currentTSHS = gogolito.get(j);
                if (j == numberOfScript - 1) {
                    if (currentTSHS.getScript().getIsStimuli() != 0) {
                        this.controllerScriptLine.setScriptandParamActionExecuction(currentTSHS);
                    } else {
                        this.controllerScriptLine.setScriptandParamVerifExecuction(currentTSHS);
                    }
                } else {
                    ScriptExecutions nextTSHS = gogolito.get(j + 1);
                    if (currentTSHS.getScript().getIsStimuli() != 0 && nextTSHS.getScript().getIsStimuli() == 0) {
                        this.controllerScriptLine.setScriptandParamActionExecuction(currentTSHS);
                        this.controllerScriptLine.setScriptandParamVerifExecuction(nextTSHS);
                        j++;
                    } else if (currentTSHS.getScript().getIsStimuli() != 0 && nextTSHS.getScript().getIsStimuli() != 0) {
                        this.controllerScriptLine.setScriptandParamActionExecuction(currentTSHS);
                    } else {
                        this.controllerScriptLine.setScriptandParamVerifExecuction(currentTSHS);
                    }
                }
            }
            finishDisplayStep();

        }
        displayVbox();
    }

    /**
     * Method called when the user clicked on the image expand form the assets.view
     * test step
     *
     * @param aThis
     * @param expand
     * @see StepLineTableStepController
     */
    void expandChildren(StepLineTableStepController aThis, boolean expand) {
        int startIndex = workingCollection.indexOf(aThis.getAnchorPane()) + 1;

        int finalIndex = startIndex + collectionControllerStep.get(collectionControllerStep.indexOf(aThis)).getCollectionScript().size();
        if (expand) {
            for (int i = startIndex; i < finalIndex; i++) {
                workingCollection.add(i, aThis.getCollectionScript().get(i - startIndex).getPane());
            }
        } else {
            workingCollection.remove(startIndex, finalIndex);
        }
        displayVbox();
        updateAllChildren(vBox.getPrefWidth());
    }

    /**
     * Return the boolean to check if the table is fully configured or not.
     *
     * @return configured
     */
    public boolean isFullyConfigured() {
        boolean configured = true;
        int i = 0;
        while (configured && i < collectionControllerStep.size()) {
            ObservableList<ScriptLineTableStepController> collectionControllerScript1 = collectionControllerStep.get(i).getCollectionScript();
            int j = 0;
            int tailleMax = collectionControllerScript1.size();
            while (configured && j < tailleMax) {
                if (!collectionControllerScript1.get(j).isFullyConfigured()) {
                    configured = false;
                }
                j++;
            }
            i++;
        }
        return configured;
    }

    /**
     * Initialize the test step for configuration
     */
    public void configurationOnForStep() {
        controllerStepLine.hideArea();
        controllerStepLine.setStepCreation(allReq);
        Thread th = new Thread(controllerStepLine.task);
        th.start();
        //controllerStepLine.initStep();
    }

    /**
     * Init the reference of the object controllerNewCase with the reference of
     * the object aThis.
     *
     * @param aThis
     */
    public void initTestCaseNew(TabTestCaseNewController aThis) {
        controllerNewCase = aThis;

        this.loadScriptCheckAndStimuli();

        if (this.controllerNewCase != null) {
            this.controllerNewCase.getAnchorPane().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            collectionControllerStep.stream().map((collectionControllerStep1) -> {
                                if (!collectionControllerStep1.getIsClicked()) {
                                    collectionControllerStep1.hideArea();
                                }
                                return collectionControllerStep1;
                            }).forEachOrdered((collectionControllerStep1) -> {
                                collectionControllerStep1.setIsClicker(false);
                            });
                        }
                    }
            );
        }
    }

    /**
     * @param aThis
     */
    public void initTestCaseEdit(TabTestCaseEditController aThis) {
        controllerEditCase = aThis;

        this.loadScriptCheckAndStimuli();
        if (this.controllerEditCase != null) {
            this.controllerEditCase.getAnchorPane().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {

                            for (StepLineTableStepController collectionControllerStep1 : collectionControllerStep) {
                                if (!collectionControllerStep1.getIsClicked()) {
                                    collectionControllerStep1.hideArea();
                                }
                                collectionControllerStep1.setIsClicker(false);
                            }
                        }
                    }
            );
        }
    }

    /**
     * Disable the configuration of script when a baseline is finished.
     */
    public void disableConfiguration() {
        collectionControllerStep.stream().forEach((collectionControllerStep1) -> {
            collectionControllerStep1.disableScriptConfiguration();
        });
    }

    private void loadScriptCheckAndStimuli() {
        ScriptDB scriptDBHandler = new ScriptDB();
        HashSet<Script> allScript = scriptDBHandler.getScriptListWithParameters();
        allScript
                .stream()
                .sorted((object1, object2) -> object1.getName().compareTo(object2.getName()));
        Script emptyScript = new Script();
        emptyScript.setName("");
        scriptStimuli.add(emptyScript);
        scriptCheck.add(emptyScript);
        Iterator<Script> itScript = allScript.iterator();
        while (itScript.hasNext()) {
            Script script = itScript.next();
            if (script.getIsStimuli() != 0) {
                scriptStimuli.add(script);
            } else {
                scriptCheck.add(script);
            }
        }
        RequirementDB requirementHandler = new RequirementDB();
        allReq = requirementHandler.getAllRequirement();
    }

    /**
     * @param test
     */
    public void addRequirement(StepLineTableStepController test) {
        try {
            ObservableList<Requirement> CasesInDB = FXCollections.observableArrayList(allReq);
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane editPane = fxmlLoader.load(getClass().getResource("/assets/view/popup/popUPRequirementSelection.fxml").openStream());
            dialogStage = new Stage();
            dialogStage.setTitle("Case Selection");
            dialogStage.initOwner(Main.primaryStage);
            dialogStage.setAlwaysOnTop(true);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(editPane);
            dialogStage.setScene(scene);

            PopUPRequirementSelectionController controller = fxmlLoader.getController();
            controller.init(this, test);
            controller.setTable(CasesInDB, test.getRequirements());

            dialogStage.setOnCloseRequest((WindowEvent event) -> {
                this.closePopUp();
            });
            dialogStage.show();
            dialogStage.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - dialogStage.getWidth() / 2);
            dialogStage.setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2 - dialogStage.getHeight() / 2);
//            dialogStage.setX(main.getMainController().getPrimaryStage().getX() + main.getMainController().getPrimaryStage().getWidth() / 2 - dialogStage.getWidth() / 2);
//            dialogStage.setY(main.getMainController().getPrimaryStage().getY() + main.getMainController().getPrimaryStage().getHeight() / 2 - dialogStage.getHeight() / 2);
            popUpOpen = true;

        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class
                    .getName()).error("", ex);
        }
    }

    /**
     * @param requirementsSelected
     */
    public void setAction(ObservableList<Requirement> requirementsSelected) {

    }

    /**
     *
     */
    public void closePopUp() {

        if (popUpOpen == true) {
            this.dialogStage.close();
            popUpOpen = false;
        }

    }

    void updateRequirementTestCase() {
        controllerNewCase.updateListRequirement();
    }

    private void finishDisplayStep() {
        controllerStepLine.expandChildren();
        if (controllerStepLine.getNumberOdScript() == 0) {
            controllerStepLine.hideArrow();
        }
    }

    /**
     * @return
     */
    public TestCase getTestCase() {
        return this.globalTestCase;
    }

}
