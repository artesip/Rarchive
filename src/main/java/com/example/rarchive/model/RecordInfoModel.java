package com.example.rarchive.model;

import com.example.rarchive.entity.RecordInfoEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordInfoModel {
    private String description;

    private int rating;

    private String status;

    private String link;

    public static RecordInfoModel toModel(RecordInfoEntity recordInfoEntity) {
        RecordInfoModel model = new RecordInfoModel();

        model.setDescription(recordInfoEntity.getDescription());
        model.setLink(recordInfoEntity.getLink());
        model.setRating(recordInfoEntity.getRating());
        model.setStatus(recordInfoEntity.getStatus());

        return model;
    }
}
