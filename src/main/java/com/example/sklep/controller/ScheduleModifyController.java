package com.example.sklep.controller;

import com.example.sklep.model.DBUtils;
import com.example.sklep.model.Product;
import com.example.sklep.model.Schedule;
import com.example.sklep.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class ScheduleModifyController {

    @FXML
    private Label Texts;



    @FXML
    private TableView<Schedule> scheduleTableView;

    @FXML
    private TableColumn<Schedule, Date> dayScheduleColumn;
    @FXML
    private TableColumn<Schedule, LocalTime> startScheduleColumn;

    @FXML
    private TableColumn<Schedule, LocalTime> endScheduleColumn;

    @FXML
    private ChoiceBox<Integer> userChoiceBox;

    @FXML
    private DatePicker dayOfWeekField;


    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    private ObservableList<Integer> userIds;


    private ObservableList<Schedule> scheduleData = FXCollections.observableArrayList();


    public void initialize() {
        try {

            userIds = DBUtils.getUserIdsFromDatabase();
            userChoiceBox.setItems(userIds);
            dayScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("dayOfWeek"));
            startScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));


            scheduleData.addAll(DBUtils.getSchedulesFromDatabase());
            scheduleTableView.setItems(scheduleData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddSchedule() {
        try {
            LocalDate selectedDate = dayOfWeekField.getValue();
            LocalTime startTime = LocalTime.parse(startTimeField.getText());
            LocalTime endTime = LocalTime.parse(endTimeField.getText());
            Integer selectedUserId = userChoiceBox.getValue();


            Date selectedDateAsDate = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Schedule newSchedule = new Schedule(selectedUserId, selectedDateAsDate, startTime, endTime);
            scheduleData.add(newSchedule);

            DBUtils.addScheduleToDatabase(newSchedule);

            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeleteSchedule() {
        try {
            Schedule selectedSchedule = scheduleTableView.getSelectionModel().getSelectedItem();
            scheduleData.remove(selectedSchedule);

            DBUtils.deleteScheduleFromDatabase(selectedSchedule);


            scheduleTableView.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleFilterSchedule() {
        try {
            Integer selectedUserId = userChoiceBox.getValue();
            ObservableList<Schedule> filteredSchedules = FXCollections.observableArrayList();

            for (Schedule schedule : scheduleData) {
                if (schedule.getUserId() == selectedUserId) {
                    filteredSchedules.add(schedule);
                }
            }

            scheduleTableView.setItems(filteredSchedules);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        dayOfWeekField.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
        userChoiceBox.setValue(null);
    }


}
