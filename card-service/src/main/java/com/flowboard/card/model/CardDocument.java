package com.flowboard.card.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cards")
public class CardDocument {

    @Id
    private Integer cardId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Integer)
    private Integer boardId;

    @Field(type = FieldType.Integer)
    private Integer listId;

    @Field(type = FieldType.Keyword)
    private String priority;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Integer)
    private Integer assigneeId;

    @Field(type = FieldType.Boolean)
    private Boolean isArchived;
}
