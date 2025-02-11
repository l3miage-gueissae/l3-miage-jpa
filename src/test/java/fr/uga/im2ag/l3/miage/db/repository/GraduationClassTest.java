package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.GraduationClass;
import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.TeacherRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraduationClassTest extends Base {

    GraduationClassRepository classRepository;
    TeacherRepository teacherRepository;
    SubjectRepository subjectRepository;

    @BeforeEach
    void before() {
        classRepository = daoFactory.newGraduationClassRepository(entityManager);
        teacherRepository = daoFactory.newTeacherRepository(entityManager);
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveClass() {
    	   final var Gc = Fixtures.createClass();
    	   final var testGc = Fixtures.createClass();
           System.out.println(Gc.getName());
          
           entityManager.getTransaction().begin();
//         Test de la contrainte unique name + year 
           Gc.setName("a");
           Gc.setYear(1);
           /*testGc.setName("a");
           testGc.setYear(1);*/
           classRepository.save(testGc);
           classRepository.save(Gc);
          
           entityManager.getTransaction().commit();
           entityManager.detach(Gc); 

           var pGc = classRepository.findById(Gc.getId());
           assertThat(pGc).isNotNull().isNotSameAs(Gc);
           assertThat(pGc.getName()).isEqualTo(Gc.getName());
           assertThat(pGc.getYear()).isEqualTo(Gc.getYear());
    }


    @Test
    void shouldFindByYearAndName() {
       
    	final var graduationClass = Fixtures.createClass();
    	final var subject = Fixtures.createSubject();
    	final var teacher = Fixtures.createTeacher(subject,graduationClass);
    	
        graduationClass.setYear(2004);
        graduationClass.setName("BaseDeDoDo");
        
        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        classRepository.save(graduationClass);
        teacherRepository.save(teacher);
        
        entityManager.getTransaction().commit();
        entityManager.detach(graduationClass);
        entityManager.detach(subject);
        entityManager.detach(teacher);
        
        
        
        GraduationClass LaClasse = classRepository.findByYearAndName(2004,"BaseDeDoDo"); // problem 
    	
        // on v�rifie que l'objet existe
        assertThat(LaClasse).isNotNull();
        
    	// On r�cup�re le nom de la GraduationClass
    	String name = LaClasse.getName() ;
    	
    	// On v�rifie qu'il a bien trouv� la bonne GraduationClass avec le bon nom
    	assertThat(name).isEqualTo("BaseDeDoDo");	
    	
    	
    	// On r�cup�re l'ann�e de la GraduationClass
    	Integer annee = LaClasse.getYear();
    	
    	assertThat(annee).isEqualTo(2004);	
    	
    	// V�rification que getAll <GraduationClass> fonctionne 
    	System.out.println(classRepository.getAll().get(0).getName());
    }
    
    @Test
    void shouldGetAllGraduationClass() {
            // Initialisation d'une liste N grade    	
    		ArrayList<GraduationClass> lesClasses = new ArrayList<GraduationClass>();
            entityManager.getTransaction().begin();
       
    		for(int i = 0; i < 10; i++) {
                final var classe = Fixtures.createClass();
                lesClasses.add(classe);
                classRepository.save(classe);

    		}
    		entityManager.getTransaction().commit();
    		for(int i = 0; i < lesClasses.size(); i++) {
                entityManager.detach(lesClasses.get(i));
    		}
    		
    		ArrayList<GraduationClass> plesClasses = new ArrayList<GraduationClass>();
    		plesClasses = new ArrayList(classRepository.getAll());
    		assertThat(plesClasses).isNotEmpty();
    		assertThat(plesClasses.size()).isEqualTo(lesClasses.size());
    		for(int i = 0; i < plesClasses.size(); i++) {
    		
               assertThat(plesClasses.get(i).getId()).isEqualTo(lesClasses.get(i).getId());
               assertThat(plesClasses.get(i).getName()).isEqualTo(lesClasses.get(i).getName());
               assertThat(plesClasses.get(i).getYear()).isEqualTo(lesClasses.get(i).getYear());
    		}
    		
    }
    
    @Test
    void shouldDeleteClass() {
    	  final var classe = Fixtures.createClass();

          entityManager.getTransaction().begin();
          classRepository.save(classe);
          entityManager.getTransaction().commit();
          
          classRepository.delete(classe);
          
          GraduationClass pClasse = classRepository.findById(classe.getId());
          assertThat(pClasse).isNull();
          
    }

}
