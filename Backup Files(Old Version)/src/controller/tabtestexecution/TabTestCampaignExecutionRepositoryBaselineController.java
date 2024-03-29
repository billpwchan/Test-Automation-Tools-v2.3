/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestexecution;

import DB.Iterations;
import DB.TestCampaign;
import DBcontroller.IterationDB;
import DBcontroller.TestCampaignDB;
import DBcontroller.TestExecution;
import controller.popup.PopUpCampaignSelectionController;
import controller.popup.PopUpRunController;
import controller.tabtestcampaign.TabTestCampaignRepositoryController;
import controller.util.CommonFunctions;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import main.Main;
import model.*;
import org.hibernate.exception.GenericJDBCException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static main.Main.primaryStage;

/**
 * FXML Controller class To validate a particular test case (With prompt window
 * if excel is provided)
 *
 * @author Thomas M.
 */
public class TabTestCampaignExecutionRepositoryBaselineController implements Initializable {

    private static final TestCampaignDB testCampaignHandler = new TestCampaignDB();
    /**
     *
     */
    public static TabTestCampaignExecutionMainViewController main;
    @FXML
    private static TreeItem<Iterations> root;
    private static TreeItem<Iterations> root2;
    private static TreeItem<Iterations> root0;
    private static TreeItem<Iterations> root3;
    private final IterationDB iteHandler = new IterationDB();
    private final TestExecution testExecutionHandler = new TestExecution();
    /**
     *
     */
    public int notExecuted;
    @FXML
    private AnchorPane anchorPaneTestExecution;
    @FXML
    private Button baselineButton;
    @FXML
    private TreeTableView<Iterations> campaignBaselineAndExecution;
    @FXML
    private Button runButton;
    @FXML
    private Button buttonReport;
    @FXML
    private Button buttonDelete;
    private Stage popUpCampaignStage;
    private Stage runStage;
    private PopUpRunController runController;
    private Iterations selected;
    private boolean popUpBaselineOpen = false;

    /**
     * Create the tree of campaigns,configurations and executions with up to
     * date informations
     *
     * @throws ParseException
     */
    public static void UpdateTreeItem() throws ParseException {
        TestExecution iterationHandler = new TestExecution();
        ArrayList<Iterations> baselines;
        ArrayList<Iterations> executions;
        ArrayList<Iterations> baselineCampaigns;
        baselineCampaigns = testCampaignHandler.getBaselinedCampaignsTree();
        root.getChildren().clear();
        for (Iterations baselineCampaign : baselineCampaigns) {
            root0 = new TreeItem<>(baselineCampaign);
            baselines = iterationHandler.getBaselinesFromCampaign(baselineCampaign.getTestCampaign());
            for (Iterations baseline : baselines) {
                root2 = new TreeItem<>(baseline);
                executions = iterationHandler.getExecutionsFromBaseline(baseline);
                for (Iterations execution : executions) {
                    if (execution.getIterationNumber() != 0) {
                        root3 = new TreeItem<>(execution);
                        root2.getChildren().add(root3);
                    }
                }
                root0.getChildren().add(root2);
            }
            root.getChildren().add(root0);
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {

        this.iniButtons();
        this.prepareTreeViewColumns();

        campaignBaselineAndExecution.setPlaceholder(new Label(""));

        Iterations iteration = new Iterations();
        root = new TreeItem<>(iteration);
        campaignBaselineAndExecution.setRoot(root);
        campaignBaselineAndExecution.setShowRoot(false);
        campaignBaselineAndExecution.setEditable(false);
        campaignBaselineAndExecution.sortPolicyProperty();
        campaignBaselineAndExecution.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        root.setExpanded(true);
        try {
            updateRepository();
        } catch (ParseException ex) {
            CommonFunctions.debugLog.error("", ex);
        }

        buttonDelete.setDisable(false);

        //Enable or Disable the button run regarding the line selected by the user
        this.campaignBaselineAndExecution.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Iterations>> observable, TreeItem<Iterations> oldValue, TreeItem<Iterations> newValue) -> {
            if (newValue != null) {
                selected = newValue.getValue();
                switch (selected.getType()) {
                    case "campaign":
                        runButton.setDisable(true);
                        buttonReport.setDisable(true);
                        break;
                    case "execution":
                        runButton.setDisable(false);
                        buttonReport.setDisable(false);
                        System.out.println("RESULT= " + selected.getIterationResult());
                        break;
                    case "baseline":
                        runButton.setDisable(false);
                        buttonReport.setDisable(true);
                        break;
                    default:
                        break;
                }
            }
        });

        //Listener to open the right tab when the user double click on an object of the table
        this.campaignBaselineAndExecution.setOnMousePressed((MouseEvent event) -> {
            if (event.getClickCount() == 2 && selected != null) {
                switch (selected.getType()) {
                    case "campaign":
                        main.viewTestCampaign(selected.getTestCampaign());
                        break;
                    case "execution":
                        main.displayViewTab(selected);
                        break;
                    case "baseline":
                        main.displayViewTab(selected);
                        break;
                    default:
                        break;
                }
            }
        });
//

//        if (TabTestCampaignRepositoryController.observableListTestCampaign.isEmpty()) {
//            this.baselineButton.setDisable(true);
//        }
        baselineButton.setDisable(TabTestCampaignRepositoryController.observableListTestCampaign.isEmpty());

        TabTestCampaignRepositoryController.observableListTestCampaign.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                baselineButton.setDisable(TabTestCampaignRepositoryController.observableListTestCampaign.isEmpty());
            }

        });

        this.loadCSS();
        this.defineCursor();
    }

    /**
     * initialize the reference of the class parent
     * TabTestCampaignExecutionMainViewController to this controller.
     *
     * @param mainController the reference to the parent class
     */
    public void init(TabTestCampaignExecutionMainViewController mainController) {
        TabTestCampaignExecutionRepositoryBaselineController.main = mainController;
    }

    /**
     * Update the treeView of Campaign, Baselines and Executions
     *
     * @throws ParseException
     */
    public void updateRepository() throws ParseException {
        long tempsDebut = System.currentTimeMillis();
        UpdateTreeItem();
        long tempsFin = System.currentTimeMillis();
        float seconds = (tempsFin - tempsDebut) / 1000F;
        //System.out.println("TEMPS TOTAL UPDATE= " + Float.toString(seconds));
        if (this.runButton != null) {
            this.runButton.setDisable(true);
        }
    }

    /**
     * Open a popUp to select a campaign to baseline
     */
    private void selectCampaignToBaseline() {

        try {
            PopUpCampaignSelectionController dialogController;
            ObservableList<TestCampaign> campaignInDB = FXCollections.observableArrayList(new ArrayList<TestCampaign>(testCampaignHandler.getAllCampaigns()));
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane editPane = fxmlLoader.load(getClass().getResource("/assets/view/popup/popUpCampaignSelection.fxml").openStream());
            dialogController = fxmlLoader.getController();
            popUpCampaignStage = new Stage();
            popUpCampaignStage.setTitle("Campaign Selection");
            popUpCampaignStage.initOwner(Main.primaryStage);
            popUpCampaignStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(editPane);
            popUpCampaignStage.setScene(scene);
            PopUpCampaignSelectionController controller = fxmlLoader.getController();
            controller.init(this);
            popUpCampaignStage.setOnCloseRequest((WindowEvent event) -> {
                this.closePopUp();
            });
            controller.setTable(campaignInDB);
            popUpBaselineOpen = true;

            popUpCampaignStage.show();//.showAndWait();
            popUpCampaignStage.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - popUpCampaignStage.getWidth() / 2);
            popUpCampaignStage.setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2 - popUpCampaignStage.getHeight() / 2);
//            popUpCampaignStage.setX(main.getMainController().getPrimaryStage().getX() + main.getMainController().getPrimaryStage().getWidth() / 2 - popUpCampaignStage.getWidth() / 2);
//            popUpCampaignStage.setY(main.getMainController().getPrimaryStage().getY() + main.getMainController().getPrimaryStage().getHeight() / 2 - popUpCampaignStage.getHeight() / 2);
            dialogController.setPrimaryStage(popUpCampaignStage);

        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
    }

    /**
     * Preparation and initialization of the columns for the treeView of
     * Campaigns,Baselines and executions
     */
    private void prepareTreeViewColumns() {
        initColumn initColumnTreeView = new initColumn();
        initColumnTreeView.prepareTreeViewTabTestExecution(campaignBaselineAndExecution);
    }

    /**
     * Action to perform when the user has confirmed a campaign to baseline in
     * the PopUp
     *
     * @param campaignSelected
     */
    public void setAction(TestCampaign campaignSelected) {
        TabTestCampaignExecutionRepositoryBaselineController.main.createBaseline(campaignSelected);
    }

    /**
     * Close the PopUp of campaign selection
     */
    public void closePopUp() {
        this.popUpCampaignStage.close();
        popUpBaselineOpen = false;
    }

    /**
     * get the Stage of the PopUp of campaign selection
     *
     * @return
     */
    public Stage getDialogStage() {
        return this.popUpCampaignStage;
    }

    /**
     * Open a new PopUp to run a new execution of a baseline
     *
     * @param baselineName the name of the baseline to run
     */
    public void runCampaign(String baselineName) {
        try {

            //Set the parameters of the PopUp in a thread because it can take time
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() {
                    runController.setParameters(baselineName, TabTestCampaignExecutionRepositoryBaselineController.this);
                    return null;
                }
            };
            Thread th = new Thread(task);
            th.setDaemon(true);
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane editPane = fxmlLoader.load(getClass().getResource("/assets/view/popup/popUpRun.fxml").openStream());
            runController = fxmlLoader.getController();
            runStage = new Stage();
            runStage.setTitle("Run");
            runStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(editPane);
            runStage.setScene(scene);

            runController.setPrimaryStage(runStage);
            runController.init(this);
            runStage.show();
            runStage.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - runStage.getWidth() / 2);
            runStage.setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2 - runStage.getHeight() / 2);
            runStage.setHeight(600);
            runStage.setWidth(800);
//            runStage.setX(main.getMainController().getPrimaryStage().getX() + main.getMainController().getPrimaryStage().getWidth() / 2 - runStage.getWidth() / 2);
//            runStage.setY(main.getMainController().getPrimaryStage().getY() + main.getMainController().getPrimaryStage().getHeight() / 2 - runStage.getHeight() / 2);
            th.start();
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
    }

    /**
     * Initialize all the buttons of the assets.view and their listener
     */
    private void iniButtons() {

        this.buttonDelete.setDisable(true);
        this.runButton.setDisable(true);
        buttonReport.setDisable(true);

        runButton.setOnAction((ActionEvent e) -> {
            if (this.selected == null) {
                e.consume();
            } else if (this.selected.getType().equals("baseline") || this.selected.getType().equals("execution")) {
                CommonFunctions.reportLog.info("User run iteration: " + this.selected.getBaselineId());
                this.runCampaign(selected.getBaselineId());
            }

        });

        buttonDelete.setOnAction((ActionEvent e) -> {
            if (selected == null) {
                e.consume();
            } else if (this.selected.getType().equals("execution")) {
                try {
                    iteHandler.deleteExecution(this.selected);
                    UpdateTreeItem();
                    CommonFunctions.reportLog.info("User delete execution: " + this.selected.getBaselineId() + "(Iteration Num: " + this.selected.getIterationNumber() + ")");
                } catch (ParseException ex) {
                    CommonFunctions.debugLog.error("Cannot update BaseLine", ex);
                } catch (GenericJDBCException ex) {
                    CommonFunctions.debugLog.error("Database File is Locked", ex);
                    CommonFunctions.displayAlert(Alert.AlertType.ERROR,
                            "Locked Database File", "The database file is locked by another application",
                            "Please refer to the log file for further information");
                }
            } else if (this.selected.getType().equals("baseline")) {
                testExecutionHandler.deleteBaseline(this.selected);
                CommonFunctions.reportLog.info("User delete baseline: " + this.selected.getBaselineId());
                ArrayList<Iterations> iterations = testExecutionHandler.getExecutionsFromBaseline(this.selected);
                iteHandler.deleteExecution(null);
                try {
                    UpdateTreeItem();
                } catch (ParseException ex) {
                    CommonFunctions.debugLog.error("", ex);
                }
            } else if (this.selected.getType().equals("campaign")) {
                ArrayList<Iterations> arrayIt = null;
                try {
                    arrayIt = testExecutionHandler.getBaselinesFromCampaign(this.selected.getTestCampaign());
                    StringBuilder reportMsgBuilder = new StringBuilder("User delete entire test campaign: " + this.selected.getBaselineId() + System.lineSeparator());
                    for (Iterations anArrayIt : arrayIt) {
                        testExecutionHandler.deleteBaseline(anArrayIt);
                        reportMsgBuilder.append("\tBaseline: ").append(anArrayIt.getBaselineId()).append(System.lineSeparator());
                        iteHandler.deleteExecution(null);
//                        arrayIterations = testExecutionHandler.getExecutionsFromBaseline(anArrayIt);
//                        for (Iterations arrayIteration : arrayIterations) {
//                            iteHandler.deleteExecution(arrayIteration);
//                        }
                    }
                    UpdateTreeItem();
                    CommonFunctions.reportLog.info(reportMsgBuilder.toString());
                    CommonFunctions.reportLog.info("User completed test campaign deletion.");
                } catch (ParseException ex) {
                    CommonFunctions.debugLog.error("", ex);
                } catch (GenericJDBCException ex) {
                    CommonFunctions.debugLog.error("Database File is Locked", ex);
                    CommonFunctions.displayAlert(Alert.AlertType.ERROR,
                            "Locked Database File", "The database file is locked by another application",
                            "Please refer to the log file for further information");
                }
            }
        });

        baselineButton.setOnAction((ActionEvent e) -> {
            if (popUpBaselineOpen) {
                popUpCampaignStage.requestFocus();
            } else {
                this.selectCampaignToBaseline();
            }
        });

        buttonReport.setOnAction((ActionEvent e) -> {
            if (selected == null) {
                e.consume();
            } else if (this.selected.getType().equals("baseline") || this.selected.getType().equals("execution")) {
                try {
                    WriteReport newReport = new WriteReport();
                    newReport.createReport(selected);
                    CommonFunctions.displayAlert(Alert.AlertType.INFORMATION, "Report generated", "Report generated, named: " + newReport.getFileName(), null);
                } catch (Exception ex) {
                    CommonFunctions.debugLog.debug("", ex);
                    CommonFunctions.displayAlert(Alert.AlertType.ERROR, "Error in generating Report", "The TAT can only generate a report based on successfully executed baselines", "Please choose a successfully executed baseline to report");
                }
            }
        });
    }

    /**
     * Load the css sheet of the assets.view
     */
    private void loadCSS() {
        this.anchorPaneTestExecution.getStylesheets().add("/assets/view/testexecution/cssLibraryTestCase.css");
    }

    /**
     *
     */
    public void selectDirectoryForReport() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Report generation directory");

        File selectedDirectory
                = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            //System.out.println("Start Time querry");
            File selectedFile;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open model orchestra");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showOpenDialog(Main.primaryStage);
            if (file != null) {
                util.startTime();
                iteHandler.initializeIteration(selected);
                // System.out.println("Time taken by request : ");
                util.endTime();
                // System.out.println("Start time excel");
                util.startTime();

                createOrchestra orchestra = new createOrchestra();
                orchestra.generateExcelRapport(selected, selectedDirectory, file.toPath().toString());
                orchestra = null;
                //selected = null;
                // System.out.println("Time taken by excel generation");
                util.endTime();
            }

        }

    }

    /**
     *
     */
    public void setBaselineButtonEnable() {
        this.runButton.setDisable(false);
    }

    /**
     * define a new cursor for buttons where an action is possible
     */
    private void defineCursor() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.baselineButton);
        //nodeHand.add(this.buttonRefreshCampaignRepository);
        nodeHand.add(this.buttonDelete);
        nodeHand.add(this.runButton);
        nodeHand.add(this.buttonReport);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }
}
