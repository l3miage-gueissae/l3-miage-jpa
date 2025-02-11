package fr.uga.im2ag.l3.miage.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="HighestGrade",query="SELECT g from Grade g where g.value > :limit"),
	@NamedQuery(name="HighestGrade-Subject",query="SELECT g from Grade g join g.subject subj where g.value > :limit and subj.id =:subjid")
})
public class Grade {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Subject subject;
    @Column(name = "grade", updatable = false , nullable = false) //
    private Float value;
    private Float weight;

    public Long getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public Grade setSubject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public Float getValue() {
        return value;
    }

    public Grade setValue(Float value) {
        this.value = value;
        return this;
    }

    public Float getWeight() {
        return weight;
    }

    public Grade setWeight(Float weight) {
        this.weight = weight;
        return this;
    }
}
