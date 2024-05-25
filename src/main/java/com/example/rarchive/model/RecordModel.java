package com.example.rarchive.model;

import com.example.rarchive.entity.RecordEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.DateFormat;

@Getter
@Setter
public class RecordModel implements Serializable {

    private String groupTitle;
    private String title;
    private String status;
    private String date;


    public static RecordModel toModel(RecordEntity record) {
        RecordModel recordModel = new RecordModel();

        DateFormat df = DateFormat.getDateInstance();
        recordModel.setDate(df.format(record.getDate()));

        recordModel.setGroupTitle(record.getGroupEntity().getTitle());
        recordModel.setTitle(record.getTitle());
        recordModel.setStatus(record.getStatus());

        return recordModel;
    }
}
