package fr.uga.im2ag.l3.miage.db.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

// TODO ajouter une named query pour une des requêtes à faire dans le repository
@Entity
@NamedQueries({
	@NamedQuery(name="Student-AvgAbove",query="SELECT st from Student st join st.grades grades having ( sum(grades.value * grades.weight)/ sum(weight ))>:avg")
})
public class Student extends Person {
	@ManyToOne
    private GraduationClass belongTo;
	@OneToMany
    private List<Grade> grades;

    public GraduationClass getBelongTo() {
        return belongTo;
    }

    public Student setBelongTo(GraduationClass belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public Student setGrades(List<Grade> grades) {
        this.grades = grades;
        return this;
    }
}
