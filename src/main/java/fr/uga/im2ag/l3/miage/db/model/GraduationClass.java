package fr.uga.im2ag.l3.miage.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

// TODO ajouter une named query pour une des requÃªtes Ã  faire dans le repository
/*Il peut y avoir qu'un nom de matière par year
* ex probleme : name = test, year = 2020 - name = test, year = 2020
* ex correct  : name = test, year = 2019 - name = test, year = 2020  
*/
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueNameByYear", columnNames = { "name", "laDate" }) })
public class GraduationClass {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(name = "laDate") // year est un type en base de données
    private Integer year;
    @OneToMany(mappedBy ="belongTo")
    private List<Student> students;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GraduationClass setId(Long id) {
        this.id = id;
        return this;
    }

    public GraduationClass setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public GraduationClass setYear(Integer year) {
        this.year = year;
        return this;
    }

    public List<Student> getStudents() {
        return students;
    }

    public GraduationClass setStudents(List<Student> students) {
        this.students = students;
        return this;
    }

    public void addStudent(Student student) {
        if (students == null) {
            students = new ArrayList<>();
        }
        students.add(student);
    }
}
