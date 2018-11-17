package com.ums.pau;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;


public class FacultyDashboardController {
    @FXML
    public JFXTreeTableView treeTableView;
    @FXML
    public TreeTableColumn idCol;
    @FXML
    public TreeTableColumn nameCol;
    @FXML
    public TreeTableColumn dptCol;
    @FXML
    public TreeTableColumn cgpaCol;
    @FXML
    public JFXTextField searchTF;
    @FXML
    public JFXTextField idTF;
    @FXML
    public JFXTextField nameTF;
    @FXML
    public JFXTextField dptTF;
    @FXML
    public JFXTextField cgpaTF;
    @FXML
    public Label idLB;
    @FXML
    public Label nameLB;
    @FXML
    public Label dptLB;
    @FXML
    public Label cgpaLB;

//    @FXML
//    public void switchToLogin() throws IOException {
//        new SceneSwitcher().switchSceneTo("resources/studentLogin.fxml");
//    }
}