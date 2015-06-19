package com.srccodes.example.hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

import org.hibernate.search.annotations.*;

/**
 * The persistent class for the contact database table.
 */
@Entity
@Indexed
@AnalyzerDef(name = "customanalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
                , @TokenFilterDef(factory = SnowballPorterFilterFactory.class

        )
        })
@Table(name = "contact")
public class Contact {

    @Id
    @DocumentId
    private Integer id;

    @Field(index = Index.YES, store = Store.NO)
    @Analyzer(definition = "customanalyzer")
    private String name;
    private String email;

    public Contact() {

    }

    public Contact(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Id: ").append(this.getId()).append(" | Name:").append(this.getName()).append(" | Email:").append(this.getEmail());

        return stringBuilder.toString();
    }
}