/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.*;
import DBcontroller.IterationDB;
import DBcontroller.TestCampaignDB;
import DBcontroller.TestExecution;
import controller.tablestep.HeaderTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import controller.tabtestcase.TabTestCaseNewController;
import controller.tabtestexecution.TabTestCampaignExecutionRepositoryBaselineController;
import controller.util.CommonFunctions;
import engine.Engine;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.TestCasesExecution;
import model.initColumn;
import org.apache.log4j.Logger;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.ClosedByInterruptException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author tmorin
 */
public class PopUpRunController implements Initializable {

    private final TestExecution testExecutionHandler = new TestExecution();
    private final initColumn CaseToExecute = new initColumn();
    private final SimpleIntegerProperty notExecuted = new SimpleIntegerProperty();
    private final SimpleIntegerProperty ok = new SimpleIntegerProperty();
    /**
     *
     */
    public TabTestCampaignExecutionRepositoryBaselineController executionMainViewController;
    @FXML
    PieChart pieChart;
    ArrayList<TestCase> testCasesToDisplay = new ArrayList();
    ObservableList<CaseExecutions> testCases = FXCollections.observableArrayList();
    ObservableList<Script> testScript;
    ObservableList<PieChart.Data> pieChartData;
    @FXML
    private AnchorPane anchorRunPopUp;
    @FXML
    private AnchorPane mainAnchorPopUp;
    @FXML
    private GridPane gridPanePopUpCase;
    @FXML
    private TableView<CaseExecutions> tableViewCampaignPopUpRun;
    @FXML
    private TitledPane tPane;
    @FXML
    private Label labelReferenceCampaignView;
    @FXML
    private Label labelNameCampaignView;
    @FXML
    private Label labelSystemCampaignView;
    @FXML
    private Label labelWriterCampaignView;
    @FXML
    private Label labelVersionCampaignView;
    @FXML
    private Label labelCreationDateCampaignView;
    @FXML
    private Label labelEditionDateCampaignView;
    @FXML
    private Label labelSUTReleaseCampaignView;
    @FXML
    private Label labelNumberOfCaseCampaignView;
    @FXML
    private Label labelRegressionThreadCampaignView;
    @FXML
    private Label labelWriterMailCampaignView;
    @FXML
    private Label labelDescriptionCampaignView;
    @FXML
    private Label labelCommentsCampaignView;
    @FXML
    private Label labelTimeCase;
    @FXML
    private Label labelTimeRemaining;
    @FXML
    private TextField jtextfieldReferenceCampaignView;
    @FXML
    private TextField jtextfieldNameCampaignView;
    @FXML
    private TextField jtextfieldSystemCampaignView;
    @FXML
    private TextField jtextfieldWriterCampaignView;
    @FXML
    private TextField jtextfieldVersionCampaignView;
    @FXML
    private TextField jtextfieldCreationDateCampaignView;
    @FXML
    private TextField jtextfieldEditionDateCampaignView;
    @FXML
    private TextField jtextfieldSUTReleaseCampaignView;
    @FXML
    private TextField jtextfieldNumberCasesCampaignView;
    @FXML
    private TextField jtextfieldWriterEmailCampaignView;
    @FXML
    private TextArea jtextareaCommentsCampaignView;
    @FXML
    private CheckBox CheckboxRegressionThreadCampaignView;
    @FXML
    private Button runButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button autoExecutionDisplay;
    private String baselineId;
    private ArrayList<CaseExecutions> caseExecutions;
    private CaseExecutions testCaseSelected;
    private TableStepScriptCreationController controllerTableStep;
    private boolean ready;

    private boolean suspended;

    private PopUpRunController thisController;

    private int iterationNumber;

    private Thread th;

    private boolean caseSelected = false;

    private CaseExecutions testCaseInExecution;

    private HeaderTableStepController controllerHeader;

    private Stage dialogStage;

    private Alert dlg;

    private Iterations iteration;

    private TestCasesExecution testCaseExecution = new TestCasesExecution();

    private Double iterationPercentage;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //this.mainAnchorPopUp.getStylesheets().add("/assets.view/CustomeStyle.css");
        thisController = this;
        ready = false;
        tableViewCampaignPopUpRun.setTableMenuButtonVisible(true);
        tableViewCampaignPopUpRun.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.constructTableStep();
        this.initPieChart();
        tableViewCampaignPopUpRun.setPlaceholder(new Label(""));
        CaseToExecute.initColumnCaseToExecute(tableViewCampaignPopUpRun);
        autoExecutionDisplay.setVisible(false);
        autoExecutionDisplay.setDisable(true);

        this.tableViewCampaignPopUpRun.setOnMousePressed((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                testCaseSelected = tableViewCampaignPopUpRun.getSelectionModel().getSelectedItem();
                if (testCaseSelected != null) {
                    caseSelected = true;
                    autoExecutionDisplay.setDisable(false);
                    try {
                        DisplaySteps(testCaseSelected);
                    } catch (InterruptedException ex) {
                        CommonFunctions.debugLog.error("", ex);
                    }
                    if (testScript != null) {
                        testScript.clear();
                    } else {
                        event.consume();
                    }
                }
            }
        });

        tPane.setAnimated(false);
        tPane.expandedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            Platform.runLater(() -> {
                        if (!tPane.isExpanded()) {
                            tPane.getChildrenUnmodifiable().get(0).setVisible(false);
                            gridPanePopUpCase.getRowConstraints().get(0).setMaxHeight(30);
                            gridPanePopUpCase.getRowConstraints().get(0).setPrefHeight(30);
                            gridPanePopUpCase.getRowConstraints().get(0).setMinHeight(30);
                        } else {
                            tPane.getChildrenUnmodifiable().get(0).setVisible(true);
                            gridPanePopUpCase.getRowConstraints().get(0).setMaxHeight(300);
                            gridPanePopUpCase.getRowConstraints().get(0).setPrefHeight(300);
                            gridPanePopUpCase.getRowConstraints().get(0).setMinHeight(300);
                        }
                    }
            );
        });

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                Engine engine = new Engine(caseExecutions, thisController, baselineId, iterationNumber);
                try {
                    engine.run();
                } catch (InterruptedException | ClosedByInterruptException ex) {
                    System.out.println("Break the current session.");
                    CommonFunctions.debugLog.error("Stopping Baseline Execution.");
                } catch (Exception ex) {
                    //Default Logger Event
                    CommonFunctions.debugLog.error("", ex);
                    //Can be specified to different types of exception. 
                    String exceptionMessage = ex.getMessage();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error: ");
                        alert.setHeaderText("Exception Caught. Cannot execute the test case / User interrupted the session.");
                        alert.setContentText("Please refer to the log for adjusting configurations.");
                        if (ex.getMessage() != null && ex.getMessage().contains("SSH")) {      //Customized Content for Exceptions from SSHCommand Script
                            alert.setTitle("Error: The server (IP: " + exceptionMessage.substring(exceptionMessage.indexOf("IP: ")).trim() + " ) cannot be reached.");
                            CommonFunctions.debugLog.error("\"The server (IP: \" + exceptionMessage.replace(\"//n\", \" \").replace(\"    \", \" \").substring(exceptionMessage.indexOf(\"IP: \"), exceptionMessage.indexOf(\"at\")).trim() + \" ) cannot be reached.", ex);
                        }
                        alert.showAndWait();
                    });
                }
                return null;
            }
        };

        th = new Thread(task);
        this.initButtons();
        this.loadCss();
    }

    /**
     * @param controllerBaselineCampaign
     */
    public void init(TabTestCampaignExecutionRepositoryBaselineController controllerBaselineCampaign) {
        executionMainViewController = controllerBaselineCampaign;
    }

    /**
     * @param stage
     */
    public void setPrimaryStage(Stage stage) {
        this.dialogStage = stage;
        stage.setOnCloseRequest((WindowEvent event) -> {
            if (th.getState() != Thread.State.NEW && th.getState() != Thread.State.TERMINATED) {
                event.consume();
            } else if (th.getState() == Thread.State.NEW) {
                long tempsDebut = System.currentTimeMillis();
                try {
                    testExecutionHandler.deleteIteration(this.iteration);
                } catch(org.hibernate.service.UnknownServiceException ex) {
                    CommonFunctions.debugLog.error("Invalid Request. Caused by Invalid operation on thread.", ex);
                    return;
                } catch(org.hibernate.HibernateException ex) {
                    CommonFunctions.debugLog.error("Invalid Request.", ex);
                    return;
                }
                long tempsFin = System.currentTimeMillis();
                float seconds = (tempsFin - tempsDebut) / 1000F;
                try {
                    long tempsDebut2 = System.currentTimeMillis();
                    Update();
                    long tempsFin2 = System.currentTimeMillis();
                    float seconds2 = (tempsFin2 - tempsDebut2) / 1000F;
                } catch (ParseException ex) {
                    CommonFunctions.debugLog.error("", ex);
                }
            }
        });
    }

    /**
     * @param baselineName
     * @param repositoryConroller
     */
    public void setParameters(String baselineName, TabTestCampaignExecutionRepositoryBaselineController repositoryConroller) {
        Platform.runLater(this::alertBox2);
        executionMainViewController = repositoryConroller;
        this.baselineId = baselineName;
        //TestCasesExecution testCaseExecution = new TestCasesExecution();
        long tempsDebu2t = System.currentTimeMillis();
        this.iteration = testExecutionHandler.prepareIteration(baselineName);
        this.iterationNumber = this.iteration.getIterationNumber();
        long tempsFin2 = System.currentTimeMillis();
        long tempsDebut = System.currentTimeMillis();
        long tempsDebut3 = System.currentTimeMillis();
        //System.out.println("");
        caseExecutions = testCaseExecution.PrepareCaseDisplayResults(this.baselineId, this.iterationNumber);    //Causing Exception with No Message.
        long tempsFin3 = System.currentTimeMillis();
        float seconds3 = (tempsFin3 - tempsDebut3) / 1000F;
        //System.out.println("Case display= " + Float.toString(seconds3));

        //testCasesAndSteps = testCaseExecution.PrepareTestDiplay(this.campaignID, this.baselineId, this.iterationNumber);
        //System.out.println("TEST CASE AND STEPS= " + caseExecutions.size());
        testCases = FXCollections.observableArrayList(caseExecutions);
        tableViewCampaignPopUpRun.setItems(testCases);
        pieChartData.get(0).setPieValue(caseExecutions.size());
        Platform.runLater(() -> {
                    pieChartData.get(0).setName("not tested\t" + caseExecutions.size());
                    pieChartData.get(7).setName("Total\t\t" + caseExecutions.size());
                    testCaseInExecution = caseExecutions.get(0);
                    try {
                        //automaticCasesDisplay(testCaseInExecution);
                        DisplaySteps(testCaseInExecution);
                    } catch (InterruptedException ex) {
                        CommonFunctions.debugLog.error("", ex);
                    }
                    tPane.setText("Information of baseline : " + baselineId + ",  iteration number " + iterationNumber);
                }
        );
        long tempsFin = System.currentTimeMillis();
        float seconds = (tempsFin - tempsDebut) / 1000F;
        //System.out.println("EFFECTUE EN= " + Float.toString(seconds));
        float seconds2 = (tempsFin2 - tempsDebu2t) / 1000F;
        //System.out.println("2 EFFECTUE EN= " + Float.toString(seconds2));
        constructCampaignInformation(this.iteration.getTestCampaign().getIdtestCampaign());
        ready = true;
        this.runButton.setDisable(false);
        Platform.runLater(this::closeAlert);
    }

    /**
     * @param caseExecution
     * @throws InterruptedException
     */
    public void DisplaySteps(CaseExecutions caseExecution) throws InterruptedException {
        TestCasesExecution testCaseExecution = new TestCasesExecution();
        long tempsDebut3 = System.currentTimeMillis();
        testCaseExecution.PrepareStepsScriptsParametersDisplayResults(caseExecution, iteration);
        //System.out.println("number of steps= " + testCaseInExecution.getStepExecutionses().size());
        long tempsFin3 = System.currentTimeMillis();
        float seconds3 = (tempsFin3 - tempsDebut3) / 1000F;
        //System.out.println("Step preparation= " + Float.toString(seconds3));
        //System.out.println("STEP NUMBER = " + caseExecution.getStepExecutionses().iterator());
        long tempsDebut = System.currentTimeMillis();
        controllerTableStep.displayScriptAndStepExecution(caseExecution);
        long tempsFin = System.currentTimeMillis();
        float seconds = (tempsFin - tempsDebut) / 1000F;
        //System.out.println("AFFICHAGE EFFECTUE EN= " + Float.toString(seconds));

    }

    private void constructTableStep() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPanePopUpCase.add((AnchorPane) fxmlLoader.load(getClass().getResource("/assets/view/stepcreation/tableStepScriptCreation.fxml").openStream()), 1, 4, 3, 3);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/assets.view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerTableStep = fxmlLoader.getController();

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/assets/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPanePopUpCase.add(paneTest, 1, 3, 3, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/assets.view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerTableStep = fxmlLoader.getController();
        controllerHeader = fxmlLoader2.getController();
        controllerHeader.init(controllerTableStep);
        controllerTableStep.setControllerHeader(controllerHeader);
        controllerHeader.setRestults();
    }

    /**
     * @return
     */
    public TabTestCampaignExecutionRepositoryBaselineController getController() {
        return executionMainViewController;
    }

    /**
     * @param nbCaseOK
     * @param nbCaseOKWC
     * @param nbCaseNOK
     * @param nbCaseNtestable
     * @param nbCaseIncomplete
     * @param nbCaseOS
     * @param nbCaseNT
     */
    public void setNumberNotExecuted(int nbCaseOK, int nbCaseOKWC, int nbCaseNOK, int nbCaseNtestable, int nbCaseIncomplete, int nbCaseOS, int nbCaseNT) {
        //Pie Chart UI & Statistics Related.
        pieChartData.get(0).setPieValue(nbCaseNT);
        pieChartData.get(1).setPieValue(nbCaseOK);
        pieChartData.get(2).setPieValue(nbCaseOKWC);
        pieChartData.get(3).setPieValue(nbCaseNOK);
        pieChartData.get(4).setPieValue(nbCaseNtestable);
        pieChartData.get(5).setPieValue(nbCaseIncomplete);
        pieChartData.get(6).setPieValue(nbCaseOS);
        Platform.runLater(() -> {
                    pieChartData.get(0).setName("Not tested\t" + nbCaseNT);
                    pieChartData.get(1).setName("OK\t\t\t" + nbCaseOK);
                    pieChartData.get(2).setName("OKWC\t\t" + nbCaseOKWC);
                    pieChartData.get(3).setName("NOK\t\t\t" + nbCaseNOK);
                    pieChartData.get(4).setName("Not testable\t" + nbCaseNtestable);
                    pieChartData.get(5).setName("Incomplete\t" + nbCaseIncomplete);
                    pieChartData.get(6).setName("OS\t\t\t" + nbCaseOS);
                }
        );
    }

    /**
     * @param averageTimeCase
     * @param timeRemaining
     */
    public void updateAverageTimeCase(float averageTimeCase, float timeRemaining) {
        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        DecimalFormat df = new DecimalFormat("#######.00");

        int seconds = (int) (timeRemaining % SECONDS_IN_A_MINUTE);
        int totalMinutes = (int) (timeRemaining / SECONDS_IN_A_MINUTE);
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;

        Platform.runLater(() -> {
                    labelTimeCase.setText("Average time case: " + df.format(averageTimeCase) + " sec");
                    labelTimeRemaining.setText("Time remaining: " + hours + " h " + minutes + " min " + seconds + " sec");
                }
        );
    }

    /**
     * @param currentTestCaseExecution
     */
    public void callDisplaySteps(CaseExecutions currentTestCaseExecution) {
        testCaseSelected = currentTestCaseExecution;
        testCaseInExecution = currentTestCaseExecution;
        if (!caseSelected) {
            Platform.runLater(() -> {
                        try {
                            automaticCasesDisplay(currentTestCaseExecution);
                            //tableViewCampaignPopUpRun.scrollTo(testCase);
                        } catch (InterruptedException ex) {
                            CommonFunctions.debugLog.error("", ex);
                        }
                    }
            );
        } else {
        }
    }

    /**
     * @param currentTestCaseExecution
     * @throws InterruptedException
     */
    public void automaticCasesDisplay(CaseExecutions currentTestCaseExecution) throws InterruptedException {
        tableViewCampaignPopUpRun.getSelectionModel().select(currentTestCaseExecution);
        DisplaySteps(currentTestCaseExecution);
    }

    private void loadCss() {
        this.mainAnchorPopUp.getStylesheets().add("/assets/view/popup/tablestep.css");
    }

    /**
     *
     */
    public void initPieChart() {
        pieChartData = FXCollections.observableArrayList(new PieChart.Data("Not tested", 0), new PieChart.Data("OK", 0), new PieChart.Data("OKWC", 0), new PieChart.Data("NOK", 0), new PieChart.Data("Not Testable", 0), new PieChart.Data("Incomplete", 0), new PieChart.Data("OS", 0), new PieChart.Data("Total", 0));
        pieChart.setData(pieChartData);
        pieChart.setLegendVisible(true);
    }

    /**
     *
     */
    public void initButtons() {
        runButton.setDisable(true);
        pauseButton.setDisable(true);
        stopButton.setDisable(true);

        runButton.setOnAction((ActionEvent e) -> {
            // Control the button behavior. 
            if (ready && th.getState() == Thread.State.NEW) {
                //playSound("go");
                th.start();
                suspended = false;
                runButton.setDisable(true);
                pauseButton.setDisable(false);
//                stopButton.setDisable(false);
            } else if (suspended) {
                th.resume();
                suspended = false;
                runButton.setDisable(true);
                pauseButton.setDisable(false);
//                stopButton.setDisable(false);
            } else {
                e.consume();
            }

        });

        autoExecutionDisplay.setVisible(false); //Button for showing autoexecution result. Unsure the function of it (Jump to first step). 
        autoExecutionDisplay.setOnAction((ActionEvent e) -> {
            autoExecutionDisplay.setDisable(true);
            caseSelected = false;
            try {
                DisplaySteps(testCaseInExecution);
            } catch (InterruptedException ex) {
                CommonFunctions.debugLog.error("", ex);
            }
            tableViewCampaignPopUpRun.getSelectionModel().select(testCaseInExecution);
        });

        pauseButton.setOnAction((ActionEvent e) -> {
            if (!suspended) {
                th.suspend();
                suspended = true;
                runButton.setDisable(false);
                pauseButton.setDisable(true);
            } else {
                e.consume();
            }
        });

        stopButton.setOnAction((ActionEvent e) -> {
            //The Stop button is not working at this stage. Only UI responds, but the algorithm will keep running till encountering exceptions / Finished.
            try {
                //Need to delete one record in Iterations database. Provide iteration_number and baseline_id
//                th.suspend();
//                th.resume();
                th.interrupt();
                this.executionInterrupted();
                //Change the state of testCaseInExecution to "Not tested."
//                this.executionFinished();
                //Need to stop the currentThread now.
//              Thread.currentThread().interrupt();
            } catch (Exception ex) {
                CommonFunctions.debugLog.error("ExecutionInterrupted Is having problem! ", ex);
            }
        });
    }

    /**
     * @throws Exception
     */
    public void executionInterrupted() throws Exception {
        IterationDB iterationHandler = new IterationDB();
        iterationHandler.deleteExecution(iteration);        //Causing exception
        Update();
        stopButton.setDisable(true);
        pauseButton.setDisable(true);
        runButton.setDisable(true);
        autoExecutionDisplay.setDisable(true);
    }

    /**
     * @throws Exception
     */
    public void executionFinished() throws Exception {
        IterationDB iterationHandler = new IterationDB();
        iterationHandler.setIterationResult(iteration, iterationPercentage);
        Update();
        stopButton.setDisable(true);
        pauseButton.setDisable(true);
        runButton.setDisable(true);
    }

    /**
     * @throws ParseException
     */
    public void Update() throws ParseException {
        executionMainViewController.updateRepository();
    }

    /**
     * @param campaignID
     */
    public void constructCampaignInformation(int campaignID) {
        TestCampaignDB testCampaignHandler = new TestCampaignDB();
        TestCampaign testCampaign = testCampaignHandler.getTestCampaignFromID(campaignID);
        this.jtextfieldReferenceCampaignView.setText(String.valueOf(testCampaign.getReference()));
        this.jtextfieldSystemCampaignView.setText(testCampaign.getSystem());
        this.jtextfieldWriterCampaignView.setText(testCampaign.getWritter());
        this.jtextfieldVersionCampaignView.setText(String.valueOf(testCampaign.getCampaignVersion()));
        this.jtextfieldCreationDateCampaignView.setText(testCampaign.getCreationDate());
        this.jtextfieldEditionDateCampaignView.setText(testCampaign.getEditionDate());
        this.jtextfieldSUTReleaseCampaignView.setText(testCampaign.getSoftwareSutRelease());
        this.jtextfieldNumberCasesCampaignView.setText(String.valueOf(testCampaign.getNumberTestCase()));
        this.CheckboxRegressionThreadCampaignView.setSelected(testCampaign.getRegressionThread() != 0);
        this.CheckboxRegressionThreadCampaignView.setDisable(true);
        this.jtextfieldWriterEmailCampaignView.setText(testCampaign.getWriterEmail());
        this.jtextareaCommentsCampaignView.setText(testCampaign.getComments());
    }

    /**
     *
     */
    public void alertBox2() {
        dlg = new Alert(AlertType.INFORMATION);
        dlg.setTitle("Please wait");
        dlg.getDialogPane().setContentText("Please wait until the end of your test preparation...");
        dlg.getButtonTypes().add(ButtonType.CANCEL);
        dlg.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent t) {
                if (ready) {
                    dlg.close();
                } else {
                    System.out.println("CLOSE");
                    t.consume();
                }
            }

        });

        dlg.show();
//        dlg.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - dlg.getWidth() / 2);
//        dlg.setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2 - dlg.getHeight() / 2);
        dlg.getDialogPane().lookupButton(dlg.getButtonTypes().get(0)).setVisible(false);
        dlg.getButtonTypes().remove(ButtonType.CANCEL);

    }

    /**
     *
     */
    public void closeAlert() {
        try {
//            alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
//                @Override
//                public void handle(DialogEvent t) {
//                    alert.close();
//                }
//            });
            dlg.close();
        } catch (Exception e) {
            System.out.println("Exception popUpRun Controller = " + e);
        }
    }

    /**
     * @param type
     */
    public void playSound(String type) {
        File finish = new File("src/assets.sounds/Finish.wav");
        File go = new File("src/assets.sounds/Go.wav");
        try {
            File toplay;
            if (type.equals("finish")) {
                toplay = finish;
            } else {
                toplay = go;
            }
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(toplay);
            clip.open(ais);
            clip.loop(0);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }

    }

    /**
     * @param iterationPercentage
     */
    public void setIterationPercentage(Double iterationPercentage) {
        this.iterationPercentage = iterationPercentage;
    }

}
